package com.notayessir.processor.configure;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置，简单使用本地 map 缓存表信息
 */
@Configuration
@EnableCaching
public class CacheConfiguration {


    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("columns");
    }

}
