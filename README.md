# Low Level Design - Java

Production-ready implementations of design patterns, system design problems, and concurrency patterns in Java.

## Source

Course material and implementations from [Programming Pathshala](https://programmingpathshala.com/).

## Structure

```
├── Design Patterns          # Gang of Four patterns + SOLID principles
├── System Design Problems   # Real-world LLD implementations
├── Concurrency             # Multithreading patterns and problems
├── Data Structures         # Cache systems, rate limiters, bloom filters
└── Java Practice           # Generics, functional programming, streams
```

## Design Patterns

**Creational**: Singleton, Builder, Factory
**Structural**: Decorator, Adapter, Proxy, Facade
**Behavioral**: Command, State, Strategy, Observer, Iterator, Chain of Responsibility

**Principles**: SOLID (Open-Close, Interface Segregation, Dependency Injection)

## System Design

- **ATM Machine** - State pattern, transaction processing
- **Parking Lot** - Resource allocation, payment processing
- **Food Delivery** - Order management, filtering, payments
- **Library Management** - User auth, book tracking, search
- **Stock Trading** - Real-time processing, order matching
- **Cricket Scoreboard** - Pub-sub pattern

## Concurrency

**Fundamentals**: Explicit locks, wait-notify, synchronized blocks
**Problems**: Producer-consumer, dining philosophers, sleeping barber, bank problem
**Advanced**: Thread pools, connection pools, blocking queues, futures, lock-free structures, semaphores

## Data Structure Design

LRU/LFU Cache, Rate Limiter, Bloom Filter, Consistent Hashing, Hit Counter, Trie, Skip List, Segment Tree, Time-based KV Store, Leaderboard

## Setup

```bash
# Compile
javac -d bin src/**/*.java

# Run specific problem
java -cp bin <PackageName>.<ClassName>
```

## Usage

Each module is self-contained with:
- Clean separation of concerns
- Factory patterns for object creation
- Interfaces for extensibility
- Thread-safe implementations where applicable

Explore `docs/LEARNING_PATH.md` for structured learning progression.
