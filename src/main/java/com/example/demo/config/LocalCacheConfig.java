package com.example.demo.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouyw
 * @date 2021-08-19
 * @describe com.nesun.multidim.config
 */
@Component
public class LocalCacheConfig {

    private long expireTime = 5;

    private Cache<String, Object> cache = CacheBuilder.newBuilder()
                    // 设置cache的初始大小，要合理设置该值
                    .initialCapacity(50)
                    // 设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
                    .concurrencyLevel(5)
                    // 设置cache中的数据在写入之后的存活时间为10秒
                    .expireAfterWrite(expireTime, TimeUnit.HOURS)
                    // 构建cache实例
                    .build();

    public LocalCacheConfig() {
    }

    public void putCache(String key, Object value) {
        cache.put(key, value);
    }

    public Object getCache(String key) {
        return cache.getIfPresent(key);
    }

    public void removeCache(String key) {
        cache.invalidate(key);
    }

}
