package com.leon.test_task.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception for errors while connecting to url
 */
@Slf4j
public class ConnectException extends RuntimeException {
    public ConnectException(String message) {
        super(message);
        log.info("Error while getting html from url: {}", message);
    }
}
