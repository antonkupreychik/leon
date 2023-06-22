package com.leon.test_task.service.impl;

import com.leon.test_task.exception.ParseException;
import com.leon.test_task.properties.Properties;
import com.leon.test_task.repository.CountryCodeRepository;
import com.leon.test_task.service.CacheService;
import com.leon.test_task.service.ParserService;
import com.leon.test_task.service.ValidateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {
    @Mock
    Properties properties;
    @Mock
    CountryCodeRepository countryCodeRepository;
    @Mock
    ParserService parserService;
    @Mock
    CacheService cacheService;
    @Mock
    ValidateService validateService;
    @InjectMocks
    CountryServiceImpl countryServiceImpl;

    String pathToCorrectHtml = "src/test/resources/correct-data.html";
    String pathToIncorrectHtml = "src/test/resources/incorrect-data.html";

    @Test
    void should_init_with_correct_data() throws IOException {
        File file = new File(pathToCorrectHtml);
        Document document = Jsoup.parse(file, "UTF-8");
        Elements elements = document.selectXpath("/html/body/table/tbody");

        when(properties.getLink()).thenReturn("some link");
        when(properties.getXPatForTable()).thenReturn("some xpath");
        when(parserService.getElementsFromUrl(properties.getLink(), properties.getXPatForTable()))
                .thenReturn(elements);

        assertDoesNotThrow(() -> countryServiceImpl.init());
    }

    @Test
    void should_return_exception_because_cannot_parse() throws IOException {
        File file = new File(pathToIncorrectHtml);
        Document document = Jsoup.parse(file, "UTF-8");
        Elements elements = document.selectXpath("/html/body/table/tbody");

        when(properties.getLink()).thenReturn("some link");
        when(properties.getXPatForTable()).thenReturn("some xpath");
        when(parserService.getElementsFromUrl(properties.getLink(), properties.getXPatForTable()))
                .thenReturn(elements);

        assertThrows(ParseException.class, () -> countryServiceImpl.init());
    }
}