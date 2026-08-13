package main.store.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimiter {
    private final StringRedisTemplate redisTemplate;

    public boolean allowRequest(String clientId, int limit, Duration windowSize){
        long windowIndex = System.currentTimeMillis() / windowSize.toMillis();
        String key = String.format("rate: %s:%s", clientId, windowIndex);

        Long countHits = redisTemplate
                .opsForValue()
                .increment(key);
        if (countHits != null && countHits == 1L){
            redisTemplate.expire(key, windowSize);
        }
        return countHits != null && countHits <= limit;
    }
}
