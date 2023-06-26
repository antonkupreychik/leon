package com.leon.test_task.service.impl;

import com.leon.test_task.config.AppConfig;
import com.leon.test_task.exception.CountryCodeException;
import com.leon.test_task.exception.ParseException;
import com.leon.test_task.exception.enums.ExceptionType;
import com.leon.test_task.model.CountryCode;
import com.leon.test_task.properties.Properties;
import com.leon.test_task.repository.CountryCodeRepository;
import com.leon.test_task.service.CacheService;
import com.leon.test_task.service.CountryService;
import com.leon.test_task.service.ParserService;
import com.leon.test_task.service.ValidateService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.leon.test_task.utils.Utils.COMMA;
import static com.leon.test_task.utils.Utils.LEFT_PARENTHESIS;
import static com.leon.test_task.utils.Utils.RIGHT_PARENTHESIS;
import static com.leon.test_task.utils.Utils.UNKNOWN;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Implementation of {@link CountryService} interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final Properties properties;
    private final CountryCodeRepository countryCodeRepository;
    private final ParserService parserService;
    private final CacheService cacheService;
    private final ValidateService validateService;
    private final AppConfig appConfig;

    /**
     * Gets country code from row and builds {@link CountryCode} object
     */
    @PostConstruct
    @Profile("!test")
    public void init() {
        if (!appConfig.isTest()) {
            log.info("Scanning properties: {}", properties);
            Elements textFromProperties = parserService.getElementsFromUrl(properties.getLink(), properties.getXPatForTable());
            saveCountryCodes(textFromProperties);
            log.info("Country codes saved to DB and ready to use");
        } else {
            log.info("Test mode is on. Skipping init");
        }
    }

    /**
     * Gets country name from phone number. If country name is not found, returns {@link com.leon.test_task.utils.Utils#UNKNOWN}
     *
     * @param phone phone number
     * @return country name
     */
    @Override
    public String getCountryFromPhone(String phone) throws CountryCodeException {
        if (validateService.isValidPhoneNumber(phone)) {
            String country;
            phone = removeNonDigits(phone);
            for (int i = 1; i < phone.length() + 1; i++) {
                String countryCodeCandidate = phone.substring(0, i);
                country = cacheService.get(countryCodeCandidate);
                if (!country.equals(UNKNOWN)) {
                    log.info("Country '{}' found for phone: {}", country, phone);
                    return country;
                }
            }
            log.warn("Country not found for phone: {}", phone);
            throw new CountryCodeException(ExceptionType.NOT_FOUND_EXCEPTION, "Country not found");
        } else {
            log.warn("Phone number is not valid: {}", phone);
            throw new CountryCodeException(ExceptionType.VALIDATION_EXCEPTION, "Phone number is not valid");
        }
    }

    /**
     * Get country code from an element and builds {@link CountryCode} object
     * Save country code to DB and cache.
     * <p>
     * Example of an element:
     * <tr>
     * <td>1</td>
     * <td>Canada, United States, U.S. territories</td>
     * </tr>
     *
     * @param elementsFromTable elements from table
     */
    private void saveCountryCodes(Elements elementsFromTable) {
        try {
            checkFovElementsNotNull(elementsFromTable);
            Element mainElement = elementsFromTable.get(0);
            List<CountryCode> countryCodes = filterBlankNodes(mainElement)
                    .stream()
                    .skip(1)
                    .map(row -> buildCountryCode(filterBlankNodes(row)))
                    .toList();
            countryCodeRepository.saveAll(countryCodes);
            log.info("Country codes saved to DB. Total count: {}", countryCodes.size());

            countryCodes.forEach(countryCode -> cacheService.put(countryCode.getCode(), countryCode.getCountry()));
            log.info("Country codes saved to cache. Total count: {}", countryCodes.size());
        } catch (Exception e) {
            throw new ParseException("Error while parsing html from url: " + properties.getLink());
        }
    }

    /**
     * Check if elements from table are not null
     *
     * @param elementsFromTable elements from table
     */
    private void checkFovElementsNotNull(Elements elementsFromTable) {
        if (elementsFromTable == null || elementsFromTable.isEmpty()) {
            throw new ParseException("Elements from table is empty");
        }
    }

    /**
     * Filter blank nodes from row
     *
     * @param row row from table
     * @return list of nodes without blank nodes
     */
    private List<Node> filterBlankNodes(Node row) {
        return row.childNodes()
                .stream()
                .filter(el -> isNotBlank(el.toString()))
                .toList();
    }

    /**
     * Build {@link CountryCode} object from columns.
     * If the country has a flag, then the country name is in second child node, otherwise in first child node.
     *
     * @param columns columns from table
     * @return {@link CountryCode} object
     */
    private CountryCode buildCountryCode(List<Node> columns) {
        String codes = columns.get(1).childNodes().get(1).childNode(0).toString();
        StringBuilder defaultCode = new StringBuilder(codes.split(SPACE)[0]);
        if (codes.contains(LEFT_PARENTHESIS)) {
            String[] additionalCodes = codes
                    .substring(codes.indexOf(LEFT_PARENTHESIS) + 1, codes.indexOf(RIGHT_PARENTHESIS))
                    .split(COMMA);
            for (String additionalCode : additionalCodes) {
                defaultCode.append(COMMA).append(additionalCode.trim());
            }
        }
        return CountryCode.builder()
                .country((isHaveFlag(columns) ? columns.get(0).childNodes().get(1) : columns.get(0).childNodes().get(0)).toString())
                .code(defaultCode.toString())
                .build();
    }

    /**
     * Check if the country has a flag
     *
     * @param columns columns from table
     * @return true, if the country has a flag, otherwise false
     */
    private boolean isHaveFlag(List<Node> columns) {
        return columns.get(0).childNodes().size() == 2;
    }

    /**
     * Remove all non-digits from phone number
     *
     * @param phone phone number
     * @return phone number without non-digits
     */
    private String removeNonDigits(String phone) {
        return phone.replaceAll("\\D", "");
    }
}
