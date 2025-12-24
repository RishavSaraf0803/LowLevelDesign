# Distributed Rate Limiter - Quick Start Guide

## 📚 Documentation Overview

This folder contains comprehensive guides for implementing a distributed rate limiter:

1. **README.md** - Concepts, theory, and algorithm explanations
2. **IMPLEMENTATION_GUIDE.md** - Step-by-step code structure and implementation
3. **REDIS_LUA_SCRIPTS.md** - Production-ready Lua scripts for Redis
4. **QUICK_START.md** - This file (30-minute quick start)

---

## 🎯 What You'll Build

A distributed rate limiter that:
- ✅ Works across multiple application servers
- ✅ Uses Redis for shared state
- ✅ Supports multiple rate limiting strategies
- ✅ Provides O(1) performance
- ✅ Handles race conditions atomically

---

## 🚀 30-Minute Implementation Plan

### Step 1: Understand the Problem (5 min)
Read the **Problem Statement** and **Requirements** sections in README.md

**Key Question**: Why can't we use in-memory counters?
**Answer**: Multiple servers would have separate counters, allowing N×limit requests

---

### Step 2: Choose a Strategy (5 min)
Read **Solution Approaches** in README.md

**Recommended for Beginners**: Sliding Window Counter
- Best balance of accuracy and simplicity
- Memory efficient
- Handles burst traffic reasonably

---

### Step 3: Understand Redis Lua Scripts (10 min)
Read the **Sliding Window Counter** section in REDIS_LUA_SCRIPTS.md

**Key Concepts**:
1. Lua scripts execute atomically (no race conditions)
2. All operations happen in one network round trip
3. Script calculates weighted average of two windows

**Try this**: Visualize how sliding window works:
```
Time:    09:59  |  10:00  |  10:01
Window:  [Prev] |  [Curr] |  [Next]
Count:     80   |    20   |    ?

At 10:00:30 (halfway through current):
- Previous window (80) contributes 50% = 40
- Current window (20) contributes 100% = 20
- Total = 60 requests in sliding window
```

---

### Step 4: Design Class Structure (5 min)
Review IMPLEMENTATION_GUIDE.md structure

**Core Classes**:
```
RateLimitResult      - Response (allowed, remaining, resetTime)
RateLimiterConfig    - Configuration (limit, window, strategy)
RateLimiter          - Interface (isAllowed, reset)
RateLimiterStrategy  - Interface (checkLimit)
RedisClient          - Interface (evalScript, get, set, etc.)
```

---

### Step 5: Implement (30+ min)
Follow the phases in IMPLEMENTATION_GUIDE.md

**Quick Implementation Path**:
1. Create data models (10 min)
2. Create interfaces (5 min)
3. Mock RedisClient (10 min)
4. Implement SlidingWindowCounterStrategy (15 min)
5. Write basic tests (15 min)

---

## 📝 Interview Preparation Checklist

### Must Know
- [ ] Why use Redis instead of local memory?
- [ ] What is a race condition in rate limiting?
- [ ] Why use Lua scripts instead of multiple Redis commands?
- [ ] Explain sliding window counter algorithm
- [ ] Time complexity: O(1) for all operations
- [ ] Space complexity: O(number_of_users × number_of_windows)

### Should Know
- [ ] Difference between token bucket and sliding window
- [ ] Fixed window boundary problem
- [ ] How to handle Redis failures (fail open vs fail closed)
- [ ] Distributed rate limiting challenges
- [ ] When to use which strategy

### Nice to Have
- [ ] Redis cluster sharding
- [ ] Connection pooling
- [ ] Circuit breaker pattern
- [ ] Multi-tier rate limiting
- [ ] Monitoring and alerting

---

## 🎤 Common Interview Questions

### Q1: "Why not use a database instead of Redis?"
**Answer**:
- Redis is in-memory → much faster (sub-millisecond)
- Redis Lua scripts provide atomicity
- Database transactions have higher overhead
- Redis sorted sets are perfect for time-based data

### Q2: "What happens if Redis goes down?"
**Answer**:
- **Fail Open**: Allow all requests (risk of abuse)
- **Fail Closed**: Reject all requests (availability impact)
- **Circuit Breaker**: Detect failure, fallback to local rate limiting
- **Redis Sentinel**: Automatic failover to replica

### Q3: "How do you prevent race conditions?"
**Answer**:
- Use Lua scripts for atomic operations
- All check-and-increment happens in single Redis command
- Redis is single-threaded, scripts never interleave

### Q4: "Fixed window vs sliding window?"
**Answer**:
Fixed Window:
- ✅ Simple, low memory
- ❌ Boundary problem (2x limit at edges)

Sliding Window:
- ✅ Accurate, no boundary problem
- ❌ More complex, higher memory (if using log)

Sliding Window Counter:
- ✅ Good accuracy, low memory
- ✅ Best of both worlds

### Q5: "How do you handle millions of users?"
**Answer**:
- Redis cluster for sharding
- Consistent hashing for key distribution
- Connection pooling
- Consider eventual consistency with local cache
- Multi-level caching (L1 in-memory, L2 Redis)

### Q6: "What's the time complexity?"
**Answer**:
- Token Bucket: O(1)
- Fixed Window: O(1)
- Sliding Window Counter: O(1)
- Sliding Window Log: O(log N) for cleanup, but amortized O(1)

---

## 💡 Design Discussion Points

### Tradeoffs
1. **Accuracy vs Performance**
   - Sliding log: Most accurate, but expensive
   - Fixed window: Fastest, but boundary issues
   - Sliding counter: Good balance

2. **Memory vs Accuracy**
   - Store every request → accurate but memory-intensive
   - Store just counters → memory-efficient but less accurate

3. **Consistency vs Availability**
   - Strict rate limiting → might reject valid requests if Redis lags
   - Relaxed limits → better availability but risk of abuse

### Scalability Considerations
1. **Redis Cluster**: Shard by user ID for horizontal scaling
2. **Read Replicas**: Separate read and write if read-heavy
3. **Local Cache**: Cache rate limit checks for 100-500ms
4. **Batch Processing**: For analytics, not real-time limiting

### Production Concerns
1. **Monitoring**: Track rate limit hits, Redis latency, error rates
2. **Alerting**: Redis down, high rejection rate, latency spikes
3. **Configuration**: Dynamic limits via config service
4. **Testing**: Load tests, chaos engineering

---

## 🔨 Quick Implementation Template

### Minimal Working Example
```java
// 1. Config
RateLimiterConfig config = new RateLimiterConfig(
    100,     // 100 requests
    60_000,  // per minute
    RateLimitStrategyType.SLIDING_WINDOW_COUNTER
);

// 2. Create rate limiter
RateLimiter rateLimiter = new DistributedRateLimiter(
    config,
    redisClient,
    "api"  // key prefix
);

// 3. Check rate limit
RateLimitResult result = rateLimiter.isAllowed("user123");

if (result.isAllowed()) {
    // Process request
    System.out.println("Allowed. Remaining: " + result.getRemaining());
} else {
    // Reject request
    System.out.println("Rate limited. Retry after: " + result.getRetryAfterMs() + "ms");
    // Return HTTP 429 Too Many Requests
}
```

---

## 📊 Testing Strategy

### Test Cases to Cover

#### 1. Basic Flow
```java
// Make 100 requests (should all succeed)
// Make 101st request (should fail)
```

#### 2. Window Transitions
```java
// Make 50 requests
// Wait for window to change
// Make 50 more requests (should succeed)
```

#### 3. Concurrent Requests
```java
// Multiple threads making requests
// Verify total doesn't exceed limit
```

#### 4. Different Users
```java
// User A at limit shouldn't affect User B
```

#### 5. Edge Cases
```java
// Exactly at limit
// Multiple rapid requests
// Long gaps between requests
```

---

## 🎯 Success Criteria

You've successfully implemented a distributed rate limiter when:

✅ **Functional**
- [ ] Correctly allows/denies requests based on limit
- [ ] Works across multiple servers (distributed)
- [ ] No race conditions under concurrent load
- [ ] Properly resets counters after window

✅ **Non-Functional**
- [ ] Latency < 10ms for rate limit check
- [ ] Handles 10,000+ requests/second
- [ ] Memory usage is reasonable
- [ ] Gracefully handles Redis failures

✅ **Code Quality**
- [ ] Clean abstractions and interfaces
- [ ] Well-tested (unit + integration)
- [ ] Documented design decisions
- [ ] Follows SOLID principles

---

## 📖 Learning Path

### Beginner (Day 1-2)
1. Read all documentation files
2. Understand sliding window algorithm
3. Implement basic fixed window counter
4. Write tests with mock Redis

### Intermediate (Day 3-4)
5. Implement sliding window counter
6. Write and test Lua scripts
7. Integration test with real Redis
8. Handle edge cases

### Advanced (Day 5+)
9. Implement token bucket strategy
10. Add Redis connection pooling
11. Implement circuit breaker
12. Load test and optimize
13. Add monitoring/metrics

---

## 🛠️ Tools You'll Need

### For Local Development
- **Java 8+**: Your implementation language
- **Redis**: `brew install redis` or Docker
- **Redis Client**: Jedis or Lettuce library
- **JUnit**: For testing

### For Testing
- **Redis CLI**: Test Lua scripts
- **JMeter/Gatling**: Load testing
- **Docker**: For running Redis locally

### Start Redis Locally
```bash
# Using Docker
docker run --name redis -p 6379:6379 -d redis

# Or using Homebrew
brew install redis
redis-server

# Test connection
redis-cli ping
# Should return: PONG
```

---

## 🎓 Next Steps

1. **Start Small**: Implement fixed window first
2. **Test Early**: Write tests before moving to Lua scripts
3. **Iterate**: Add complexity gradually
4. **Measure**: Profile and optimize
5. **Learn**: Try different strategies

### Recommended Order
1. Fixed Window Counter (simplest)
2. Sliding Window Counter (most practical)
3. Token Bucket (for burst handling)
4. Sliding Window Log (for accuracy)

---

## 📚 Additional Resources

### In This Folder
- `README.md` - Complete theory and concepts
- `IMPLEMENTATION_GUIDE.md` - Detailed code structure
- `REDIS_LUA_SCRIPTS.md` - All Lua scripts with explanations

### External Resources
- [Redis Documentation](https://redis.io/docs/)
- [Rate Limiting Patterns](https://cloud.google.com/architecture/rate-limiting-strategies)
- [Kong Rate Limiting](https://github.com/Kong/kong/tree/master/kong/plugins/rate-limiting)
- [Stripe Rate Limiting](https://stripe.com/blog/rate-limiters)

---

## ⏱️ Time Estimates

| Task | Beginner | Intermediate | Advanced |
|------|----------|--------------|----------|
| Understand concepts | 2 hours | 1 hour | 30 min |
| Basic implementation | 4 hours | 2 hours | 1 hour |
| Lua scripts | 3 hours | 1 hour | 30 min |
| Testing | 3 hours | 2 hours | 1 hour |
| **Total** | **12 hours** | **6 hours** | **3 hours** |

---

## 🎉 You're Ready!

You now have everything needed to implement a production-grade distributed rate limiter.

**Start with**: README.md → IMPLEMENTATION_GUIDE.md → Code!

**Stuck?**: Check REDIS_LUA_SCRIPTS.md for ready-to-use scripts

**Questions?**: Review common interview questions section above

Good luck! 🚀
