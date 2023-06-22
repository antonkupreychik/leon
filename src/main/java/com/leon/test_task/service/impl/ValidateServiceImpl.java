package com.leon.test_task.service.impl;

import com.leon.test_task.service.ValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link ValidateService} interface
 */
@Slf4j
@Service
public class ValidateServiceImpl implements ValidateService {

    /**
     * Regex for phone number validation
     */
    private static final String PATTERNS = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

    /**
     * Validates phone number, using regex
     *
     * @param phone phone number to validate
     * @return true if phone number is valid
     */
    @Override
    public boolean isValidPhoneNumber(String phone) {
        log.info("Validating phone number: {}", phone);
        Pattern pattern = Pattern.compile(PATTERNS);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
