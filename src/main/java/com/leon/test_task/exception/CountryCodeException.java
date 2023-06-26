package com.leon.test_task.exception;

import com.leon.test_task.exception.enums.ExceptionType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CountryCodeException extends Exception {

    private final ExceptionType exceptionType;
    private final String message;

    public CountryCodeException(ExceptionType exceptionType, String message) {
        super(exceptionType.getMessage());
        this.message = message;
        this.exceptionType = exceptionType;
    }
}
