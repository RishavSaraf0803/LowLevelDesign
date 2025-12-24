# Distributed Rate Limiter - Implementation Guide

## Code Structure Overview

```
DistributedRateLimiter/
├── README.md                           (Concepts and Theory)
├── IMPLEMENTATION_GUIDE.md             (This file)
├── REDIS_LUA_SCRIPTS.md                (Lua Scripts Collection)
├── data/
│   ├── RateLimitResult.java
│   ├── RateLimiterConfig.java
│   └── RateLimitStrategy.java (enum)
├── core/
│   ├── RateLimiter.java (interface)
│   └── DistributedRateLimiter.java (main implementation)
├── strategy/
│   ├── RateLimiterStrategy.java (interface)
│   ├── TokenBucketStrategy.java
│   ├── FixedWindowStrategy.java
│   ├── SlidingWindowLogStrategy.java
│   └── SlidingWindowCounterStrategy.java ⭐
├── redis/
│   ├── RedisClient.java (interface)
│   ├── RedisClientImpl.java
│   └── RedisConnectionPool.java
└── tester/
    ├── BasicTest.java
    ├── ConcurrencyTest.java
    └── LoadTest.java
```

---

## Step-by-Step Implementation

### Step 1: Define Data Models

#### RateLimitResult.java
```java
package Concurrency.DistributedRateLimiter.data;

public class RateLimitResult {
    private final boolean allowed;
    private final long remaining;      // Remaining requests in window
    private final long resetTimeMs;    // When the window resets
    private final long retryAfterMs;   // Milliseconds until next allowed request

    public RateLimitResult(boolean allowed, long remaining, long resetTimeMs) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.resetTimeMs = resetTimeMs;
        this.retryAfterMs = allowed ? 0 : resetTimeMs - System.currentTimeMillis();
    }

    // Getters
    public boolean isAllowed() { return allowed; }
    public long getRemaining() { return remaining; }
    public long getResetTimeMs() { return resetTimeMs; }
    public long getRetryAfterMs() { return retryAfterMs; }

    @Override
    public String toString() {
        return String.format("RateLimitResult{allowed=%s, remaining=%d, resetTime=%d, retryAfter=%dms}",
                allowed, remaining, resetTimeMs, retryAfterMs);
    }
}
```

#### RateLimiterConfig.java
```java
package Concurrency.DistributedRateLimiter.data;

public class RateLimiterConfig {
    private final long requestsPerWindow;   // e.g., 100
    private final long windowSizeMs;        // e.g., 60000 (1 minute)
    private final RateLimitStrategyType strategyType;

    public RateLimiterConfig(long requestsPerWindow, long windowSizeMs,
                             RateLimitStrategyType strategyType) {
        this.requestsPerWindow = requestsPerWindow;
        this.windowSizeMs = windowSizeMs;
        this.strategyType = strategyType;
    }

    // Factory methods for common configs
    public static RateLimiterConfig perSecond(long requests) {
        return new RateLimiterConfig(requests, 1000,
                RateLimitStrategyType.SLIDING_WINDOW_COUNTER);
    }

    public static RateLimiterConfig perMinute(long requests) {
        return new RateLimiterConfig(requests, 60_000,
                RateLimitStrategyType.SLIDING_WINDOW_COUNTER);
    }

    public static RateLimiterConfig perHour(long requests) {
        return new RateLimiterConfig(requests, 3_600_000,
                RateLimitStrategyType.SLIDING_WINDOW_COUNTER);
    }

    // Getters
    public long getRequestsPerWindow() { return requestsPerWindow; }
    public long getWindowSizeMs() { return windowSizeMs; }
    public RateLimitStrategyType getStrategyType() { return strategyType; }
}
```

#### RateLimitStrategyType.java
```java
package Concurrency.DistributedRateLimiter.data;

public enum RateLimitStrategyType {
    TOKEN_BUCKET,
    FIXED_WINDOW,
    SLIDING_WINDOW_LOG,
    SLIDING_WINDOW_COUNTER
}
```

---

### Step 2: Define Core Interfaces

#### RateLimiter.java
```java
package Concurrency.DistributedRateLimiter.core;

import Concurrency.DistributedRateLimiter.data.RateLimitResult;

public interface RateLimiter {
    /**
     * Check if request is allowed for the given user/resource
     * @param userId User identifier
     * @param resource Resource being accessed (optional, can be null)
     * @return RateLimitResult with allow/deny and metadata
     */
    RateLimitResult isAllowed(String userId, String resource);

    /**
     * Check if request is allowed for user
     */
    default RateLimitResult isAllowed(String userId) {
        return isAllowed(userId, null);
    }

    /**
     * Reset rate limit for a user (admin operation)
     */
    void reset(String userId);

    /**
     * Get current usage without incrementing counter
     */
    long getCurrentUsage(String userId);
}
```

#### RateLimiterStrategy.java (Strategy Interface)
```java
package Concurrency.DistributedRateLimiter.strategy;

import Concurrency.DistributedRateLimiter.data.RateLimitResult;
import Concurrency.DistributedRateLimiter.data.RateLimiterConfig;

public interface RateLimiterStrategy {
    /**
     * Check rate limit for given key
     * @param key Redis key (e.g., "user:123:api")
     * @param config Rate limiter configuration
     * @param currentTimeMs Current timestamp in milliseconds
     * @return RateLimitResult
     */
    RateLimitResult checkLimit(String key, RateLimiterConfig config, long currentTimeMs);

    /**
     * Reset counter for given key
     */
    void reset(String key);

    /**
     * Get current count without incrementing
     */
    long getCount(String key);
}
```

---

### Step 3: Redis Client Interface

#### RedisClient.java
```java
package Concurrency.DistributedRateLimiter.redis;

import java.util.List;

public interface RedisClient {
    /**
     * Execute Lua script atomically
     * @param script Lua script source
     * @param keys Redis keys (KEYS in Lua)
     * @param args Arguments (ARGV in Lua)
     * @return Script result
     */
    Object evalScript(String script, List<String> keys, List<String> args);

    /**
     * Get value
     */
    String get(String key);

    /**
     * Set value with TTL
     */
    void set(String key, String value, long ttlSeconds);

    /**
     * Increment counter
     */
    long incr(String key);

    /**
     * Delete key
     */
    void del(String key);

    /**
     * Add to sorted set
     */
    void zadd(String key, double score, String member);

    /**
     * Remove from sorted set by score range
     */
    void zremrangebyscore(String key, double min, double max);

    /**
     * Get sorted set cardinality
     */
    long zcard(String key);

    /**
     * Set expiration
     */
    void expire(String key, long seconds);

    /**
     * Close connection
     */
    void close();
}
```

---

### Step 4: Implement Sliding Window Counter Strategy (Recommended)

#### SlidingWindowCounterStrategy.java
```java
package Concurrency.DistributedRateLimiter.strategy;

import Concurrency.DistributedRateLimiter.data.RateLimitResult;
import Concurrency.DistributedRateLimiter.data.RateLimiterConfig;
import Concurrency.DistributedRateLimiter.redis.RedisClient;

import java.util.Arrays;
import java.util.List;

public class SlidingWindowCounterStrategy implements RateLimiterStrategy {
    private final RedisClient redisClient;

    // Lua script for atomic sliding window check
    private static final String LUA_SCRIPT =
        "local key_prefix = KEYS[1]\n" +
        "local limit = tonumber(ARGV[1])\n" +
        "local window_size = tonumber(ARGV[2])\n" +
        "local current_time = tonumber(ARGV[3])\n" +
        "\n" +
        "local current_window = math.floor(current_time / window_size)\n" +
        "local previous_window = current_window - 1\n" +
        "\n" +
        "local current_key = key_prefix .. ':' .. current_window\n" +
        "local previous_key = key_prefix .. ':' .. previous_window\n" +
        "\n" +
        "local current_count = tonumber(redis.call('GET', current_key) or '0')\n" +
        "local previous_count = tonumber(redis.call('GET', previous_key) or '0')\n" +
        "\n" +
        "-- Calculate sliding window count\n" +
        "local elapsed_time_in_window = current_time % window_size\n" +
        "local overlap_percentage = (window_size - elapsed_time_in_window) / window_size\n" +
        "local weighted_count = math.floor(previous_count * overlap_percentage + current_count)\n" +
        "\n" +
        "if weighted_count < limit then\n" +
        "    redis.call('INCR', current_key)\n" +
        "    redis.call('EXPIRE', current_key, window_size * 2)\n" +
        "    local remaining = limit - weighted_count - 1\n" +
        "    local reset_time = (current_window + 1) * window_size\n" +
        "    return {1, remaining, reset_time}\n" +
        "else\n" +
        "    local reset_time = (current_window + 1) * window_size\n" +
        "    return {0, 0, reset_time}\n" +
        "end";

    public SlidingWindowCounterStrategy(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public RateLimitResult checkLimit(String key, RateLimiterConfig config, long currentTimeMs) {
        List<String> keys = Arrays.asList(key);
        List<String> args = Arrays.asList(
                String.valueOf(config.getRequestsPerWindow()),
                String.valueOf(config.getWindowSizeMs()),
                String.valueOf(currentTimeMs)
        );

        Object result = redisClient.evalScript(LUA_SCRIPT, keys, args);

        // Parse result [allowed, remaining, resetTime]
        @SuppressWarnings("unchecked")
        List<Long> resultList = (List<Long>) result;

        boolean allowed = resultList.get(0) == 1;
        long remaining = resultList.get(1);
        long resetTimeMs = resultList.get(2);

        return new RateLimitResult(allowed, remaining, resetTimeMs);
    }

    @Override
    public void reset(String key) {
        // Delete all window keys for this user
        // In production, use pattern matching or track active windows
        redisClient.del(key);
    }

    @Override
    public long getCount(String key) {
        // Approximate count from current window
        long currentTimeMs = System.currentTimeMillis();
        long windowSize = 60000; // This should come from config
        long currentWindow = currentTimeMs / windowSize;
        String currentKey = key + ":" + currentWindow;

        String count = redisClient.get(currentKey);
        return count != null ? Long.parseLong(count) : 0;
    }
}
```

---

### Step 5: Main RateLimiter Implementation

#### DistributedRateLimiter.java
```java
package Concurrency.DistributedRateLimiter.core;

import Concurrency.DistributedRateLimiter.data.*;
import Concurrency.DistributedRateLimiter.strategy.*;
import Concurrency.DistributedRateLimiter.redis.RedisClient;

public class DistributedRateLimiter implements RateLimiter {
    private final RateLimiterConfig config;
    private final RateLimiterStrategy strategy;
    private final String keyPrefix;

    public DistributedRateLimiter(RateLimiterConfig config, RedisClient redisClient, String keyPrefix) {
        this.config = config;
        this.keyPrefix = keyPrefix;
        this.strategy = createStrategy(config.getStrategyType(), redisClient);
    }

    private RateLimiterStrategy createStrategy(RateLimitStrategyType type, RedisClient redisClient) {
        switch (type) {
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterStrategy(redisClient);
            case FIXED_WINDOW:
                return new FixedWindowStrategy(redisClient);
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogStrategy(redisClient);
            case TOKEN_BUCKET:
                return new TokenBucketStrategy(redisClient);
            default:
                throw new IllegalArgumentException("Unknown strategy: " + type);
        }
    }

    @Override
    public RateLimitResult isAllowed(String userId, String resource) {
        String key = buildKey(userId, resource);
        long currentTimeMs = System.currentTimeMillis();
        return strategy.checkLimit(key, config, currentTimeMs);
    }

    @Override
    public void reset(String userId) {
        String key = buildKey(userId, null);
        strategy.reset(key);
    }

    @Override
    public long getCurrentUsage(String userId) {
        String key = buildKey(userId, null);
        return strategy.getCount(key);
    }

    private String buildKey(String userId, String resource) {
        if (resource != null) {
            return String.format("%s:user:%s:resource:%s", keyPrefix, userId, resource);
        }
        return String.format("%s:user:%s", keyPrefix, userId);
    }
}
```

---

### Step 6: Testing

#### BasicTest.java
```java
package Concurrency.DistributedRateLimiter.tester;

public class BasicTest {
    public static void main(String[] args) {
        // 1. Create Redis client (mock or real)
        // RedisClient redisClient = new MockRedisClient();

        // 2. Create config
        // RateLimiterConfig config = RateLimiterConfig.perMinute(10);

        // 3. Create rate limiter
        // RateLimiter rateLimiter = new DistributedRateLimiter(config, redisClient, "api");

        // 4. Test basic flow
        // for (int i = 0; i < 12; i++) {
        //     RateLimitResult result = rateLimiter.isAllowed("user123");
        //     System.out.println("Request " + (i+1) + ": " + result);
        // }

        System.out.println("Implement tests here!");
    }
}
```

---

## Implementation Order

### Phase 1: Core (Day 1)
1. ✅ Create data models (RateLimitResult, Config, Enum)
2. ✅ Define interfaces (RateLimiter, RateLimiterStrategy, RedisClient)
3. ✅ Mock RedisClient implementation for testing

### Phase 2: Strategy (Day 2)
4. ✅ Implement SlidingWindowCounterStrategy
5. ✅ Write and test Lua script separately
6. ✅ Implement DistributedRateLimiter main class

### Phase 3: Testing (Day 3)
7. ✅ Unit tests with mock Redis
8. ✅ Integration tests with real Redis
9. ✅ Concurrent request tests

### Phase 4: Advanced (Day 4+)
10. ✅ Implement other strategies (Token Bucket, Fixed Window, etc.)
11. ✅ Add Redis connection pooling
12. ✅ Add monitoring and metrics
13. ✅ Load testing and performance tuning

---

## Testing Checklist

### Unit Tests
- [ ] Test RateLimitResult creation and getters
- [ ] Test RateLimiterConfig factory methods
- [ ] Test key building logic
- [ ] Test strategy selection

### Integration Tests (with Redis)
- [ ] Test basic allow/deny flow
- [ ] Test counter increments correctly
- [ ] Test window transitions
- [ ] Test TTL expiration
- [ ] Test reset functionality

### Concurrency Tests
- [ ] Multiple threads, same user
- [ ] Multiple threads, different users
- [ ] Race condition detection
- [ ] Verify no over-limit

### Edge Cases
- [ ] Exactly at limit
- [ ] Redis connection failure
- [ ] Invalid user IDs
- [ ] Clock skew scenarios
- [ ] Boundary conditions

---

## Common Pitfalls

1. **Not Using Lua Scripts** ❌
   - Multiple Redis commands = race conditions
   - Always use atomic Lua scripts

2. **Forgetting TTL** ❌
   - Keys will accumulate forever
   - Always set appropriate expiration

3. **Time Synchronization** ❌
   - Use consistent time source
   - Consider clock skew in distributed systems

4. **Redis Connection Leaks** ❌
   - Always use connection pooling
   - Close connections properly

5. **Not Handling Redis Failures** ❌
   - Decide: fail open or fail closed?
   - Implement circuit breaker pattern

---

## Performance Tips

1. **Use Pipelining** - Batch multiple Redis commands
2. **Connection Pool** - Reuse connections
3. **Local Cache** - Cache results briefly (with risk)
4. **Asynchronous** - Non-blocking rate limit checks
5. **Sharding** - Distribute load across Redis instances

---

## Next Steps

1. Implement MockRedisClient for testing without Redis
2. Write comprehensive unit tests
3. Set up local Redis instance
4. Implement integration tests
5. Add other rate limiting strategies
6. Performance benchmark
7. Add monitoring/alerting

---

## Resources

- Redis Lua Scripting: https://redis.io/commands/eval/
- Rate Limiting Algorithms: See README.md
- Java Redis Client: Jedis or Lettuce
- Connection Pooling: Apache Commons Pool
