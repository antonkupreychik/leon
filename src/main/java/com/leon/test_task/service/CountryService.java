package com.leon.test_task.service;

import com.leon.test_task.exception.CountryCodeException;

public interface CountryService {
    String getCountryFromPhone(String phone) throws CountryCodeException;
}
