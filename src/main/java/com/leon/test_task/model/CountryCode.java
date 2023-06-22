package com.leon.test_task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity for country code.
 */
@Entity
@Table(name = "country_code")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "country")
    @Pattern(regexp = "^[A-Z][a-z]+$",
            message = "Country must start with capital letter and contain only letters")
    private String country;

    @Column(name = "code")
    @Min(value = 1, message = "Code must be greater than 0")
    @Max(value = 999, message = "Code must be less than 1000")
    private String code;
}
