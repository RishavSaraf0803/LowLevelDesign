# Distributed Rate Limiter

## Problem Statement

Design a rate limiter that works in a distributed system with multiple application servers. The rate limiter should:

1. Limit the number of requests a user/client can make within a time window
2. Work correctly across multiple servers (distributed environment)
3. Handle high throughput (millions of requests per second)
4. Be accurate and avoid race conditions
5. Support different rate limiting strategies

---

## Requirements

### Functional Requirements
1. **Limit requests per user/API key** - Each user has their own rate limit
2. **Support multiple time windows** - per second, per minute, per hour, per day
3. **Return clear responses**:
   - Allow request if under limit
   - Reject request if limit exceeded
   - Provide metadata: remaining quota, reset time
4. **Support different strategies**:
   - Token Bucket
   - Sliding Window Log
   - Sliding Window Counter
   - Fixed Window Counter

### Non-Functional Requirements
1. **Low Latency** - Rate limiting check should be fast (< 10ms)
2. **High Availability** - System should be fault-tolerant
3. **Scalability** - Handle millions of requests per second
4. **Consistency** - Rate limits should be enforced accurately across all servers
5. **Atomic Operations** - No race conditions in distributed environment

---

## Challenges in Distributed Systems

### 1. Race Conditions
- Multiple servers checking and incrementing counters simultaneously
- Without synchronization, can exceed rate limits

### 2. Consistency
- How to maintain accurate counts across multiple servers?
- Network delays and clock skew

### 3. Single Point of Failure
- Centralized rate limiter becomes bottleneck
- Need distributed storage (Redis cluster)

### 4. Clock Synchronization
- Different servers may have slightly different times
- Affects window-based rate limiting

---

## Solution Approaches

## Approach 1: Token Bucket (with Redis)

### Description
- Each user has a bucket with tokens
- Tokens are added at a fixed rate (refill rate)
- Each request consumes a token
- If no tokens available, request is rejected

### Advantages
- Allows burst traffic (up to bucket capacity)
- Smooth traffic over time
- Memory efficient

### Disadvantages
- Complex refill logic
- Harder to understand

### Redis Implementation
```
Key: user:{userId}:bucket
Value: {
  tokens: current_tokens,
  last_refill_time: timestamp
}
```

### Algorithm
```
1. Get bucket data from Redis
2. Calculate time elapsed since last refill
3. Refill tokens = elapsed_time * refill_rate
4. tokens = min(tokens + refill_tokens, capacity)
5. If tokens >= 1:
     - Decrement token
     - Update Redis
     - Allow request
6. Else:
     - Reject request
```

---

## Approach 2: Fixed Window Counter

### Description
- Divide time into fixed windows (e.g., every minute starts at 0 seconds)
- Count requests in current window
- Reset counter when window changes

### Advantages
- Simple to implement
- Low memory usage

### Disadvantages
- **Boundary problem**: Can allow 2x requests around window boundaries
  - Example: 100 limit per minute
  - User makes 100 requests at 10:00:59
  - User makes 100 requests at 10:01:00
  - Total: 200 requests in 2 seconds!

### Redis Implementation
```
Key: user:{userId}:window:{timestamp}
Value: request_count
TTL: window_size
```

### Algorithm (Redis Lua Script)
```lua
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

local window_key = key .. ":" .. math.floor(current_time / window)
local current_count = redis.call('GET', window_key) or 0

if tonumber(current_count) < limit then
    redis.call('INCR', window_key)
    redis.call('EXPIRE', window_key, window)
    return {1, limit - current_count - 1}  -- allowed, remaining
else
    return {0, 0}  -- rejected, remaining
end
```

---

## Approach 3: Sliding Window Log

### Description
- Store timestamp of each request in a sorted set
- Remove old requests outside time window
- Count requests in current window

### Advantages
- Most accurate
- No boundary problem
- Can provide exact request history

### Disadvantages
- High memory usage (stores every request timestamp)
- Expensive for high traffic users

### Redis Implementation
```
Key: user:{userId}:requests
Value: Sorted Set {timestamp1, timestamp2, ...}
Score: timestamp
```

### Algorithm (Redis Lua Script)
```lua
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

local window_start = current_time - window

-- Remove old entries
redis.call('ZREMRANGEBYSCORE', key, 0, window_start)

-- Count requests in window
local request_count = redis.call('ZCARD', key)

if request_count < limit then
    redis.call('ZADD', key, current_time, current_time)
    redis.call('EXPIRE', key, window)
    return {1, limit - request_count - 1}
else
    return {0, 0}
end
```

---

## Approach 4: Sliding Window Counter ⭐ (Recommended)

### Description
- Hybrid approach combining fixed window and sliding window
- Uses two fixed windows: current and previous
- Estimates requests in sliding window using weighted average

### Advantages
- More accurate than fixed window
- Lower memory than sliding log
- Good balance of accuracy and performance

### Disadvantages
- Slightly less accurate than sliding log
- More complex than fixed window

### Formula
```
sliding_window_count =
    previous_window_count * overlap_percentage +
    current_window_count
```

### Example
```
Window: 1 minute
Current time: 10:00:30 (30 seconds into current minute)
Previous window (09:59-10:00): 80 requests
Current window (10:00-10:01): 20 requests

Overlap percentage = (60 - 30) / 60 = 0.5
Sliding count = 80 * 0.5 + 20 = 60 requests
```

### Redis Implementation
```
Key pattern:
  user:{userId}:window:{minute_timestamp}
```

### Algorithm (Redis Lua Script)
```lua
local key_prefix = KEYS[1]
local limit = tonumber(ARGV[1])
local window_size = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

local current_window = math.floor(current_time / window_size)
local previous_window = current_window - 1

local current_key = key_prefix .. ":" .. current_window
local previous_key = key_prefix .. ":" .. previous_window

local current_count = tonumber(redis.call('GET', current_key) or 0)
local previous_count = tonumber(redis.call('GET', previous_key) or 0)

-- Calculate sliding window count
local elapsed_time_in_window = current_time % window_size
local overlap_percentage = (window_size - elapsed_time_in_window) / window_size
local weighted_count = math.floor(previous_count * overlap_percentage + current_count)

if weighted_count < limit then
    redis.call('INCR', current_key)
    redis.call('EXPIRE', current_key, window_size * 2)
    return {1, limit - weighted_count - 1}
else
    return {0, 0}
end
```

---

## Implementation Design

### Class Structure

```
RateLimiter (Interface)
├── isAllowed(String userId, String resource): RateLimitResult

RateLimitResult
├── boolean allowed
├── long remaining
├── long resetTimeMs

RateLimiterStrategy (Interface)
├── checkLimit(String key, long limit, long windowSizeMs): RateLimitResult

Implementations:
├── TokenBucketRateLimiter
├── FixedWindowRateLimiter
├── SlidingWindowLogRateLimiter
└── SlidingWindowCounterRateLimiter (Recommended)

RedisClient (Wrapper)
├── executeLuaScript(String script, List<String> keys, List<String> args)
├── get(String key)
├── set(String key, String value, long ttlSeconds)
├── zadd(String key, double score, String member)
└── ...

RateLimiterConfig
├── long requestsPerWindow
├── long windowSizeMs
├── RateLimiterStrategy strategy
```

---

## Redis Lua Scripts (Atomic Operations)

### Why Lua Scripts?
1. **Atomicity** - Entire script executes as one atomic operation
2. **Reduce Network Overhead** - Multiple Redis commands in one round trip
3. **Avoid Race Conditions** - No interleaving with other clients

### Best Practices
1. Keep scripts short and focused
2. Avoid loops and complex logic
3. Use consistent key naming conventions
4. Set appropriate TTLs to avoid memory leaks

---

## Advanced Topics

### 1. Distributed Redis Setup
- **Redis Cluster**: Sharded data across multiple nodes
- **Redis Sentinel**: High availability with automatic failover
- **Replication**: Master-slave setup for read scalability

### 2. Handling Redis Failures
- **Fail Open**: Allow requests if Redis is down (risk of exceeding limits)
- **Fail Closed**: Reject all requests if Redis is down (availability impact)
- **Circuit Breaker**: Detect failures and fallback to local rate limiting

### 3. Multi-Tier Rate Limiting
```
User Rate Limit:  1000 requests/hour
IP Rate Limit:    5000 requests/hour
Global Limit:     1M requests/hour
```

### 4. Rate Limit Headers (HTTP)
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 500
X-RateLimit-Reset: 1699564800
Retry-After: 60
```

### 5. Distributed Counters with Eventual Consistency
- Use multiple Redis shards
- Aggregate counts periodically
- Trade accuracy for performance

---

## Performance Optimization

### 1. Connection Pooling
- Reuse Redis connections
- Configure pool size based on throughput

### 2. Pipeline Requests
- Batch multiple Redis commands
- Reduce network round trips

### 3. Local Caching
- Cache rate limit checks for short duration
- Reduce Redis load for high-traffic users
- Risk: Slight over-limit possibility

### 4. Asynchronous Processing
- Non-blocking rate limit checks
- Return cached result immediately
- Update Redis asynchronously

---

## Testing Strategy

### Unit Tests
1. Test each strategy independently
2. Mock Redis client
3. Verify correct counts and limits

### Integration Tests
1. Test with real Redis instance
2. Verify Lua script execution
3. Test TTL and expiration

### Load Tests
1. Simulate high concurrent requests
2. Measure latency (p50, p99, p999)
3. Test Redis connection pool under load

### Distributed Tests
1. Multiple application instances
2. Verify no race conditions
3. Test with network delays

### Edge Cases
1. Clock skew between servers
2. Redis failover scenarios
3. Burst traffic patterns
4. Boundary conditions (window transitions)

---

## Real-World Examples

### 1. GitHub API Rate Limiting
```
Rate Limit: 5000 requests/hour (authenticated)
Strategy: Sliding Window Counter
Headers: X-RateLimit-* headers
```

### 2. Twitter API
```
Rate Limit: 15 requests/15 minutes
Strategy: Token Bucket (allows bursts)
Response: 429 Too Many Requests
```

### 3. AWS API Gateway
```
Throttle: Requests per second
Burst: Up to 5000 requests
Strategy: Token Bucket
```

---

## Key Takeaways

1. **Use Lua Scripts** for atomic Redis operations
2. **Sliding Window Counter** offers best accuracy/performance tradeoff
3. **Set TTLs** on all Redis keys to prevent memory leaks
4. **Handle Redis failures** gracefully (fail open/closed strategy)
5. **Monitor and Alert** on rate limit violations
6. **Test thoroughly** under concurrent load

---

## Implementation Checklist

- [ ] Define RateLimiter interface
- [ ] Implement RateLimitResult class
- [ ] Create RedisClient wrapper with connection pooling
- [ ] Implement Sliding Window Counter with Lua script
- [ ] Add configuration for limits and windows
- [ ] Implement rate limit headers in API responses
- [ ] Add metrics and monitoring
- [ ] Handle Redis connection failures
- [ ] Write unit tests for strategy
- [ ] Write integration tests with Redis
- [ ] Perform load testing
- [ ] Document API usage and configuration

---

## References

- Redis EVALSHA for Lua scripts
- Token Bucket Algorithm
- Leaky Bucket Algorithm
- Sliding Window Rate Limiting
- Distributed Systems Consistency Models
