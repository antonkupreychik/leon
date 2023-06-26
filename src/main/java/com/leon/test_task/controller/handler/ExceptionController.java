package com.leon.test_task.controller.handler;

import com.leon.test_task.dto.ErrorDTO;
import com.leon.test_task.exception.CountryCodeException;
import com.leon.test_task.exception.enums.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CountryCodeException.class)
    public ResponseEntity<ErrorDTO> handleCountryCodeException(CountryCodeException ex) {
        log.warn("Country code exception: {}", ex.getMessage());
        if (ex.getExceptionType().equals(ExceptionType.NOT_FOUND_EXCEPTION)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorDTO(ex));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorDTO(ex));
        }
    }

    private ErrorDTO buildErrorDTO(CountryCodeException ex) {
        return ErrorDTO.builder()
                .exceptionType(ex.getExceptionType().getMessage())
                .message(ex.getMessage())
                .build();
    }

}
