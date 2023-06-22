package com.leon.test_task.service.impl;

import com.leon.test_task.exception.ConnectException;
import com.leon.test_task.service.ParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ParserService} interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {

    /**
     * Connects to url and gets html from it, then gets elements from html by xPath
     *
     * @param url   url to get html from
     * @param xPath xPath to get elements from html
     * @return elements from html
     */
    @Override
    public Elements getElementsFromUrl(String url, String xPath) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.selectXpath(xPath);
        } catch (Exception e) {
            throw new ConnectException("Error while getting html from url: " + url);
        }
    }

}
