package com.leon.test_task.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception for errors while parsing
 */
@Slf4j
public class ParseException extends RuntimeException {
    public ParseException(String message) {
        super(message);
        log.info("Error while parsing: {}", message);
    }
}
