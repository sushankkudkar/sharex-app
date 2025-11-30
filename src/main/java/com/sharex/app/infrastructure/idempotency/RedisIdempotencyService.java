package com.sharex.app.infrastructure.idempotency;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RedisIdempotencyService {

    private static final String BLOOM_NAME = "bf:processed_events";
    private static final String KEY_PREFIX = "idempotency:event:";

    private static final Duration TTL = Duration.ofDays(3); // Retain idempotency for 72h

    private final RBloomFilter<String> bloom;
    private final RedissonClient redisson;

    // Tune to expected load
    private final long expectedInsertions = 50_000_000L; // 50 million events
    private final double fpp = 0.001; // 0.1% false positive

    public RedisIdempotencyService(RedissonClient redisson) {
        this.redisson = redisson;
        this.bloom = redisson.getBloomFilter(BLOOM_NAME);

        if (!bloom.isExists()) {
            bloom.tryInit(expectedInsertions, fpp);
        }
    }

    private String key(String eventId) {
        return KEY_PREFIX + eventId;
    }

    /**
     * Fast check: Bloom filter → “might exist”
     * Then definitive check (Redis key)
     */
    public boolean isAlreadyProcessed(String eventId) {
        if (eventId == null) return false;

        boolean mightExist = bloom.contains(eventId);
        if (!mightExist) return false;

        RBucket<String> bucket = redisson.getBucket(key(eventId));
        return bucket.isExists();
    }

    /**
     * Mark event idempotently processed.
     * Uses definitive Redis key + Bloom filter.
     */
    public void markProcessed(String eventId) {
        if (eventId == null) return;

        // Definitive key (actual idempotency)
        RBucket<String> bucket = redisson.getBucket(key(eventId));
        bucket.trySet("1", TTL.toSeconds(), TimeUnit.SECONDS);

        // Add to Bloom (best-effort)
        bloom.add(eventId);
    }
}
