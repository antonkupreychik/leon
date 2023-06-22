package com.leon.test_task.service;

import org.jsoup.select.Elements;

public interface ParserService {
    Elements getElementsFromUrl(String url, String xPath);
}
