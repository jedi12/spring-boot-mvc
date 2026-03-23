package com.example.springbootmvc.configs;

import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                // Если Redis упал при попытке чтения — просто логируем и идем в БД
                System.err.println("Redis недоступен (GET). Идем в БД. Ошибка: " + e.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                System.err.println("Redis недоступен (PUT). Данные не сохранены в кэш.");
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                System.err.println("Redis недоступен (EVICT).");
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                System.err.println("Redis недоступен (CLEAR).");
            }
        };
    }
}