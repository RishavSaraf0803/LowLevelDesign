# Database Selection Strategy for E-Commerce Microservices

## 🎯 **Overview**

This document outlines the comprehensive database selection strategy for our e-commerce microservices platform. We implement a **polyglot persistence** approach, selecting the optimal database technology for each specific use case based on data characteristics, access patterns, consistency requirements, and scalability needs.

## 📊 **Database Architecture Decision Matrix**

| Service | Primary DB | Cache Layer | Search Engine | Analytics Store | Reasoning |
|---------|------------|-------------|---------------|-----------------|-----------|
| User Service | PostgreSQL | Redis | - | - | ACID compliance, complex queries |
| Product Service | MongoDB | Redis | Elasticsearch | - | Flexible schema, rich queries |
| Order Service | PostgreSQL | Redis | - | ClickHouse | Financial data integrity |
| Payment Service | PostgreSQL | Redis | - | - | Strong consistency, compliance |
| Notification Service | MongoDB | Redis | - | - | Document storage, high writes |

## 🗄️ **Primary Database Selections**

### **PostgreSQL - Relational Database Choice**

**Selected for:** User Service, Order Service, Payment Service

#### **Why PostgreSQL?**

1. **ACID Compliance**
   - Full ACID transactions for financial data
   - Strong consistency guarantees
   - Data integrity for critical operations

2. **Advanced SQL Features**
   - Complex joins and aggregations
   - Window functions for analytics
   - Common Table Expressions (CTEs)
   - JSON/JSONB support for flexibility

3. **Scalability Features**
   - Read replicas for scaling reads
   - Partitioning for large tables
   - Connection pooling with PgBouncer
   - Horizontal scaling with Citus (if needed)

4. **Operational Excellence**
   - Mature ecosystem and tooling
   - Excellent monitoring with pg_stat_statements
   - Point-in-time recovery
   - Logical replication for real-time sync

#### **Configuration Example**

```sql
-- User Service Database Schema
CREATE DATABASE ecommerce_users
    WITH ENCODING 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8';

-- Performance optimization settings
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET work_mem = '4MB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;

-- Connection pooling
ALTER SYSTEM SET max_connections = 200;
ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';
```

#### **Indexing Strategy**

```sql
-- User Service Indexes
CREATE INDEX CONCURRENTLY idx_users_email ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX CONCURRENTLY idx_users_username ON users(username) WHERE deleted_at IS NULL;
CREATE INDEX CONCURRENTLY idx_users_status ON users(status) WHERE deleted_at IS NULL;
CREATE INDEX CONCURRENTLY idx_users_last_login ON users(last_login DESC);
CREATE INDEX CONCURRENTLY idx_users_created_at ON users(created_at DESC);

-- Partial indexes for performance
CREATE INDEX CONCURRENTLY idx_users_failed_attempts 
ON users(failed_login_attempts) 
WHERE failed_login_attempts > 0 AND deleted_at IS NULL;

-- Composite indexes for common queries
CREATE INDEX CONCURRENTLY idx_users_status_created 
ON users(status, created_at DESC) 
WHERE deleted_at IS NULL;
```

### **MongoDB - Document Database Choice**

**Selected for:** Product Service, Notification Service

#### **Why MongoDB?**

1. **Schema Flexibility**
   - Product catalogs with varying attributes
   - Easy schema evolution without migrations
   - Nested documents for complex data structures

2. **Rich Query Capabilities**
   - Powerful aggregation framework
   - Geospatial queries for location-based features
   - Text search capabilities
   - Array and embedded document operations

3. **Horizontal Scalability**
   - Built-in sharding for massive datasets
   - Automatic failover with replica sets
   - Read preference routing

4. **Developer Productivity**
   - JSON-native storage and queries
   - Flexible data modeling
   - Rich ecosystem and drivers

#### **Configuration Example**

```javascript
// Product Service Collection Design
db.products.createIndex({ "name": "text", "description": "text" });
db.products.createIndex({ "category": 1, "price": 1 });
db.products.createIndex({ "brand": 1, "status": 1 });
db.products.createIndex({ "created_at": -1 });
db.products.createIndex(
  { "attributes.size": 1, "attributes.color": 1 },
  { sparse: true }
);

// Compound index for common filters
db.products.createIndex({
  "category": 1,
  "price": 1,
  "status": 1,
  "in_stock": 1
});

// Geospatial index for location-based queries
db.products.createIndex({ "store_locations": "2dsphere" });
```

#### **Document Schema Example**

```javascript
// Product Document Schema
{
  "_id": ObjectId("..."),
  "sku": "PROD-12345",
  "name": "Premium Wireless Headphones",
  "description": "High-quality wireless headphones with noise cancellation",
  "category": "electronics",
  "subcategory": "audio",
  "brand": "TechBrand",
  "price": {
    "amount": 299.99,
    "currency": "USD",
    "discount": {
      "percentage": 10,
      "valid_until": ISODate("2024-12-31T23:59:59Z")
    }
  },
  "attributes": {
    "color": ["black", "white", "silver"],
    "connectivity": ["bluetooth", "wired"],
    "battery_life": "30 hours",
    "features": ["noise_cancellation", "quick_charge", "voice_assistant"]
  },
  "inventory": {
    "in_stock": true,
    "quantity": 150,
    "reserved": 5,
    "warehouse_locations": ["US-WEST", "US-EAST"]
  },
  "seo": {
    "slug": "premium-wireless-headphones-techbrand",
    "meta_title": "Premium Wireless Headphones - TechBrand",
    "meta_description": "Shop the best wireless headphones with premium sound quality"
  },
  "status": "active",
  "created_at": ISODate("2023-11-01T10:00:00Z"),
  "updated_at": ISODate("2023-12-01T15:30:00Z")
}
```

## 🚀 **Caching Strategy with Redis**

### **Why Redis as Universal Cache?**

1. **Performance Benefits**
   - Sub-millisecond latency for cache hits
   - In-memory storage for maximum speed
   - Reduces database load significantly

2. **Data Structure Flexibility**
   - Strings, hashes, lists, sets, sorted sets
   - Atomic operations on complex data types
   - Support for advanced use cases

3. **Scalability and Availability**
   - Redis Cluster for horizontal scaling
   - Sentinel for high availability
   - Read replicas for scaling reads

4. **Advanced Features**
   - Pub/Sub for real-time messaging
   - Transactions with MULTI/EXEC
   - Lua scripting for complex operations
   - Streams for event processing

### **Multi-Level Caching Architecture**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Application   │    │   Redis Cache   │    │   Primary DB    │
│     Cache       │───▶│     (L2)        │───▶│      (L3)       │
│     (L1)        │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
     Caffeine              Distributed           PostgreSQL/
     (Local)               Redis Cluster         MongoDB
```

### **Cache Implementation Examples**

#### **User Service Caching**

```java
@Service
public class UserCacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String USER_CACHE_PREFIX = "user:";
    private static final int USER_CACHE_TTL = 3600; // 1 hour
    
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserById(String userId) {
        // Cache miss will trigger database query
        return userService.findById(userId);
    }
    
    @CachePut(value = "users", key = "#userResponse.id")
    public UserResponse updateUserCache(UserResponse userResponse) {
        return userResponse;
    }
    
    @CacheEvict(value = "users", key = "#userId")
    public void evictUser(String userId) {
        // Explicitly remove from cache
    }
    
    // Session management
    public void cacheUserSession(String sessionId, UserSession session) {
        String key = "session:" + sessionId;
        redisTemplate.opsForValue().set(key, session, Duration.ofMinutes(30));
    }
    
    // Failed login attempts tracking
    public void incrementFailedAttempts(String email) {
        String key = "failed_attempts:" + email;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(15));
    }
}
```

#### **Product Service Caching**

```java
@Service
public class ProductCacheService {
    
    // Hot products cache (frequently accessed)
    @Cacheable(value = "hot_products", key = "#productId")
    public ProductResponse getHotProduct(String productId) {
        return productService.findById(productId);
    }
    
    // Category-based caching
    public List<ProductResponse> getCachedProductsByCategory(String category) {
        String key = "products:category:" + category;
        List<ProductResponse> products = (List<ProductResponse>) 
            redisTemplate.opsForValue().get(key);
            
        if (products == null) {
            products = productService.findByCategory(category);
            redisTemplate.opsForValue().set(key, products, Duration.ofMinutes(10));
        }
        
        return products;
    }
    
    // Search results caching
    public SearchResults getCachedSearchResults(SearchQuery query) {
        String key = "search:" + DigestUtils.md5Hex(query.toString());
        SearchResults results = (SearchResults) redisTemplate.opsForValue().get(key);
        
        if (results == null) {
            results = searchService.search(query);
            redisTemplate.opsForValue().set(key, results, Duration.ofMinutes(5));
        }
        
        return results;
    }
}
```

### **Cache Invalidation Strategies**

1. **Time-Based Expiration (TTL)**
   ```java
   // Short TTL for frequently changing data
   redisTemplate.opsForValue().set("inventory:" + productId, inventory, Duration.ofMinutes(1));
   
   // Longer TTL for stable data
   redisTemplate.opsForValue().set("user:" + userId, user, Duration.ofHours(1));
   ```

2. **Event-Driven Invalidation**
   ```java
   @EventListener
   public void handleUserUpdatedEvent(UserUpdatedEvent event) {
       cacheManager.evict("users", event.getUserId());
       // Also evict related caches
       cacheManager.evict("user_preferences", event.getUserId());
   }
   ```

3. **Cache-Aside Pattern**
   ```java
   public UserResponse getUser(String userId) {
       // Try cache first
       UserResponse user = getUserFromCache(userId);
       if (user == null) {
           // Cache miss - load from database
           user = userRepository.findById(userId);
           if (user != null) {
               // Update cache
               cacheUser(userId, user);
           }
       }
       return user;
   }
   ```

## 🔍 **Search Engine Integration**

### **Elasticsearch for Product Search**

#### **Why Elasticsearch?**
- Full-text search capabilities
- Faceted search and filtering
- Real-time indexing
- Scalable distributed architecture
- Rich aggregations for analytics

#### **Index Configuration**

```json
{
  "settings": {
    "number_of_shards": 3,
    "number_of_replicas": 1,
    "analysis": {
      "analyzer": {
        "product_analyzer": {
          "type": "custom",
          "tokenizer": "standard",
          "filter": [
            "lowercase",
            "asciifolding",
            "synonym_filter"
          ]
        }
      },
      "filter": {
        "synonym_filter": {
          "type": "synonym",
          "synonyms": [
            "laptop,notebook,computer",
            "phone,mobile,smartphone"
          ]
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "product_analyzer",
        "fields": {
          "keyword": {
            "type": "keyword"
          }
        }
      },
      "description": {
        "type": "text",
        "analyzer": "product_analyzer"
      },
      "category": {
        "type": "keyword"
      },
      "price": {
        "type": "double"
      },
      "brand": {
        "type": "keyword"
      },
      "attributes": {
        "type": "nested",
        "properties": {
          "name": {"type": "keyword"},
          "value": {"type": "keyword"}
        }
      },
      "created_at": {
        "type": "date"
      }
    }
  }
}
```

## 📈 **Analytics and Data Warehouse**

### **ClickHouse for Analytics**

#### **Why ClickHouse?**
- Columnar storage for analytical queries
- Extremely fast aggregations
- Real-time analytics capabilities
- Excellent compression ratios
- SQL compatibility

#### **Order Analytics Schema**

```sql
CREATE TABLE order_analytics (
    order_id String,
    user_id String,
    order_date DateTime,
    total_amount Decimal(10, 2),
    currency String,
    status Enum8('pending' = 1, 'confirmed' = 2, 'shipped' = 3, 'delivered' = 4, 'cancelled' = 5),
    payment_method String,
    items_count UInt32,
    discount_amount Decimal(10, 2),
    shipping_cost Decimal(10, 2),
    country String,
    city String,
    created_at DateTime DEFAULT now()
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(order_date)
ORDER BY (order_date, user_id)
SETTINGS index_granularity = 8192;

-- Example analytical queries
SELECT 
    toStartOfDay(order_date) as day,
    count() as orders,
    sum(total_amount) as revenue
FROM order_analytics 
WHERE order_date >= today() - 30
GROUP BY day 
ORDER BY day;
```

## 🔄 **Data Synchronization Strategies**

### **Change Data Capture (CDC)**

```java
@Component
public class DatabaseChangeListener {
    
    @Autowired
    private ElasticsearchService elasticsearchService;
    
    @Autowired
    private CacheService cacheService;
    
    @EventListener
    @Async
    public void handleProductChanged(ProductChangedEvent event) {
        try {
            // Update search index
            elasticsearchService.indexProduct(event.getProductId());
            
            // Invalidate cache
            cacheService.evictProduct(event.getProductId());
            
            // Update analytics store
            analyticsService.updateProductMetrics(event.getProductId());
            
        } catch (Exception e) {
            log.error("Failed to sync product changes", e);
            // Implement retry logic or dead letter queue
        }
    }
}
```

### **Event-Driven Architecture**

```java
@Service
public class ProductEventPublisher {
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public void publishProductUpdated(Product product) {
        ProductUpdatedEvent event = new ProductUpdatedEvent(
            product.getId(),
            product.getName(),
            product.getCategory(),
            System.currentTimeMillis()
        );
        
        eventPublisher.publishEvent(event);
    }
}
```

## 🛡️ **Data Security and Compliance**

### **Encryption at Rest**
- Database-level encryption for sensitive data
- Application-level encryption for PII
- Key rotation policies

### **Backup and Recovery**
```bash
# PostgreSQL continuous archiving
archive_mode = on
archive_command = 'cp %p /backup/archive/%f'
wal_level = replica

# MongoDB replica set backup
mongodump --host replica-set/mongo1:27017,mongo2:27017,mongo3:27017 --out /backup/mongodb/
```

### **Data Retention Policies**
```sql
-- Automated data cleanup
DELETE FROM user_sessions WHERE created_at < NOW() - INTERVAL '30 days';
DELETE FROM audit_logs WHERE created_at < NOW() - INTERVAL '7 years';
```

## 📊 **Performance Monitoring**

### **Database Metrics to Monitor**

1. **PostgreSQL Metrics**
   - Connection pool utilization
   - Query execution time
   - Lock waits and deadlocks
   - Cache hit ratio
   - Replication lag

2. **MongoDB Metrics**
   - Operation counters
   - Memory usage
   - Index usage statistics
   - Replica set health

3. **Redis Metrics**
   - Memory usage and fragmentation
   - Cache hit/miss ratios
   - Key expiration rates
   - Connection count

### **Alerting Thresholds**
```yaml
# Database performance alerts
alerts:
  - name: high_database_connections
    condition: postgresql_connections > 80%
    severity: warning
    
  - name: slow_queries
    condition: postgresql_slow_queries > 10
    severity: critical
    
  - name: cache_miss_rate
    condition: redis_cache_hit_rate < 85%
    severity: warning
```

## 🎯 **Conclusion**

This polyglot persistence strategy provides:

1. **Optimal Performance**: Right tool for the right job
2. **Scalability**: Each database can scale independently
3. **Reliability**: Fault isolation between services
4. **Flexibility**: Easy to adapt to changing requirements
5. **Cost Efficiency**: Pay only for what you need

The combination of PostgreSQL for transactional data, MongoDB for flexible documents, Redis for caching, and Elasticsearch for search provides a robust, scalable foundation for our e-commerce platform.

## 📚 **Further Learning**

- [PostgreSQL Performance Tuning Guide](./postgresql-tuning.md)
- [MongoDB Schema Design Patterns](./mongodb-patterns.md)
- [Redis Caching Best Practices](./redis-best-practices.md)
- [Elasticsearch Index Optimization](./elasticsearch-optimization.md)