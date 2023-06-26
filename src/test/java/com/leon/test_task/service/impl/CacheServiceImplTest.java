package com.leon.test_task.service.impl;

import com.leon.test_task.repository.CountryCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

    @Mock
    CountryCodeRepository countryCodeRepository;

    @InjectMocks
    CacheServiceImpl cacheService;

    String correctCountryCode = "1";
    String incorrectCountryCode = "2";
    String correctCountryName = "USA";

    @Test
    void should_save_to_cache_and_return() {
        cacheService.put(correctCountryCode, correctCountryName);
        assertEquals(correctCountryName, cacheService.get(correctCountryCode));
    }

    @Test
    void should_return_incorrect_code() {
        cacheService.put(correctCountryCode, correctCountryName);
        assertNotEquals(incorrectCountryCode, cacheService.get(correctCountryCode));
    }

    @Test
    void should_not_find_in_cache_and_scan_in_db() {
        when(countryCodeRepository.findByCode(incorrectCountryCode))
                .thenReturn(Optional.empty());
        assertEquals("Unknown country", cacheService.get(incorrectCountryCode));
    }

    @Test
    void should_find_in_cache_and_not_scan_in_db() {
        cacheService.put(correctCountryCode, correctCountryName);

        assertEquals(correctCountryName, cacheService.get(correctCountryCode));
        verify(countryCodeRepository, never()).findByCode(correctCountryCode);
    }
}