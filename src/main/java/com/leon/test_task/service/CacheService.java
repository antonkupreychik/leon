package com.leon.test_task.service;

public interface CacheService {

    void put(String key, String value);

    String get(String key);
}
