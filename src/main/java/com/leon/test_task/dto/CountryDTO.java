package com.leon.test_task.dto;

import lombok.Data;

@Data
public class CountryDTO {
    private String country;

    public CountryDTO(String country) {
        this.country = country;
    }
}
