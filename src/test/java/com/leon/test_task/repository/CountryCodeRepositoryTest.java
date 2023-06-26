package com.leon.test_task.repository;

import com.leon.test_task.annotations.EnableTestcontainers;
import com.leon.test_task.model.CountryCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@EnableTestcontainers
@Transactional
class CountryCodeRepositoryTest {

    @Autowired
    CountryCodeRepository countryCodeRepository;

    @Test
    @Rollback
    void should_save_country_to_db() {
        CountryCode countryCodeToSave = CountryCode.builder()
                .code("000")
                .country("Zero country")
                .build();
        countryCodeRepository.save(countryCodeToSave);
        Assertions.assertEquals(1, countryCodeRepository.findAll().size());
    }

    @Test
    @Rollback
    void should_find_country_by_code() {
        CountryCode countryCodeToSave = CountryCode.builder()
                .code("000")
                .country("Zero country")
                .build();
        countryCodeRepository.save(countryCodeToSave);
        Assertions.assertEquals("Zero country", countryCodeRepository.findByCode("000").isPresent() ? countryCodeRepository.findByCode("000").get() : null);
    }
}

