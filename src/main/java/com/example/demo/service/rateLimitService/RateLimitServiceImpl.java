package com.example.demo.service.rateLimitService;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    public boolean allowRequest(String key) {
        Bucket bucket = cache.computeIfAbsent(
                key,
                k -> createBucket());

        return bucket.tryConsume(1);
    }

    // Helper Method
    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofMinutes(15))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}