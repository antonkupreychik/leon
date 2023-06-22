package com.leon.test_task.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class ValidateServiceImplTest {

    @InjectMocks
    ValidateServiceImpl validateService;

    @ParameterizedTest
    @ValueSource(strings = {
            "2055550125",
            "202 555 0125",
            "(202) 555-0125",
            "111 (202) 555-0125",
            "636 856 789",
            "111 636 856 789",
            "375 447 357 152",
            "636 85 67 89",
            "111 636 85 67 89"})
    void should_validate_all_numbers(String phone) {
        assertTrue(validateService.isValidPhoneNumber(phone));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "2055F550125",
            "(202 555 0125",
            "()202) 555-0125",
            "++111 (202) 555-0125",
            "636 856 789-",
            "+111 636 856 789!",
            "636 85 67 89s",
            "+111 636 85 67 89 9"})
    void should_not_validate_all_numbers(String phone) {
        assertFalse(validateService.isValidPhoneNumber(phone));
    }

}