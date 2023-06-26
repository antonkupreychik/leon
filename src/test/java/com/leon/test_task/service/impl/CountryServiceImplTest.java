package com.leon.test_task.service.impl;

import com.leon.test_task.config.AppConfig;
import com.leon.test_task.exception.CountryCodeException;
import com.leon.test_task.exception.ParseException;
import com.leon.test_task.properties.Properties;
import com.leon.test_task.repository.CountryCodeRepository;
import com.leon.test_task.service.CacheService;
import com.leon.test_task.service.ParserService;
import com.leon.test_task.service.ValidateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {
    @Mock
    Properties properties;
    @Mock
    AppConfig appConfig;
    @Mock
    CountryCodeRepository countryCodeRepository;
    @Mock
    ParserService parserService;
    @Mock
    CacheService cacheService;
    @Mock
    ValidateService validateService;
    @InjectMocks
    CountryServiceImpl countryServiceImpl;

    String PATH_TO_CORRECT_HTML = "src/test/resources/html/correct-data.html";
    String PATH_TO_INCORRECT_HTML = "src/test/resources/html/incorrect-data.html";
    String UTF_8 = "UTF-8";
    String PATH = "/html/body/table/tbody";
    String INVALID_PHONE_NUMBER = "Invalid phone number";
    String VALID_PHONE_NUMBER = "1 202-456-1111";
    String VALID_COUNTRY = "USA";
    String UNKNOWN = "Unknown country";

    @Test
    void should_init_with_correct_data() throws IOException {
        File file = new File(PATH_TO_CORRECT_HTML);
        Document document = Jsoup.parse(file, UTF_8);
        Elements elements = document.selectXpath(PATH);
        when(appConfig.isTest()).thenReturn(false);
        when(properties.getLink()).thenReturn("some link");
        when(properties.getXPatForTable()).thenReturn("some xpath");
        when(parserService.getElementsFromUrl(properties.getLink(), properties.getXPatForTable()))
                .thenReturn(elements);

        assertDoesNotThrow(() -> countryServiceImpl.init());
    }

    @Test
    void should_return_exception_because_cannot_parse() throws IOException {
        File file = new File(PATH_TO_INCORRECT_HTML);
        Document document = Jsoup.parse(file, UTF_8);
        Elements elements = document.selectXpath(PATH);

        when(appConfig.isTest()).thenReturn(false);
        when(properties.getLink()).thenReturn("some link");
        when(properties.getXPatForTable()).thenReturn("some xpath");
        when(parserService.getElementsFromUrl(properties.getLink(), properties.getXPatForTable()))
                .thenReturn(elements);

        assertThrows(ParseException.class, () -> countryServiceImpl.init());
    }

    @Test
    void should_return_unknown_because_validation_failed() {
        when(validateService.isValidPhoneNumber(INVALID_PHONE_NUMBER)).thenReturn(false);
        assertThrows(CountryCodeException.class, () -> countryServiceImpl.getCountryFromPhone(INVALID_PHONE_NUMBER));
    }

    @Test
    void should_return_unknown_because_cache_is_empty() {
        when(validateService.isValidPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(true);
        when(cacheService.get(anyString())).thenReturn(UNKNOWN);
        assertThrows(CountryCodeException.class, () -> countryServiceImpl.getCountryFromPhone(VALID_PHONE_NUMBER));
    }

    @Test
    void should_return_country() throws CountryCodeException {
        when(validateService.isValidPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(true);
        when(cacheService.get("1")).thenReturn(VALID_COUNTRY);
        assertEquals(VALID_COUNTRY, countryServiceImpl.getCountryFromPhone(VALID_PHONE_NUMBER));
    }
}