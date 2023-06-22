package com.leon.test_task.repository;

import com.leon.test_task.model.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link CountryCode} entity.
 */
@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {
    /**
     * Returns country name by country code.
     *
     * @param code country code
     * @return country name
     */
    @Query("SELECT c.country FROM CountryCode c WHERE c.code = ?1")
    Optional<String> findByCode(String code);
}
