# Microservices Architecture Guide - Best Practices & Design Principles

## 🎯 **Introduction**

This comprehensive guide outlines the best practices, design principles, and architectural decisions for building production-ready microservices. Based on real-world experience and industry standards, this guide will help you create scalable, maintainable, and resilient distributed systems.

## 🏗️ **Core Architecture Principles**

### **1. Single Responsibility Principle**
Each microservice should have a single, well-defined business responsibility.

```
❌ BAD: Monolithic User Service
┌─────────────────────────────────┐
│        User Service             │
├─────────────────────────────────┤
│ • User Registration             │
│ • Authentication                │
│ • Profile Management            │
│ • Order History                 │
│ • Payment Methods               │
│ • Notifications                 │
│ • Analytics                     │
└─────────────────────────────────┘

✅ GOOD: Decomposed Services
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ User Service │ │ Auth Service │ │Profile Service│
├──────────────┤ ├──────────────┤ ├──────────────┤
│• Registration│ │• Login/Logout│ │• User Details│
│• User CRUD   │ │• JWT Tokens  │ │• Preferences │
└──────────────┘ └──────────────┘ └──────────────┘

┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│Order Service │ │Payment Svc   │ │Notification  │
├──────────────┤ ├──────────────┤ ├──────────────┤
│• Order Mgmt  │ │• Transactions│ │• Emails/SMS  │
│• History     │ │• Methods     │ │• Push Alerts │
└──────────────┘ └──────────────┘ └──────────────┘
```

### **2. Database per Service**
Each microservice owns its data and database.

```java
// ❌ BAD: Shared Database
@Entity
@Table(name = "users", schema = "shared_db")
public class User {
    // Multiple services accessing same table
}

// ✅ GOOD: Service-Owned Database
@Entity
@Table(name = "users", schema = "user_service_db")
public class User {
    // Only User Service accesses this table
}
```

### **3. API-First Design**
Design your APIs before implementing the service.

```yaml
# OpenAPI Specification First
openapi: 3.0.0
info:
  title: User Service API
  version: 1.0.0
paths:
  /users:
    post:
      summary: Create new user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/CreateUserRequest'
      responses:
        '201':
          description: User created successfully
        '400':
          description: Invalid input
        '409':
          description: User already exists
```

## 📡 **Service Communication Patterns**

### **Synchronous Communication**

#### **REST APIs with Circuit Breaker**
```java
@Component
public class OrderServiceClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @CircuitBreaker(name = "order-service", fallbackMethod = "fallbackGetOrder")
    @Retry(name = "order-service")
    @TimeLimiter(name = "order-service")
    public CompletableFuture<OrderResponse> getOrderAsync(String orderId) {
        return CompletableFuture.supplyAsync(() -> {
            return restTemplate.getForObject(
                "/orders/{orderId}", 
                OrderResponse.class, 
                orderId
            );
        });
    }
    
    public CompletableFuture<OrderResponse> fallbackGetOrder(String orderId, Exception ex) {
        log.warn("Order service unavailable, using fallback for order: {}", orderId);
        return CompletableFuture.completedFuture(
            OrderResponse.builder()
                .id(orderId)
                .status("UNKNOWN")
                .message("Service temporarily unavailable")
                .build()
        );
    }
}
```

#### **Configuration for Resilience**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      order-service:
        failure-rate-threshold: 50
        wait-duration-in-open-state: 30s
        sliding-window-size: 10
        minimum-number-of-calls: 5
  retry:
    instances:
      order-service:
        max-attempts: 3
        wait-duration: 1s
        exponential-backoff-multiplier: 2
  timelimiter:
    instances:
      order-service:
        timeout-duration: 3s
```

### **Asynchronous Communication**

#### **Event-Driven Architecture with Kafka**
```java
@Service
public class UserEventPublisher {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishUserCreated(User user) {
        UserCreatedEvent event = UserCreatedEvent.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .timestamp(Instant.now())
            .build();
            
        kafkaTemplate.send("user-events", user.getId(), event);
        log.info("Published UserCreated event for user: {}", user.getId());
    }
}

@KafkaListener(topics = "user-events", groupId = "email-service-group")
@Component
public class EmailServiceEventListener {
    
    @Autowired
    private EmailService emailService;
    
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        try {
            emailService.sendWelcomeEmail(event.getUserId(), event.getEmail());
            log.info("Welcome email sent for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Failed to send welcome email for user: {}", event.getUserId(), e);
            // Implement retry or dead letter queue logic
        }
    }
}
```

## 🔐 **Security Best Practices**

### **JWT-Based Authentication**
```java
@Component
public class JwtTokenProvider {
    
    @Value("${app.security.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.security.jwt.expiration}")
    private int jwtExpirationInMs;
    
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}
```

### **API Gateway Security**
```java
@Component
public class AuthenticationFilter implements GatewayFilter {
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        if (!containsAuthHeader(request)) {
            return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
        }
        
        String token = extractToken(request);
        
        if (!tokenProvider.validateToken(token)) {
            return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
        }
        
        return chain.filter(exchange);
    }
    
    private boolean containsAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }
    
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ") 
            ? bearerToken.substring(7) : null;
    }
}
```

## 📊 **Observability and Monitoring**

### **Distributed Tracing**
```java
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users/{userId}")
    @NewSpan("get-user")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable String userId,
            @SpanTag("user.id") String userIdTag) {
        
        log.info("Fetching user with ID: {}", userId);
        
        try (Scope scope = tracer.nextSpan()
                .name("user-service-lookup")
                .tag("user.id", userId)
                .start()
                .makeCurrent()) {
            
            UserResponse user = userService.getUserById(userId);
            
            // Add custom metrics
            Metrics.counter("user.retrieval", "status", "success").increment();
            
            return ResponseEntity.ok(user);
            
        } catch (UserNotFoundException e) {
            Metrics.counter("user.retrieval", "status", "not_found").increment();
            throw e;
        } catch (Exception e) {
            Metrics.counter("user.retrieval", "status", "error").increment();
            span.current().tag("error", e.getMessage());
            throw e;
        }
    }
}
```

### **Custom Metrics**
```java
@Component
public class UserServiceMetrics {
    
    private final Counter userCreationCounter;
    private final Timer userCreationTimer;
    private final Gauge activeUsersGauge;
    
    public UserServiceMetrics(MeterRegistry meterRegistry) {
        this.userCreationCounter = Counter.builder("user.creation.total")
            .description("Total number of users created")
            .register(meterRegistry);
            
        this.userCreationTimer = Timer.builder("user.creation.duration")
            .description("Time taken to create a user")
            .register(meterRegistry);
            
        this.activeUsersGauge = Gauge.builder("user.active.count")
            .description("Number of currently active users")
            .register(meterRegistry, this, UserServiceMetrics::getActiveUserCount);
    }
    
    public void recordUserCreation() {
        userCreationCounter.increment();
    }
    
    public <T> T recordUserCreationTime(Supplier<T> operation) {
        return userCreationTimer.recordCallable(operation::get);
    }
    
    private double getActiveUserCount() {
        // Implementation to get active user count
        return userService.getActiveUserCount();
    }
}
```

### **Health Checks**
```java
@Component
public class UserServiceHealthIndicator implements HealthIndicator {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public Health health() {
        try {
            // Check database connectivity
            userRepository.count();
            
            // Check Redis connectivity
            redisTemplate.opsForValue().get("health-check");
            
            // Check external service dependencies
            checkExternalServices();
            
            return Health.up()
                .withDetail("database", "Available")
                .withDetail("cache", "Available")
                .withDetail("external-services", "Available")
                .build();
                
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
    
    private void checkExternalServices() {
        // Implement checks for external dependencies
    }
}
```

## 🚀 **Deployment and DevOps**

### **Containerization with Docker**
```dockerfile
# Multi-stage build for User Service
FROM openjdk:17-jdk-slim as builder
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17-jdk-slim
VOLUME /tmp

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-cp","app:app/lib/*","com.ecommerce.userservice.UserServiceApplication"]
```

### **Kubernetes Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
  labels:
    app: user-service
    version: v1
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 1
      maxSurge: 1
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
        version: v1
    spec:
      containers:
      - name: user-service
        image: ecommerce/user-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_HOST
          valueFrom:
            configMapKeyRef:
              name: user-service-config
              key: db.host
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: user-service-secrets
              key: db.password
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 10
          failureThreshold: 30
---
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
  ports:
  - port: 80
    targetPort: 8080
    protocol: TCP
  type: ClusterIP
```

### **CI/CD Pipeline (GitHub Actions)**
```yaml
name: User Service CI/CD

on:
  push:
    branches: [ main, develop ]
    paths: [ 'user-service/**' ]
  pull_request:
    branches: [ main ]
    paths: [ 'user-service/**' ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
        
    - name: Run tests
      run: |
        cd user-service
        mvn clean test
        
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: user-service/target/surefire-reports/*.xml
        reporter: java-junit
        
    - name: Code coverage
      run: |
        cd user-service
        mvn jacoco:report
        
    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: user-service/target/site/jacoco/jacoco.xml

  build-and-deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Build Docker image
      run: |
        cd user-service
        docker build -t ecommerce/user-service:${{ github.sha }} .
        docker tag ecommerce/user-service:${{ github.sha }} ecommerce/user-service:latest
        
    - name: Push to registry
      run: |
        echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
        docker push ecommerce/user-service:${{ github.sha }}
        docker push ecommerce/user-service:latest
        
    - name: Deploy to staging
      run: |
        # Update Kubernetes deployment
        kubectl set image deployment/user-service user-service=ecommerce/user-service:${{ github.sha }} -n staging
        kubectl rollout status deployment/user-service -n staging
        
    - name: Run smoke tests
      run: |
        # Run basic smoke tests against staging
        ./scripts/smoke-tests.sh staging
```

## 🧪 **Testing Strategies**

### **Unit Testing**
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
            .username("testuser")
            .email("test@example.com")
            .password("password123")
            .firstName("Test")
            .lastName("User")
            .build();
            
        User savedUser = new User("testuser", "test@example.com", "hashedPassword", "Test", "User");
        savedUser.setId("user123");
        
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        UserResponse result = userService.createUser(request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("user123");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getUsername()).isEqualTo("testuser");
        
        verify(emailService).sendVerificationEmail(savedUser);
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should throw exception when user already exists")
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
            .email("existing@example.com")
            .username("existinguser")
            .build();
            
        when(userRepository.findByEmail(request.getEmail()))
            .thenReturn(Optional.of(new User()));
            
        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
            .isInstanceOf(UserAlreadyExistsException.class)
            .hasMessage("Email already exists: existing@example.com");
    }
}
```

### **Integration Testing**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class UserServiceIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:6-alpine")
            .withExposedPorts(6379);
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }
    
    @Test
    void shouldCreateUserEndToEnd() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
            .username("integrationtest")
            .email("integration@test.com")
            .password("password123")
            .firstName("Integration")
            .lastName("Test")
            .build();
            
        // When
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
            "/api/v1/users", 
            request, 
            UserResponse.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("integration@test.com");
        
        // Verify in database
        Optional<User> savedUser = userRepository.findByEmail("integration@test.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getUsername()).isEqualTo("integrationtest");
    }
}
```

## 📈 **Performance Optimization**

### **Connection Pooling**
```java
@Configuration
public class DatabaseConfig {
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        
        // Performance optimizations
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000); // 5 minutes
        config.setConnectionTimeout(30000); // 30 seconds
        config.setMaxLifetime(1800000); // 30 minutes
        config.setLeakDetectionThreshold(60000); // 1 minute
        
        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);
        
        return new HikariDataSource(config);
    }
}
```

### **Async Processing**
```java
@Service
public class AsyncUserService {
    
    @Async("taskExecutor")
    public CompletableFuture<Void> sendWelcomeEmailAsync(String userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
            
            emailService.sendWelcomeEmail(user);
            log.info("Welcome email sent for user: {}", userId);
            
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("Failed to send welcome email for user: {}", userId, e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    @Async("taskExecutor")
    public CompletableFuture<UserAnalytics> generateUserAnalyticsAsync(String userId) {
        // Heavy computation done asynchronously
        UserAnalytics analytics = analyticsService.generateUserAnalytics(userId);
        return CompletableFuture.completedFuture(analytics);
    }
}

@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
```

## 🎯 **Best Practices Summary**

### **DO's**
✅ **Design for Failure**: Implement circuit breakers, retries, and fallbacks  
✅ **Monitor Everything**: Comprehensive logging, metrics, and tracing  
✅ **Automate Deployments**: CI/CD pipelines with automated testing  
✅ **Secure by Default**: Authentication, authorization, and input validation  
✅ **Cache Strategically**: Multi-level caching with proper invalidation  
✅ **Document APIs**: OpenAPI specifications and architectural decisions  
✅ **Test Thoroughly**: Unit, integration, and end-to-end testing  

### **DON'Ts**
❌ **Don't Share Databases**: Each service should own its data  
❌ **Don't Make Synchronous Calls for Non-Critical Operations**  
❌ **Don't Ignore Security**: Never skip authentication and authorization  
❌ **Don't Skip Monitoring**: Blind systems are impossible to debug  
❌ **Don't Hardcode Configuration**: Use externalized configuration  
❌ **Don't Ignore Data Consistency**: Plan for eventual consistency  
❌ **Don't Over-Engineer**: Start simple and evolve based on needs  

## 📚 **Further Reading**

- [Microservices Patterns by Chris Richardson](https://microservices.io/)
- [Building Microservices by Sam Newman](https://samnewman.io/books/building_microservices/)
- [Spring Boot Best Practices](./spring-boot-best-practices.md)
- [Docker and Kubernetes Guide](./containerization-guide.md)
- [Observability Implementation Guide](./observability-guide.md)

This architecture guide serves as a foundation for building robust, scalable microservices. Remember that architecture is an evolutionary process - start with these fundamentals and adapt based on your specific requirements and constraints.