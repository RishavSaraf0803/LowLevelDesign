# SDE Interview Practice Problems

A comprehensive collection of essential problems for Software Development Engineer interviews, organized by category.

---

## 📁 Directory Structure

```
/LowLevelDesign/src/
├── DataStructureDesignProblems/  (Core data structure implementations)
├── OODesignProblems/              (System design problems)
├── Concurrency/                   (Multithreading & concurrency)
├── OODesignPatterns/              (Design pattern applications)
└── OODesignPrinciples/            (SOLID principles)
```

---

## 1️⃣ DataStructureDesignProblems/

### Essential Cache Implementations
- **DesignLRUCache/** ⭐⭐⭐
  - HashMap + Doubly Linked List
  - O(1) get and put operations
  - Eviction policy: Least Recently Used

- **DesignLFUCache/**
  - Least Frequently Used eviction
  - Frequency tracking with O(1) operations
  - Handle tie-breaking with LRU

- **TimeBasedKeyValueStore/**
  - Versioned key-value storage
  - Binary search for timestamp lookups
  - Snapshot-based retrieval

### Core Data Structures
- **DesignHashMap/** ⭐⭐⭐
  - Implement from scratch
  - Collision handling: chaining or open addressing
  - Dynamic resizing with load factor

- **DesignMinStack/**
  - Stack with O(1) min/max operations
  - Space-efficient tracking

- **DesignTrie/**
  - Prefix tree for dictionary operations
  - Insert, search, startsWith
  - Applications: autocomplete, spell check

- **DesignCircularQueue/**
  - Fixed-size ring buffer
  - Head and tail pointer management
  - Full/empty detection

### Advanced Structures
- **ConsistentHashing/** ⭐⭐⭐
  - Virtual nodes for load balancing
  - Add/remove servers with minimal rehashing
  - Distributed system essential

- **BloomFilter/**
  - Probabilistic membership test
  - Space-efficient with false positive rate
  - Multiple hash functions

- **SkipList/**
  - Probabilistic balanced structure
  - Alternative to balanced trees
  - O(log n) search, insert, delete

- **SegmentTree/**
  - Range query and update
  - Build tree for sum/min/max queries
  - Lazy propagation for range updates

### Specialized Designs
- **DesignHitCounter/**
  - Count hits in last N seconds
  - Sliding window implementation
  - Handle high QPS

- **DesignLeaderboard/**
  - Real-time ranking system
  - Score updates and rank queries
  - Top-K retrieval

---

## 2️⃣ OODesignProblems/

### Rate Limiting (Critical)
- **DesignRateLimiter/** ⭐⭐⭐
  - **Token Bucket**: Allow bursts, refill rate
  - **Sliding Window**: Accurate rate limiting
  - **Fixed Window Counter**: Simple but has edge cases
  - **Leaky Bucket**: Smooth traffic flow
  - **Distributed Rate Limiter**: Redis-based, multi-server

### Sharing & Splitting Systems
- **Splitwise/** ⭐⭐⭐
  - Expense sharing between users and groups
  - Debt graph construction
  - Optimal settlement algorithm (minimize transactions)
  - Split types: equal, exact, percentage

- **RideSharingSystem/** ⭐⭐⭐
  - Rider and driver matching
  - Location-based search (geospatial indexing)
  - Pricing calculation (surge, distance)
  - Ride states: requested, accepted, in-progress, completed
  - Driver availability and ratings

- **MeetingScheduler/** ⭐⭐⭐
  - Room booking with conflict detection
  - Interval overlap checking
  - Resource allocation strategies
  - Recurring meetings support

### Booking & E-commerce
- **MovieTicketBooking/** ⭐⭐⭐
  - Seat selection and locking mechanism
  - Concurrent booking handling
  - Payment timeout and rollback
  - Show scheduling and theater management

- **ShoppingCart/**
  - Add/remove items, quantity management
  - Inventory reservation
  - Checkout flow with payment
  - Cart persistence and expiration

- **HotelBookingSystem/**
  - Room availability by date range
  - Booking and cancellation
  - Different room types and pricing
  - Reservation conflicts

- **AuctionSystem/**
  - Bidding mechanism
  - Highest bidder tracking
  - Auction close and winner determination
  - Bid history and notifications

### State Machine Problems
- **VendingMachine/** ⭐⭐
  - States: idle, selecting, payment, dispensing
  - Inventory management
  - Coin/bill handling
  - Refund mechanism

- **TrafficLightSystem/**
  - State transitions with timers
  - Multi-intersection coordination
  - Emergency vehicle priority

- **ElevatorSystem/** (Complete existing skeleton)
  - Request queue management
  - Direction states (up, down, idle)
  - Scheduling algorithms: SCAN, LOOK, FCFS
  - Multi-elevator coordination

### Games
- **TicTacToe/** ⭐⭐
  - Board representation (extensible size)
  - Win detection strategies
  - Player turns and validation
  - Strategy pattern for win checking

- **ConnectFour/**
  - Vertical drop mechanics
  - Diagonal win detection
  - Gravity-based placement

- **SudokuValidator/**
  - Row, column, box validation
  - Constraint checking
  - Backtracking solver (optional)

- **SnakeAndLadderGame/** (Already implemented)
- **Chess/** (Already implemented)

### Social & Content
- **TwitterNewsFeed/** ⭐⭐⭐
  - Follow/unfollow users
  - Timeline generation (fan-out strategies)
  - Tweet creation and retrieval
  - Push vs pull models

- **CommentsSystem/**
  - Nested/threaded comments (tree structure)
  - Voting and sorting (top, best, controversial)
  - Pagination for large threads
  - Comment editing and deletion

- **NotificationSystem/** ⭐⭐
  - Multi-channel: email, SMS, push, in-app
  - User preferences and opt-out
  - Deduplication and batching
  - Priority and urgency levels

- **ChatApplication/**
  - One-on-one and group chats
  - Message delivery and read receipts
  - Online/offline status
  - Message persistence

### File & Storage
- **InMemoryFileSystem/**
  - File and directory tree
  - Path resolution and navigation
  - File operations: create, read, write, delete
  - Directory listing

- **DropboxDesign/**
  - File upload and download
  - Sync mechanism
  - Versioning and conflict resolution
  - Sharing and permissions

- **URLShortener/** ⭐⭐⭐
  - Shorten URL (encode)
  - Redirect to original (decode)
  - Base62 encoding
  - Collision handling and custom aliases
  - Distributed ID generation

- **Pastebin/**
  - Paste creation with expiration
  - Access control (public, private, unlisted)
  - Syntax highlighting
  - URL shortening integration

### Advanced Systems
- **TaskScheduler/** ⭐⭐
  - Task submission with priority
  - Deadline-based scheduling
  - Task dependencies (DAG)
  - Worker pool management

- **RecommendationSystem/**
  - User-based collaborative filtering
  - Item-based recommendations
  - Similarity algorithms (cosine, Jaccard)
  - Cold start problem

- **SearchEngine/**
  - Inverted index construction
  - TF-IDF ranking
  - Query processing
  - Result ranking and relevance

- **TypeAheadSearch/**
  - Trie-based autocomplete
  - Weighted suggestions by frequency
  - Fuzzy matching (edit distance)
  - Query caching

- **CarRentalSystem/**
  - Vehicle inventory and types
  - Booking and availability
  - Pricing calculation
  - Damage assessment

- **OnlineJudgeSystem/**
  - Code submission and execution
  - Test case management
  - Verdict generation (AC, WA, TLE, etc.)
  - Sandbox execution

- **StackOverflowClone/**
  - Questions and answers
  - Voting and reputation system
  - Tags and search
  - Accepted answer

### Existing Systems (Already Implemented)
- **ParkingLot/** ✅
- **LibraryManagementSystem/** ✅
- **FoodDeliverySystem/** ✅
- **atmmachine/** ✅
- **logframework/** ✅
- **CricketBoard/** ✅
- **stocktrading/** ✅
- **EnquiryHandler/** ✅
- **JSONParser/** ✅
- **microprocessor/** ✅

---

## 3️⃣ Concurrency/

### Core Implementations
- **ThreadPool/** ⭐⭐⭐
  - Fixed-size worker thread pool
  - Task queue (blocking queue)
  - Thread lifecycle management
  - Graceful shutdown
  - Rejection policies

- **BlockingQueue/** ⭐⭐
  - Thread-safe queue implementation
  - Blocking on empty (take) and full (put)
  - Using wait/notify or locks
  - Bounded vs unbounded

- **SemaphoreImplementation/**
  - Counting semaphore from scratch
  - Acquire and release operations
  - Permit tracking
  - Fairness support

- **FuturePromise/**
  - Asynchronous computation result
  - get() blocking until ready
  - isDone() and cancel()
  - Exception handling

### Advanced Concurrency
- **ReadWriteLockWithWriteStarvation/** ✅ (Implemented)
- **ReadWriteLockWithoutWriteStarvation/** ✅ (Implemented)

- **ConnectionPool/**
  - Database connection pooling
  - Acquire and release connections
  - Max connections limit
  - Connection validation and timeout

- **DistributedRateLimiter/**
  - Redis-based rate limiting
  - Sliding window in distributed environment
  - Atomic operations with Lua scripts
  - Multi-server coordination

- **LockFreeDataStructures/**
  - Compare-and-swap (CAS) based
  - Lock-free stack
  - Lock-free queue
  - ABA problem handling

### Classic Problems
- **DiningPhilosopherProblem/** ✅ (Implemented)
- **BankProblem/** ✅ (Implemented)
- **WaitAndNotify/** ✅ (Implemented)
- **BusyWaitingAndThreadSignaling/** ✅ (Implemented)

- **SleepingBarber/**
  - Barber sleeps when no customers
  - Customers wake barber or wait
  - Waiting room capacity
  - Synchronization coordination

- **ProducerConsumerBounded/**
  - Multiple producers and consumers
  - Bounded buffer implementation
  - Proper signaling and coordination

### Challenges (Already Implemented)
- **EvenOdd/** ✅
- **FizzBuzz/** ✅
- **SortedSequence/** ✅
- **SumOfElements/** ✅

---

## 4️⃣ OODesignPatterns/

### Behavioral Patterns
- **UndoRedoManager/** ⭐⭐
  - Command pattern with history
  - Memento pattern for state
  - Stack-based undo/redo
  - Command interface: execute, undo

- **EventBus/**
  - Pub-sub pattern implementation
  - Event subscription and publishing
  - Asynchronous event handling
  - Topic-based routing

- **WorkflowEngine/**
  - State machine for workflows
  - Task execution pipeline
  - Conditional transitions
  - Error handling and retries

### Creational Patterns
- **ConnectionPool/** (see Concurrency)
  - Object pool pattern
  - Resource reuse
  - Lifecycle management

- **PluginSystem/**
  - Dynamic plugin loading
  - Factory + strategy patterns
  - Plugin interface and registration
  - Hot-swapping support

### Structural Patterns
- **CachingLayer/**
  - Decorator pattern for caching
  - Proxy pattern for lazy loading
  - Write-through, write-back strategies
  - Cache invalidation

- **APIGateway/**
  - Facade pattern
  - Request routing
  - Authentication and authorization
  - Rate limiting integration

### Existing Patterns (Already Implemented)
- **command/** ✅ (Command pattern)
- **withoutcor/** ✅ (Chain of Responsibility)
- **BuilderPattern/** ✅
- **DecoratorPattern/** ✅
- **IterPattern/** ✅
- **SingletonPattern/** ✅

---

## 5️⃣ OODesignPrinciples/

### Already Implemented ✅
- **DependencyInjectionPrinciple/**
- **InterfaceSegregationPrinciple/**
- **Open_Close_Principle/**
- **Polymorphism/**

---

## 🎯 Practice Priority

### **Tier 1: Must Practice First**
1. DesignLRUCache
2. DesignHashMap
3. DesignRateLimiter (Token Bucket + Sliding Window)
4. ThreadPool
5. BlockingQueue
6. Splitwise
7. TicTacToe
8. VendingMachine
9. MeetingScheduler
10. URLShortener

### **Tier 2: Important Concepts**
11. ConsistentHashing
12. RideSharingSystem
13. MovieTicketBooking
14. ElevatorSystem (complete existing)
15. DesignTrie
16. ReadWriteLock variations
17. TwitterNewsFeed
18. InMemoryFileSystem
19. NotificationSystem
20. TaskScheduler

### **Tier 3: Advanced Topics**
21. DesignLFUCache
22. BloomFilter
23. SkipList
24. DistributedRateLimiter
25. RecommendationSystem
26. SearchEngine
27. LockFreeDataStructures

---

## 📚 Key Concepts to Understand

### Data Structures
- Hash function design and collision handling
- Doubly linked list for O(1) operations
- Tree structures (Trie, Segment Tree)
- Probabilistic data structures (Bloom Filter)

### Concurrency
- Deadlock, livelock, starvation
- Thread signaling: wait/notify, condition variables
- Atomic operations and CAS
- Memory visibility and happens-before
- Lock-free programming

### System Design
- Caching strategies (write-through, write-back, cache-aside)
- Rate limiting algorithms
- Pessimistic vs optimistic locking
- Distributed ID generation
- Geospatial indexing
- Fan-out strategies (push vs pull)

### Design Patterns
- When to apply patterns
- Avoiding over-engineering
- Composition over inheritance
- Interface-based design

### Performance
- Time/space complexity tradeoffs
- Latency vs throughput
- Batch vs streaming processing
- Connection pooling
- Indexing strategies

---

## 💡 Practice Tips

1. **Start Simple**: Implement basic version first, then optimize
2. **Consider Edge Cases**: Empty inputs, concurrency, failures
3. **Think About Scale**: How does it work with millions of users?
4. **Explain Tradeoffs**: Why this approach over alternatives?
5. **Code Quality**: Clean code, proper abstractions, SOLID principles
6. **Test Thoroughly**: Unit tests, concurrent test scenarios
7. **Document Design**: Write brief design docs explaining approach

---

## 📖 Related Resources

- **Existing Documentation**:
  - `/src/LLD_SDE2_Revision_Guide.md` - LLD revision guide
  - `/src/OODesignProblems/ElevatorSystem/ELEVATOR_SYSTEM_LLD.md`
  - `/src/OODesignProblems/SnakeAndLadderGame/SNAKE_AND_LADDER_LLD.md`

- **Code Examples**: See existing implementations in each category for code structure and style guidance

---

**Last Updated**: November 2025
