# Elevator System - Low Level Design

## Table of Contents
1. [Review of Current Implementation](#review-of-current-implementation)
2. [Requirements](#requirements)
3. [Core Components](#core-components)
4. [Class Diagram](#class-diagram)
5. [Design Patterns Used](#design-patterns-used)
6. [Scheduling Algorithms](#scheduling-algorithms)
7. [Sequence Diagrams](#sequence-diagrams)
8. [Complete Implementation Guide](#complete-implementation-guide)

---

## Review of Current Implementation

### What You Have
- ✅ Basic `Floor` class with level tracking
- ✅ `Gate` class with open/close functionality
- ✅ `Elevator` class with basic movement
- ✅ Simple floor navigation

### What's Missing (Critical Components)
- ❌ **Request System** - No internal/external button handling
- ❌ **Direction Management** - No UP/DOWN/IDLE states
- ❌ **Request Queue** - No scheduling or prioritization
- ❌ **Elevator Controller** - No multi-elevator management
- ❌ **Dispatcher** - No algorithm to assign requests to elevators
- ❌ **Door Management** - Gate is oversimplified
- ❌ **Capacity Management** - No weight/passenger limits
- ❌ **Thread Safety** - No handling of concurrent requests
- ❌ **Emergency Handling** - No emergency stop/alarm

### Issues in Current Design
1. **Tight Coupling**: Elevator directly manages Floor objects
2. **Index Confusion**: Using list index for floor level (floor 5 != index 5 if there are basements)
3. **No Request Abstraction**: No concept of internal vs external requests
4. **Missing State Management**: No proper elevator state transitions
5. **No Separation of Concerns**: Everything in one class

---

## Requirements

### Functional Requirements
1. **Multiple Elevators**: System should handle multiple elevator cars
2. **Request Handling**:
   - External requests: UP/DOWN buttons on each floor
   - Internal requests: Floor selection buttons inside elevator
3. **Smart Dispatching**: Assign requests to optimal elevator
4. **Direction Management**: Elevator should serve requests in its current direction
5. **Capacity Management**: Maximum weight/passenger limit
6. **Door Control**: Automatic door open/close with safety
7. **Emergency Operations**: Emergency stop, alarm, door hold

### Non-Functional Requirements
1. **Thread Safety**: Handle concurrent button presses
2. **Efficiency**: Minimize wait time and travel distance
3. **Scalability**: Easy to add more elevators
4. **Maintainability**: Clear separation of concerns
5. **Extensibility**: Easy to change scheduling algorithms

---

## Core Components

### 1. Enums

#### Direction
```java
public enum Direction {
    UP,
    DOWN,
    IDLE
}
```

#### ElevatorState
```java
public enum ElevatorState {
    IDLE,
    MOVING,
    STOPPED,
    MAINTENANCE,
    EMERGENCY
}
```

#### DoorState
```java
public enum DoorState {
    OPEN,
    CLOSED,
    OPENING,
    CLOSING
}
```

#### RequestType
```java
public enum RequestType {
    INTERNAL,  // Button pressed inside elevator
    EXTERNAL   // UP/DOWN button on floor
}
```

---

## Class Diagram

### Core Data Classes

#### 1. Request
Represents a request to move the elevator
```java
public class Request {
    private int floorNumber;
    private Direction direction;  // null for internal requests
    private RequestType type;
    private long timestamp;

    // Constructor, getters, equals, hashCode
}
```

#### 2. Floor
Represents a building floor
```java
public class Floor {
    private final int floorNumber;
    private ExternalButtonPanel externalPanel;

    // Methods
    + pressUpButton()
    + pressDownButton()
}
```

#### 3. ExternalButtonPanel
Buttons outside elevator on each floor
```java
public class ExternalButtonPanel {
    private Button upButton;
    private Button downButton;
    private int floorNumber;

    + pressUp()
    + pressDown()
}
```

#### 4. InternalButtonPanel
Buttons inside elevator
```java
public class InternalButtonPanel {
    private List<Button> floorButtons;
    private Button emergencyButton;
    private Button doorOpenButton;
    private Button doorCloseButton;
    private int elevatorId;

    + pressFloorButton(int floor)
    + pressEmergency()
    + pressDoorOpen()
    + pressDoorClose()
}
```

#### 5. Button
```java
public class Button {
    private int id;
    private boolean isPressed;
    private ButtonPressListener listener;

    + press()
    + unpress()
}
```

#### 6. Door
Manages elevator door operations
```java
public class Door {
    private DoorState state;
    private Timer doorTimer;

    + open()
    + close()
    + hold()
    + getState()
    - startCloseTimer()
}
```

#### 7. Display
Shows current floor and direction
```java
public class Display {
    private int currentFloor;
    private Direction direction;

    + update(int floor, Direction dir)
    + show()
}
```

---

### Core Logic Classes

#### 8. ElevatorCar
Represents a single elevator car
```java
public class ElevatorCar {
    private int id;
    private int currentFloor;
    private Direction currentDirection;
    private ElevatorState state;
    private Door door;
    private InternalButtonPanel panel;
    private Display display;
    private int capacity;
    private int currentWeight;

    // Request queues
    private PriorityQueue<Integer> upQueue;
    private PriorityQueue<Integer> downQueue;

    // Methods
    + addRequest(Request request)
    + move()
    + stop()
    + openDoor()
    + closeDoor()
    + processNextRequest()
    - shouldStopAtFloor(int floor)
    - changeDirection()
}
```

#### 9. ElevatorController
Controls a single elevator car
```java
public class ElevatorController implements Runnable {
    private ElevatorCar car;
    private Queue<Request> pendingRequests;
    private volatile boolean running;

    + run()
    + addRequest(Request request)
    + stop()
    - processRequests()
    - moveToFloor(int targetFloor)
}
```

#### 10. Dispatcher (Strategy Pattern)
Assigns requests to optimal elevator
```java
public interface DispatcherStrategy {
    ElevatorCar selectElevator(Request request, List<ElevatorCar> elevators);
}

public class NearestCarDispatcher implements DispatcherStrategy {
    // Selects nearest available elevator
}

public class LeastLoadedDispatcher implements DispatcherStrategy {
    // Selects elevator with least requests
}

public class ZoneBasedDispatcher implements DispatcherStrategy {
    // Divides building into zones
}
```

#### 11. ElevatorSystem
Main system managing all elevators
```java
public class ElevatorSystem {
    private List<ElevatorCar> elevators;
    private List<ElevatorController> controllers;
    private List<Floor> floors;
    private DispatcherStrategy dispatcher;
    private RequestProcessor requestProcessor;

    + initialize(int numElevators, int minFloor, int maxFloor)
    + requestElevator(int floor, Direction direction)
    + selectFloor(int elevatorId, int floor)
    + emergencyStop(int elevatorId)
    + shutdown()
}
```

#### 12. RequestProcessor
Processes and queues requests
```java
public class RequestProcessor {
    private BlockingQueue<Request> requestQueue;
    private ElevatorSystem system;

    + submitRequest(Request request)
    + processRequests()
}
```

---

## Design Patterns Used

### 1. Singleton Pattern
- `ElevatorSystem` - Single instance managing entire system

### 2. Strategy Pattern
- `DispatcherStrategy` - Different scheduling algorithms
- Allows switching between FCFS, SCAN, LOOK, etc.

### 3. Observer Pattern
- Button press notifications
- Elevator state change notifications

### 4. State Pattern
- `ElevatorState` - Different states (IDLE, MOVING, STOPPED)
- Door states (OPEN, CLOSED, OPENING, CLOSING)

### 5. Factory Pattern
- Creating elevator cars with configurations

### 6. Command Pattern
- Encapsulating button press actions as commands

---

## Scheduling Algorithms

### 1. FCFS (First Come First Served)
- Simplest approach
- Serve requests in order received
- Not efficient for elevators

### 2. SCAN (Elevator Algorithm)
```
Process:
1. Elevator moves in one direction serving all requests
2. When no more requests in that direction, reverse
3. Continue serving requests in opposite direction
```

### 3. LOOK Algorithm
```
Process:
1. Similar to SCAN but doesn't go to extreme ends
2. Reverses when last request in direction is served
3. More efficient than SCAN
```

### 4. SSTF (Shortest Seek Time First)
```
Process:
1. Serve the closest request first
2. Can cause starvation for far requests
3. Good for minimizing travel time
```

### 5. Destination Dispatch
```
Process:
1. User enters destination before entering elevator
2. System assigns optimal elevator
3. Groups passengers going to same zone
```

---

## Sequence Diagrams

### Scenario 1: External Request (User presses UP on Floor 5)

```
User -> Floor(5).ExternalPanel : pressUp()
ExternalPanel -> RequestProcessor : submitRequest(floor=5, dir=UP, type=EXTERNAL)
RequestProcessor -> Dispatcher : selectElevator(request, elevators)
Dispatcher -> ElevatorSystem : Returns optimal elevator (e.g., Elevator-2)
ElevatorSystem -> ElevatorController(2) : addRequest(request)
ElevatorController(2) -> ElevatorCar(2) : addRequest(request)
ElevatorCar(2) : Adds to upQueue
[Elevator moves to floor 5]
ElevatorCar(2) : Arrives at floor 5
ElevatorCar(2) -> Door : open()
ElevatorCar(2) -> Display : update(5, UP)
```

### Scenario 2: Internal Request (User inside elevator presses Floor 10)

```
User -> InternalPanel : pressFloorButton(10)
InternalPanel -> ElevatorController : addRequest(floor=10, type=INTERNAL)
ElevatorController -> ElevatorCar : addRequest(request)
ElevatorCar : Adds to appropriate queue (upQueue/downQueue)
[Elevator processes queue]
ElevatorCar : Moves to floor 10
ElevatorCar -> Door : open()
```

### Scenario 3: Multiple Elevators

```
User(Floor 5) -> RequestProcessor : UP request
User(Floor 15) -> RequestProcessor : DOWN request
RequestProcessor -> Dispatcher : Find optimal for Floor 5 UP
Dispatcher : Evaluates all elevators
    - Elevator-1: Floor 3, moving UP -> Distance: 2
    - Elevator-2: Floor 20, moving DOWN -> Distance: 15
    - Elevator-3: Floor 10, IDLE -> Distance: 5
Dispatcher : Selects Elevator-1 (shortest distance, same direction)
ElevatorSystem -> ElevatorController(1) : Assign Floor 5 request
[Process Floor 15 DOWN separately]
```

---

## Complete Implementation Guide

### Step 1: Create Enums
Create Direction, ElevatorState, DoorState, RequestType enums

### Step 2: Create Data Models
1. Request class
2. Button class
3. Floor class
4. Door class
5. Display class

### Step 3: Create Button Panels
1. ExternalButtonPanel
2. InternalButtonPanel

### Step 4: Implement ElevatorCar
- Core logic for single elevator
- Request queues (upQueue, downQueue)
- Movement logic
- Door control

### Step 5: Implement ElevatorController
- Runnable for concurrent operation
- Processes requests continuously
- Controls one elevator car

### Step 6: Implement Dispatcher
- Interface for strategy pattern
- Implement NearestCarDispatcher
- Implement other strategies as needed

### Step 7: Implement RequestProcessor
- Handles all incoming requests
- Uses BlockingQueue for thread safety

### Step 8: Implement ElevatorSystem
- Main orchestrator
- Initializes all components
- Provides public API

### Step 9: Add Thread Safety
- Synchronize shared resources
- Use concurrent collections
- Lock management

### Step 10: Testing
- Unit tests for each component
- Integration tests for scenarios
- Load testing with multiple concurrent requests

---

## Key Differences from Your Implementation

| Aspect | Your Implementation | Correct Implementation |
|--------|-------------------|----------------------|
| Request Handling | None | Internal + External requests with queues |
| Direction | None | UP/DOWN/IDLE with proper tracking |
| Multiple Elevators | Single elevator | Multiple elevators with dispatcher |
| Scheduling | Direct movement | SCAN/LOOK algorithm with queues |
| Door Management | Simple Gate | Proper Door with states and timers |
| Thread Safety | None | Concurrent collections and locks |
| Button System | None | Internal and External panels |
| State Management | Basic | Proper state machine |
| Dispatching | N/A | Strategy pattern for algorithms |
| Floor Indexing | List index | Proper floor number mapping |

---

## Sample Usage

```java
public class ElevatorSystemDemo {
    public static void main(String[] args) {
        // Initialize system with 3 elevators, floors 0-20
        ElevatorSystem system = ElevatorSystem.getInstance();
        system.initialize(3, 0, 20);

        // External request: User on floor 5 wants to go UP
        system.requestElevator(5, Direction.UP);

        // User enters elevator (let's say Elevator-2 arrived)
        // User presses floor 15
        system.selectFloor(2, 15);

        // Another user on floor 10 wants to go DOWN
        system.requestElevator(10, Direction.DOWN);

        // Emergency stop for elevator 1
        system.emergencyStop(1);

        // Shutdown system
        system.shutdown();
    }
}
```

---

## Extension Points

### 1. Advanced Features
- Express elevators (skip certain floors)
- VIP mode (priority requests)
- Floor access control (key card required)
- Peak hour optimization
- Predictive dispatching (ML-based)

### 2. Monitoring
- Performance metrics (wait time, travel time)
- Real-time dashboard
- Alert system for maintenance

### 3. Energy Optimization
- Sleep mode for idle elevators
- Group control for energy efficiency

---

## Conclusion

Your current implementation is a good starting point but lacks several critical components for a production-ready elevator system. The key additions needed are:

1. Request handling system (internal/external)
2. Proper scheduling algorithms (SCAN/LOOK)
3. Multi-elevator management with dispatcher
4. Thread-safe concurrent operations
5. State management for elevators and doors
6. Button panel abstractions
7. Separation of concerns (Controller, Dispatcher, System)

This LLD provides a comprehensive blueprint for building a scalable, efficient, and maintainable elevator system.
