package com.leon.test_task.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.leon.test_task.repository.CountryCodeRepository;
import com.leon.test_task.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.leon.test_task.utils.Utils.UNKNOWN;

/**
 * Implementation of {@link CacheService} interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {


    private final CountryCodeRepository countryCodeRepository;

    /**
     * Cache for country codes. It is used to reduce the number of requests to the database.
     * The cache is updated every 30 minutes.
     * The maximum number of entries in the cache is 1000.
     */
    private final LoadingCache<String, String> countryCodesCache =
            CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .build(new CacheLoader<>() {
                        @Override
                        public @NotNull String load(@NotNull String countryCode) {
                            return countryCodeRepository.findByCode(countryCode)
                                    .orElse(UNKNOWN);
                        }
                    });

    /**
     * Put a key-value pair to cache
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void put(String key, String value) {
        String[] codes = key.split(",");
        for (String code : codes) {
            if (countryCodesCache.getIfPresent(code) == null) {
                countryCodesCache.put(code, value);
            } else {
                String currentValue = countryCodesCache.getIfPresent(code);
                countryCodesCache.put(code, currentValue + "," + value);
            }
        }
    }

    /**
     * Get value from cache by key
     *
     * @param key key
     * @return value from cache by key
     */
    @Override
    public String get(String key) {
        return countryCodesCache.getUnchecked(key);
    }
}
