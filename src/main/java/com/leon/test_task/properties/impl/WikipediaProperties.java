package com.leon.test_task.properties.impl;

import com.leon.test_task.properties.Properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link Properties} interface for Wikipedia.
 *
 * @see Properties
 */
@Data
@Component
@ConfigurationProperties(prefix = "wikipedia")
public class WikipediaProperties implements Properties {
    private String link;
    private String xPatForTable;
}