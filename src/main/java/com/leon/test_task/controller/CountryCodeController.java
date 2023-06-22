package com.leon.test_task.controller;

import com.leon.test_task.service.CountryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for getting country code from phone number
 */
@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryCodeController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<String> getCountryCode(@Valid @RequestParam(value = "phone") String phone) {
        String countryDTO = countryService.getCountryFromPhone(phone);
        return ResponseEntity.ok(countryDTO);
    }
}
