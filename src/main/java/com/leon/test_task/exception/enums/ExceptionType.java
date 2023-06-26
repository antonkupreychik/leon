package com.leon.test_task.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionType {
    VALIDATION_EXCEPTION("Validation exception"),
    NOT_FOUND_EXCEPTION("Not found exception");

    final String message;
}
