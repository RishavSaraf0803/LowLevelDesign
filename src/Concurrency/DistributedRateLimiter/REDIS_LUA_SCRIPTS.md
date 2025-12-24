# Redis Lua Scripts for Rate Limiting

This document contains production-ready Lua scripts for different rate limiting strategies.

---

## Why Lua Scripts?

### Atomicity
- All Redis commands in a Lua script execute atomically
- No interleaving with other clients
- Prevents race conditions

### Performance
- Single network round trip
- All operations executed on Redis server
- Reduced latency

### Consistency
- Guarantees correct behavior in distributed systems
- Multiple app servers can safely use same rate limiter

---

## 1. Sliding Window Counter (Recommended) ⭐

### Algorithm
Uses weighted average of previous and current window to estimate sliding window count.

### Lua Script
```lua
-- KEYS[1]: key prefix (e.g., "ratelimit:user:123")
-- ARGV[1]: limit (max requests)
-- ARGV[2]: window size in milliseconds
-- ARGV[3]: current time in milliseconds
-- Returns: {allowed (0/1), remaining, reset_time}

local key_prefix = KEYS[1]
local limit = tonumber(ARGV[1])
local window_size = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

-- Calculate current and previous window
local current_window = math.floor(current_time / window_size)
local previous_window = current_window - 1

-- Build keys
local current_key = key_prefix .. ':' .. current_window
local previous_key = key_prefix .. ':' .. previous_window

-- Get counts
local current_count = tonumber(redis.call('GET', current_key) or '0')
local previous_count = tonumber(redis.call('GET', previous_key) or '0')

-- Calculate position in current window
local elapsed_time_in_window = current_time % window_size
local overlap_percentage = (window_size - elapsed_time_in_window) / window_size

-- Weighted count: previous_count * overlap + current_count
local weighted_count = math.floor(previous_count * overlap_percentage + current_count)

-- Check limit
if weighted_count < limit then
    -- Allow request
    redis.call('INCR', current_key)
    redis.call('EXPIRE', current_key, window_size * 2 / 1000) -- TTL in seconds

    local remaining = limit - weighted_count - 1
    local reset_time = (current_window + 1) * window_size

    return {1, remaining, reset_time}
else
    -- Deny request
    local reset_time = (current_window + 1) * window_size
    return {0, 0, reset_time}
end
```

### Usage Example (Java)
```java
String script = "... lua script above ...";
List<String> keys = Arrays.asList("ratelimit:user:123");
List<String> args = Arrays.asList("100", "60000", String.valueOf(System.currentTimeMillis()));
Object result = redisClient.eval(script, keys, args);
```

---

## 2. Fixed Window Counter

### Algorithm
Simple counter per time window. Resets at window boundaries.

### Lua Script
```lua
-- KEYS[1]: key prefix
-- ARGV[1]: limit
-- ARGV[2]: window size in milliseconds
-- ARGV[3]: current time in milliseconds
-- Returns: {allowed (0/1), remaining, reset_time}

local key_prefix = KEYS[1]
local limit = tonumber(ARGV[1])
local window_size = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

-- Calculate current window
local current_window = math.floor(current_time / window_size)
local window_key = key_prefix .. ':' .. current_window

-- Get current count
local current_count = tonumber(redis.call('GET', window_key) or '0')

-- Check limit
if current_count < limit then
    -- Allow request
    redis.call('INCR', window_key)
    redis.call('EXPIRE', window_key, window_size * 2 / 1000)

    local remaining = limit - current_count - 1
    local reset_time = (current_window + 1) * window_size

    return {1, remaining, reset_time}
else
    -- Deny request
    local reset_time = (current_window + 1) * window_size
    return {0, 0, reset_time}
end
```

### Pros & Cons
✅ Simple and fast
✅ Low memory usage
❌ Boundary problem (can allow 2x requests at window edges)

---

## 3. Sliding Window Log

### Algorithm
Store timestamp of each request in sorted set. Most accurate but memory-intensive.

### Lua Script
```lua
-- KEYS[1]: key (sorted set)
-- ARGV[1]: limit
-- ARGV[2]: window size in milliseconds
-- ARGV[3]: current time in milliseconds
-- Returns: {allowed (0/1), remaining, reset_time}

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window_size = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

-- Calculate window start time
local window_start = current_time - window_size

-- Remove old entries (outside current window)
redis.call('ZREMRANGEBYSCORE', key, 0, window_start)

-- Count requests in current window
local request_count = redis.call('ZCARD', key)

-- Check limit
if request_count < limit then
    -- Allow request: add current timestamp
    redis.call('ZADD', key, current_time, current_time .. ':' .. math.random())
    redis.call('EXPIRE', key, math.ceil(window_size / 1000) * 2)

    local remaining = limit - request_count - 1
    local reset_time = current_time + window_size

    return {1, remaining, reset_time}
else
    -- Deny request
    -- Get oldest request time to calculate reset
    local oldest = redis.call('ZRANGE', key, 0, 0, 'WITHSCORES')
    local reset_time = tonumber(oldest[2]) + window_size

    return {0, 0, reset_time}
end
```

### Pros & Cons
✅ Most accurate
✅ No boundary problem
❌ High memory usage (stores every request)
❌ Slower for high-traffic users

---

## 4. Token Bucket

### Algorithm
Bucket holds tokens. Tokens refill at constant rate. Each request consumes a token.

### Lua Script
```lua
-- KEYS[1]: key (hash with tokens and last_refill_time)
-- ARGV[1]: capacity (bucket size)
-- ARGV[2]: refill_rate (tokens per second)
-- ARGV[3]: current time in milliseconds
-- ARGV[4]: tokens_per_request (default 1)
-- Returns: {allowed (0/1), remaining_tokens, retry_after_ms}

local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local refill_rate = tonumber(ARGV[2])  -- tokens per second
local current_time = tonumber(ARGV[3])
local tokens_per_request = tonumber(ARGV[4] or '1')

-- Get current state
local bucket = redis.call('HMGET', key, 'tokens', 'last_refill_time')
local tokens = tonumber(bucket[1])
local last_refill_time = tonumber(bucket[2])

-- Initialize if first request
if tokens == nil or last_refill_time == nil then
    tokens = capacity
    last_refill_time = current_time
end

-- Calculate refill
local elapsed_seconds = (current_time - last_refill_time) / 1000
local refill_tokens = elapsed_seconds * refill_rate
tokens = math.min(tokens + refill_tokens, capacity)
last_refill_time = current_time

-- Check if enough tokens
if tokens >= tokens_per_request then
    -- Allow request
    tokens = tokens - tokens_per_request
    redis.call('HMSET', key, 'tokens', tokens, 'last_refill_time', last_refill_time)
    redis.call('EXPIRE', key, math.ceil(capacity / refill_rate) * 2)

    return {1, math.floor(tokens), 0}
else
    -- Deny request
    redis.call('HMSET', key, 'tokens', tokens, 'last_refill_time', last_refill_time)
    redis.call('EXPIRE', key, math.ceil(capacity / refill_rate) * 2)

    -- Calculate retry after
    local tokens_needed = tokens_per_request - tokens
    local retry_after_ms = math.ceil((tokens_needed / refill_rate) * 1000)

    return {0, math.floor(tokens), retry_after_ms}
end
```

### Pros & Cons
✅ Allows burst traffic
✅ Smooth rate over time
✅ Memory efficient
❌ Complex to understand
❌ Harder to reason about limits

---

## 5. Leaky Bucket

### Algorithm
Requests enter bucket, leak out at constant rate. Bucket has max capacity.

### Lua Script
```lua
-- KEYS[1]: queue key (list)
-- ARGV[1]: capacity (bucket size)
-- ARGV[2]: leak_rate (requests per second)
-- ARGV[3]: current time in milliseconds
-- Returns: {allowed (0/1), queue_length, retry_after_ms}

local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local leak_rate = tonumber(ARGV[2])
local current_time = tonumber(ARGV[3])

-- Get metadata
local meta_key = key .. ':meta'
local meta = redis.call('HMGET', meta_key, 'last_leak_time', 'queue_size')
local last_leak_time = tonumber(meta[1]) or current_time
local queue_size = tonumber(meta[2]) or 0

-- Leak requests
local elapsed_seconds = (current_time - last_leak_time) / 1000
local leaked_requests = math.floor(elapsed_seconds * leak_rate)

queue_size = math.max(0, queue_size - leaked_requests)
last_leak_time = current_time

-- Check capacity
if queue_size < capacity then
    -- Allow request
    queue_size = queue_size + 1
    redis.call('HMSET', meta_key, 'last_leak_time', last_leak_time, 'queue_size', queue_size)
    redis.call('EXPIRE', meta_key, math.ceil(capacity / leak_rate) * 2)

    return {1, queue_size, 0}
else
    -- Deny request
    redis.call('HMSET', meta_key, 'last_leak_time', last_leak_time, 'queue_size', queue_size)
    redis.call('EXPIRE', meta_key, math.ceil(capacity / leak_rate) * 2)

    -- Calculate retry after
    local retry_after_ms = math.ceil((1 / leak_rate) * 1000)

    return {0, queue_size, retry_after_ms}
end
```

### Pros & Cons
✅ Smooths traffic
✅ Predictable output rate
❌ Can delay requests
❌ Harder to implement distributed version

---

## 6. Multi-Tier Rate Limiting

### Algorithm
Check multiple rate limits (per user, per IP, global) in one atomic operation.

### Lua Script
```lua
-- KEYS[1]: user key prefix
-- KEYS[2]: ip key prefix
-- KEYS[3]: global key prefix
-- ARGV[1]: user_limit
-- ARGV[2]: ip_limit
-- ARGV[3]: global_limit
-- ARGV[4]: window_size
-- ARGV[5]: current_time
-- Returns: {allowed, limited_by, remaining}

local function check_limit(key_prefix, limit, window_size, current_time)
    local current_window = math.floor(current_time / window_size)
    local key = key_prefix .. ':' .. current_window
    local count = tonumber(redis.call('GET', key) or '0')

    if count >= limit then
        return false, 0
    end

    return true, limit - count - 1
end

local user_key = KEYS[1]
local ip_key = KEYS[2]
local global_key = KEYS[3]

local user_limit = tonumber(ARGV[1])
local ip_limit = tonumber(ARGV[2])
local global_limit = tonumber(ARGV[3])
local window_size = tonumber(ARGV[4])
local current_time = tonumber(ARGV[5])

-- Check all limits
local user_ok, user_remaining = check_limit(user_key, user_limit, window_size, current_time)
if not user_ok then
    return {0, 'user', user_remaining}
end

local ip_ok, ip_remaining = check_limit(ip_key, ip_limit, window_size, current_time)
if not ip_ok then
    return {0, 'ip', ip_remaining}
end

local global_ok, global_remaining = check_limit(global_key, global_limit, window_size, current_time)
if not global_ok then
    return {0, 'global', global_remaining}
end

-- All checks passed, increment all counters
local current_window = math.floor(current_time / window_size)
local ttl = window_size * 2 / 1000

for i, key_prefix in ipairs({user_key, ip_key, global_key}) do
    local key = key_prefix .. ':' .. current_window
    redis.call('INCR', key)
    redis.call('EXPIRE', key, ttl)
end

local min_remaining = math.min(user_remaining, ip_remaining, global_remaining)
return {1, 'none', min_remaining}
```

---

## Testing Lua Scripts

### Test in Redis CLI
```bash
redis-cli

# Load script
SET test_script "return {1, 100, 1234567890}"

# Execute script
EVAL "$(cat script.lua)" 1 "ratelimit:user:123" 100 60000 1234567890000
```

### Test Script Syntax
```bash
# Check for syntax errors
redis-cli --eval script.lua
```

### Debug Output
```lua
-- Add debug output (remove in production)
redis.log(redis.LOG_WARNING, "Current count: " .. tostring(current_count))
```

---

## Best Practices

### 1. Always Set TTL
```lua
redis.call('EXPIRE', key, ttl_seconds)
```
Prevents memory leaks from abandoned keys.

### 2. Use Consistent Time
```lua
-- Pass time as argument, don't use redis.call('TIME')
local current_time = tonumber(ARGV[3])
```
Ensures consistency across distributed systems.

### 3. Return Structured Data
```lua
-- Return array with consistent structure
return {allowed, remaining, reset_time}
```

### 4. Handle Nil Values
```lua
local count = tonumber(redis.call('GET', key) or '0')
```

### 5. Use Integer Division
```lua
local window = math.floor(current_time / window_size)
```

### 6. Keep Scripts Small
- Avoid loops when possible
- Break complex logic into multiple scripts
- Profile performance

---

## Common Issues

### Issue 1: Script Timeout
**Problem**: Script takes too long
**Solution**: Keep scripts under 5ms, avoid KEYS * patterns

### Issue 2: Memory Spike
**Problem**: Script uses too much memory
**Solution**: Use sorted sets carefully, clean up old data

### Issue 3: Race Conditions
**Problem**: Non-atomic operations
**Solution**: Use Lua scripts for all multi-step operations

### Issue 4: Key Leaks
**Problem**: Keys never expire
**Solution**: Always set TTL, use reasonable values

---

## Performance Tips

1. **Minimize Redis Calls**: Each call has overhead
2. **Use Local Variables**: Cache values in Lua
3. **Avoid String Concatenation in Loops**: Build strings efficiently
4. **Use EVALSHA**: Cache script on server, execute by hash
5. **Profile Scripts**: Use Redis SLOWLOG to identify issues

---

## Script Management

### Loading Scripts
```java
// Load script once, get SHA
String sha = redisClient.scriptLoad(luaScript);

// Execute by SHA (faster)
redisClient.evalsha(sha, keys, args);
```

### Version Control
```java
public class RateLimitScripts {
    public static final String SLIDING_WINDOW_V1 = "...";
    public static final String SLIDING_WINDOW_V2 = "...";

    // Use version that matches your Redis setup
    public static String getScript(String version) {
        // return appropriate script
    }
}
```

---

## Summary Table

| Strategy | Accuracy | Memory | Complexity | Bursts | Recommended |
|----------|----------|--------|------------|--------|-------------|
| Fixed Window | Low | Low | Low | Yes | ❌ No |
| Sliding Log | High | High | Medium | No | ⚠️ Sometimes |
| Sliding Counter | Medium | Low | Medium | Partial | ✅ Yes |
| Token Bucket | Medium | Low | High | Yes | ✅ Yes |
| Leaky Bucket | Medium | Low | High | No | ⚠️ Sometimes |

**Recommendation**: Start with **Sliding Window Counter** for best balance.

---

## Further Reading

- [Redis Lua Scripting](https://redis.io/docs/manual/programmability/eval-intro/)
- [EVALSHA Command](https://redis.io/commands/evalsha/)
- [Lua 5.1 Reference](https://www.lua.org/manual/5.1/)
- [Redis Atomicity Guarantees](https://redis.io/docs/manual/transactions/)
