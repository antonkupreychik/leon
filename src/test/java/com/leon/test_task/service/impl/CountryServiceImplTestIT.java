package com.leon.test_task.service.impl;

import com.leon.test_task.annotations.EnableTestcontainers;
import com.leon.test_task.exception.CountryCodeException;
import com.leon.test_task.model.CountryCode;
import com.leon.test_task.repository.CountryCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableTestcontainers
@ActiveProfiles("test")
class CountryServiceImplTestIT {

    @Autowired
    CountryServiceImpl countryService;

    @Autowired
    CountryCodeRepository countryCodeRepository;

    @Test
    @Rollback
    void should_return_country_by_code() throws CountryCodeException {
        CountryCode countryCodeToSave = CountryCode.builder()
                .code("001")
                .country("Zero One country")
                .build();

        countryCodeRepository.save(countryCodeToSave);

        assertEquals("Zero One country", countryService.getCountryFromPhone("0011112223"));
    }

    @Test
    @Rollback
    void should_return_validation_error_message() {
        assertThrows(CountryCodeException.class, () -> countryService.getCountryFromPhone("000"));
    }

    @Test
    @Rollback
    void should_not_return_country() {
        assertThrows(CountryCodeException.class, () -> countryService.getCountryFromPhone("0021112223"));
    }
}
