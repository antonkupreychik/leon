package com.leon.test_task.dto;

import lombok.Data;

@Data
public class CountryDTO {
    private String message;

    public CountryDTO(String message) {
        this.message = message;
    }
}
