package com.example.demo.service.rateLimitService;

import org.springframework.stereotype.Service;

@Service
public interface RateLimitService {
    boolean allowRequest(String key);
}
