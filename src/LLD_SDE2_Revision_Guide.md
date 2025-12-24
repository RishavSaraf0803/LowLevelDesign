# Low Level Design (LLD) Revision Guide - SDE2 Level

## Table of Contents
1. [Object-Oriented Programming Fundamentals](#oop-fundamentals)
2. [SOLID Principles](#solid-principles)
3. [Design Patterns](#design-patterns)
4. [UML Diagrams](#uml-diagrams)
5. [Common LLD Problems](#common-lld-problems)
6. [Code Quality & Best Practices](#code-quality)
7. [Interview Tips](#interview-tips)

---

## OOP Fundamentals

### Four Pillars of OOP

**1. Encapsulation**
- Bundling data and methods that operate on that data within a single unit (class)
- Data hiding through access modifiers (private, protected, public)
- Provides controlled access through getters/setters

**2. Abstraction**
- Hiding complex implementation details and showing only essential features
- Achieved through abstract classes and interfaces
- Focuses on "what" an object does rather than "how"

**3. Inheritance**
- Mechanism for creating new classes from existing ones
- Promotes code reusability
- Creates "is-a" relationships
- Types: Single, Multiple (through interfaces), Multilevel, Hierarchical

**4. Polymorphism**
- Ability of objects to take multiple forms
- **Compile-time (Method Overloading)**: Same method name, different parameters
- **Runtime (Method Overriding)**: Subclass provides specific implementation of parent method

### Key OOP Concepts

**Abstract Classes vs Interfaces**
- Abstract classes can have concrete methods; interfaces (traditionally) only have abstract methods
- A class can implement multiple interfaces but extend only one abstract class
- Use abstract classes for "is-a" relationships, interfaces for "can-do" capabilities

**Composition vs Inheritance**
- **Composition**: "Has-a" relationship (Car has an Engine)
- **Inheritance**: "Is-a" relationship (Car is a Vehicle)
- Favor composition over inheritance for flexibility

---

## SOLID Principles

### S - Single Responsibility Principle (SRP)
**Definition**: A class should have only one reason to change.

**Example Problem**: A User class that handles both user data and email sending.

```java
// BAD
class User {
    private String name;
    private String email;
    
    public void save() { /* save to database */ }
    public void sendEmail() { /* send email */ }
}

// GOOD
class User {
    private String name;
    private String email;
    
    public void save() { /* save to database */ }
}

class EmailService {
    public void sendEmail(User user) { /* send email */ }
}
```

### O - Open/Closed Principle (OCP)
**Definition**: Classes should be open for extension but closed for modification.

```java
// BAD
class AreaCalculator {
    public double calculateArea(Object shape) {
        if (shape instanceof Circle) {
            // calculate circle area
        } else if (shape instanceof Rectangle) {
            // calculate rectangle area
        }
        // Adding new shape requires modifying this method
    }
}

// GOOD
interface Shape {
    double calculateArea();
}

class Circle implements Shape {
    public double calculateArea() { /* implementation */ }
}

class Rectangle implements Shape {
    public double calculateArea() { /* implementation */ }
}
```

### L - Liskov Substitution Principle (LSP)
**Definition**: Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

```java
// BAD
class Bird {
    public void fly() { /* implementation */ }
}

class Penguin extends Bird {
    public void fly() {
        throw new UnsupportedOperationException();
    }
}

// GOOD
interface Bird {
    void eat();
}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird { /* implementation */ }
class Penguin implements Bird { /* implementation */ }
```

### I - Interface Segregation Principle (ISP)
**Definition**: Clients should not be forced to depend on interfaces they don't use.

```java
// BAD
interface Worker {
    void work();
    void eat();
    void sleep();
}

// Robot doesn't need eat() and sleep()

// GOOD
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Eatable, Sleepable { }
class Robot implements Workable { }
```

### D - Dependency Inversion Principle (DIP)
**Definition**: High-level modules should not depend on low-level modules. Both should depend on abstractions.

```java
// BAD
class MySQLDatabase {
    public void save(String data) { }
}

class UserService {
    private MySQLDatabase database = new MySQLDatabase();
    
    public void saveUser(String user) {
        database.save(user);
    }
}

// GOOD
interface Database {
    void save(String data);
}

class MySQLDatabase implements Database {
    public void save(String data) { }
}

class UserService {
    private Database database;
    
    public UserService(Database database) {
        this.database = database;
    }
    
    public void saveUser(String user) {
        database.save(user);
    }
}
```

---

## Design Patterns

### Creational Patterns

#### 1. Singleton Pattern
**Purpose**: Ensure a class has only one instance with global access point.

```java
class DatabaseConnection {
    private static DatabaseConnection instance;
    
    private DatabaseConnection() { }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}

// Thread-safe: Double-checked locking
class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    
    private DatabaseConnection() { }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
```

**Use Cases**: Logger, Configuration Manager, Database Connection Pool

#### 2. Factory Pattern
**Purpose**: Create objects without specifying exact class.

```java
interface Vehicle {
    void drive();
}

class Car implements Vehicle {
    public void drive() { System.out.println("Driving car"); }
}

class Bike implements Vehicle {
    public void drive() { System.out.println("Riding bike"); }
}

class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        if (type.equals("CAR")) return new Car();
        if (type.equals("BIKE")) return new Bike();
        return null;
    }
}
```

#### 3. Abstract Factory Pattern
**Purpose**: Create families of related objects.

```java
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

class WindowsFactory implements GUIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}

class MacFactory implements GUIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
```

#### 4. Builder Pattern
**Purpose**: Construct complex objects step by step.

```java
class User {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private String address;
    
    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.age = builder.age;
        this.phone = builder.phone;
        this.address = builder.address;
    }
    
    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private String address;
        
        public UserBuilder(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }
        
        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}

// Usage
User user = new User.UserBuilder("John", "Doe")
    .age(30)
    .phone("123-456")
    .address("123 Main St")
    .build();
```

#### 5. Prototype Pattern
**Purpose**: Create new objects by copying existing ones.

```java
interface Prototype {
    Prototype clone();
}

class Document implements Prototype {
    private String content;
    
    public Document(String content) {
        this.content = content;
    }
    
    public Prototype clone() {
        return new Document(this.content);
    }
}
```

### Structural Patterns

#### 1. Adapter Pattern
**Purpose**: Make incompatible interfaces work together.

```java
// Legacy printer
class LegacyPrinter {
    public void printOldFormat(String text) {
        System.out.println("Legacy: " + text);
    }
}

// Modern interface
interface ModernPrinter {
    void print(String text);
}

// Adapter
class PrinterAdapter implements ModernPrinter {
    private LegacyPrinter legacyPrinter;
    
    public PrinterAdapter(LegacyPrinter legacyPrinter) {
        this.legacyPrinter = legacyPrinter;
    }
    
    public void print(String text) {
        legacyPrinter.printOldFormat(text);
    }
}
```

#### 2. Decorator Pattern
**Purpose**: Add new functionality to objects dynamically.

```java
interface Coffee {
    double cost();
    String description();
}

class SimpleCoffee implements Coffee {
    public double cost() { return 5.0; }
    public String description() { return "Simple Coffee"; }
}

abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }
    
    public double cost() {
        return coffee.cost() + 1.5;
    }
    
    public String description() {
        return coffee.description() + ", Milk";
    }
}

// Usage
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
```

#### 3. Facade Pattern
**Purpose**: Provide simplified interface to complex subsystem.

```java
class CPU {
    public void freeze() { }
    public void jump(long position) { }
    public void execute() { }
}

class Memory {
    public void load(long position, byte[] data) { }
}

class HardDrive {
    public byte[] read(long lba, int size) { return null; }
}

// Facade
class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;
    
    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }
    
    public void start() {
        cpu.freeze();
        memory.load(0, hardDrive.read(0, 1024));
        cpu.jump(0);
        cpu.execute();
    }
}
```

#### 4. Proxy Pattern
**Purpose**: Provide placeholder for another object to control access.

```java
interface Image {
    void display();
}

class RealImage implements Image {
    private String filename;
    
    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }
    
    private void loadFromDisk() {
        System.out.println("Loading " + filename);
    }
    
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

class ProxyImage implements Image {
    private RealImage realImage;
    private String filename;
    
    public ProxyImage(String filename) {
        this.filename = filename;
    }
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}
```

### Behavioral Patterns

#### 1. Observer Pattern
**Purpose**: Define one-to-many dependency between objects.

```java
interface Observer {
    void update(String message);
}

interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(news);
        }
    }
    
    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

class NewsChannel implements Observer {
    private String news;
    
    public void update(String news) {
        this.news = news;
        System.out.println("News received: " + news);
    }
}
```

#### 2. Strategy Pattern
**Purpose**: Define family of algorithms and make them interchangeable.

```java
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void checkout(int amount) {
        paymentStrategy.pay(amount);
    }
}
```

#### 3. Command Pattern
**Purpose**: Encapsulate request as object.

```java
interface Command {
    void execute();
}

class Light {
    public void turnOn() { System.out.println("Light is ON"); }
    public void turnOff() { System.out.println("Light is OFF"); }
}

class TurnOnCommand implements Command {
    private Light light;
    
    public TurnOnCommand(Light light) {
        this.light = light;
    }
    
    public void execute() {
        light.turnOn();
    }
}

class RemoteControl {
    private Command command;
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public void pressButton() {
        command.execute();
    }
}
```

#### 4. State Pattern
**Purpose**: Allow object to change behavior when internal state changes.

```java
interface State {
    void handle();
}

class StartState implements State {
    public void handle() {
        System.out.println("In Start State");
    }
}

class StopState implements State {
    public void handle() {
        System.out.println("In Stop State");
    }
}

class Context {
    private State state;
    
    public void setState(State state) {
        this.state = state;
    }
    
    public void request() {
        state.handle();
    }
}
```

---

## UML Diagrams

### Class Diagram Components

**Relationships:**
1. **Association**: Simple relationship (line)
2. **Aggregation**: "Has-a" relationship (hollow diamond)
3. **Composition**: Strong "has-a" relationship (filled diamond)
4. **Inheritance**: "Is-a" relationship (hollow triangle arrow)
5. **Implementation**: Interface implementation (dashed line with hollow triangle)
6. **Dependency**: Uses relationship (dashed arrow)

**Example:**
```
┌─────────────────┐
│     Vehicle     │
├─────────────────┤
│ - brand: String │
├─────────────────┤
│ + start(): void │
└─────────────────┘
        △
        │ (inherits)
        │
┌─────────────────┐
│       Car       │
├─────────────────┤
│ - doors: int    │
├─────────────────┤
│ + drive(): void │
└─────────────────┘
```

### Sequence Diagram
Shows interaction between objects over time.

---

## Common LLD Problems

### 1. Parking Lot System

**Requirements:**
- Multiple floors, spots per floor
- Different vehicle types (Car, Bike, Truck)
- Entry/Exit points
- Payment processing
- Availability tracking

**Key Classes:**
```java
enum VehicleType { CAR, BIKE, TRUCK }
enum SpotType { COMPACT, LARGE, HANDICAPPED }

class Vehicle {
    private String licensePlate;
    private VehicleType type;
}

class ParkingSpot {
    private int spotId;
    private SpotType type;
    private boolean isAvailable;
    private Vehicle vehicle;
}

class ParkingFloor {
    private int floorNumber;
    private List<ParkingSpot> spots;
    
    public ParkingSpot findAvailableSpot(VehicleType type) { }
}

class ParkingLot {
    private List<ParkingFloor> floors;
    private static ParkingLot instance;
    
    public Ticket parkVehicle(Vehicle vehicle) { }
    public double unparkVehicle(Ticket ticket) { }
}

class Ticket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot spot;
    private LocalDateTime entryTime;
}
```

### 2. Library Management System

**Key Classes:**
```java
class Book {
    private String ISBN;
    private String title;
    private String author;
    private BookStatus status;
}

class Member {
    private String memberId;
    private String name;
    private List<BookLending> activeLoans;
    
    public boolean canBorrowBook() { }
}

class BookLending {
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
}

class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    
    public boolean issueBook(String ISBN, String memberId) { }
    public boolean returnBook(String ISBN, String memberId) { }
}
```

### 3. Elevator System

**Key Classes:**
```java
enum Direction { UP, DOWN, IDLE }

class Request {
    private int floor;
    private Direction direction;
}

class Elevator {
    private int currentFloor;
    private Direction direction;
    private Queue<Request> requests;
    
    public void addRequest(Request request) { }
    public void move() { }
}

class ElevatorController {
    private List<Elevator> elevators;
    
    public void requestElevator(int floor, Direction direction) { }
    private Elevator findOptimalElevator(int floor) { }
}
```

### 4. Online Shopping System

**Key Classes:**
```java
class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
}

class ShoppingCart {
    private List<CartItem> items;
    
    public void addItem(Product product, int quantity) { }
    public double calculateTotal() { }
}

class Order {
    private String orderId;
    private User user;
    private List<OrderItem> items;
    private OrderStatus status;
    private Payment payment;
}

class User {
    private String userId;
    private ShoppingCart cart;
    private List<Order> orderHistory;
    
    public void checkout() { }
}
```

### 5. Snake and Ladder Game

**Key Classes:**
```java
class Board {
    private int size;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    
    public int getNewPosition(int position) { }
}

class Player {
    private String name;
    private int position;
}

class Dice {
    public int roll() {
        return new Random().nextInt(6) + 1;
    }
}

class Game {
    private Board board;
    private List<Player> players;
    private Dice dice;
    private int currentPlayerIndex;
    
    public void play() { }
    private boolean hasWon(Player player) { }
}
```

### 6. Design a Hotel Management System

**Key Classes:**
```java
class Room {
    private String roomNumber;
    private RoomType type;
    private double pricePerNight;
    private RoomStatus status;
}

class Reservation {
    private String reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
}

class Guest {
    private String guestId;
    private String name;
    private String contact;
}

class Hotel {
    private List<Room> rooms;
    private List<Reservation> reservations;
    
    public List<Room> searchAvailableRooms(LocalDate checkIn, LocalDate checkOut, RoomType type) { }
    public Reservation makeReservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) { }
}
```

---

## Code Quality & Best Practices

### Naming Conventions
- **Classes**: PascalCase (UserAccount, OrderProcessor)
- **Methods**: camelCase (getUserData, calculateTotal)
- **Constants**: UPPER_SNAKE_CASE (MAX_SIZE, DEFAULT_TIMEOUT)
- **Variables**: camelCase (userName, totalAmount)

### Code Smells to Avoid
1. **Long methods**: Break into smaller methods
2. **Large classes**: Violates SRP
3. **Duplicate code**: Extract into reusable methods
4. **Too many parameters**: Use objects or builder pattern
5. **Magic numbers**: Use named constants

### Exception Handling
```java
// Custom exceptions
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Usage
public void withdraw(double amount) throws InsufficientBalanceException {
    if (balance < amount) {
        throw new InsufficientBalanceException("Insufficient balance");
    }
    balance -= amount;
}
```

### Concurrency Considerations
- Use synchronized blocks for thread safety
- Consider using concurrent collections (ConcurrentHashMap)
- Be aware of deadlock scenarios
- Use volatile for visibility across threads

---

## Interview Tips

### Approach to LLD Problems

1. **Clarify Requirements** (5 minutes)
   - Ask about scale, users, features
   - Identify core vs optional features
   - Clarify constraints

2. **Define Core Entities** (5-10 minutes)
   - Identify main classes
   - Define relationships
   - List attributes and methods

3. **Apply Design Principles** (10 minutes)
   - Check SOLID principles
   - Identify applicable design patterns
   - Think about extensibility

4. **Draw Class Diagram** (5 minutes)
   - Show relationships
   - Mark important methods

5. **Discuss Edge Cases** (5 minutes)
   - Concurrency issues
   - Error handling
   - Scalability

6. **Write Code** (Remaining time)
   - Start with interfaces
   - Implement key classes
   - Show important methods

### Common Interview Questions

1. "Why did you choose this design pattern?"
2. "How would you handle concurrent access?"
3. "How would you extend this to support X feature?"
4. "What are the trade-offs of your design?"
5. "How would this scale?"

### Red Flags to Avoid
- Jumping to code without design
- Not asking clarifying questions
- Ignoring SOLID principles
- Overengineering simple problems
- Not considering edge cases
- Poor naming conventions

### What Interviewers Look For
- **Problem understanding**: Did you ask the right questions?
- **Design thinking**: Clean, extensible architecture
- **Code quality**: Readable, maintainable code
- **Communication**: Can you explain your choices?
- **Trade-offs**: Do you understand pros/cons?

---

## Additional Resources

### Books
- "Head First Design Patterns" by Freeman et al.
- "Clean Code" by Robert C. Martin
- "Effective Java" by Joshua Bloch

### Practice Platforms
- LeetCode (System Design section)
- InterviewBit
- educative.io

### Key Takeaways

1. **Master SOLID principles** - They're foundational
2. **Know 10-15 design patterns well** - Focus on commonly used ones
3. **Practice common problems** - Parking lot, Library, Elevator, etc.
4. **Think extensibility** - How would your design adapt to new requirements?
5. **Communication is key** - Explain your thought process clearly

---

Good luck with your interview preparation! Remember, LLD is about demonstrating clean, maintainable, and extensible design thinking.
