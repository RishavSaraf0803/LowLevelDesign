# Tic Tac Toe Game Implementation

A comprehensive, object-oriented implementation of the classic Tic Tac Toe game following clean architecture principles and design patterns.

## 📁 Project Structure

```
src/TicTacToe/
├── apis/                    # API interfaces and implementations
│   ├── GameAPI.java        # Main game API interface
│   └── TicTacToeGameAPI.java # Concrete API implementation
├── data/                    # Data models and enums
│   ├── CellValue.java      # Enum for cell values (X, O, EMPTY)
│   ├── GameStatus.java     # Enum for game states
│   ├── Move.java          # Move data model
│   └── Position.java      # Position data model
├── game/                   # Core game logic
│   ├── Board.java         # Game board implementation
│   └── TicTacToeGame.java # Main game controller
├── managers/              # Game management
│   └── GameManager.java  # Overall game flow manager
├── observers/            # Observer pattern implementation
│   ├── GameObserver.java # Observer interface
│   └── ConsoleGameObserver.java # Console observer
├── players/              # Player implementations
│   ├── Player.java       # Abstract player class
│   ├── HumanPlayer.java  # Human player implementation
│   └── ComputerPlayer.java # AI player with difficulty levels
├── ui/                   # User interface
│   └── GameDisplay.java  # Display and formatting
├── validators/           # Validation logic
│   └── MoveValidator.java # Move validation
├── tester/              # Test classes
│   └── TicTacToeTester.java # Comprehensive test suite
├── TicTacToeMain.java   # Main entry point
└── README.md           # This file
```

## 🎯 Design Patterns Used

### 1. **Observer Pattern**
- **Location**: `observers/`
- **Purpose**: Notify observers about game events
- **Implementation**: `GameObserver` interface with `ConsoleGameObserver`

### 2. **Strategy Pattern**
- **Location**: `players/ComputerPlayer.java`
- **Purpose**: Different AI difficulty levels
- **Implementation**: Easy, Medium, Hard strategies

### 3. **Template Method Pattern**
- **Location**: `players/Player.java`
- **Purpose**: Common player interface with abstract `getNextMove()`
- **Implementation**: Abstract base class with concrete implementations

### 4. **Factory Pattern** (Implied)
- **Location**: `managers/GameManager.java`
- **Purpose**: Create different player types based on user choice
- **Implementation**: Player creation methods

### 5. **Command Pattern** (Implied)
- **Location**: `apis/GameAPI.java`
- **Purpose**: Encapsulate game operations
- **Implementation**: API methods as commands

## 🚀 Features

### **Game Modes**
1. **Human vs Human**: Two human players
2. **Human vs Computer**: Human vs AI with difficulty levels
3. **Computer vs Computer**: AI vs AI for testing

### **AI Difficulty Levels**
- **Easy**: Random moves
- **Medium**: 70% smart moves, 30% random
- **Hard**: Strategic moves (win, block, center, corners)

### **Game Features**
- ✅ Move validation
- ✅ Win condition checking
- ✅ Draw detection
- ✅ Move history tracking
- ✅ Real-time game updates
- ✅ Clean console interface
- ✅ Comprehensive error handling

## 🎮 How to Play

### **Running the Game**
```bash
# Compile
javac -d ../bin src/TicTacToe/**/*.java

# Run main game
java -cp ../bin TicTacToe.TicTacToeMain

# Run tests
java -cp ../bin TicTacToe.tester.TicTacToeTester
```

### **Game Controls**
- Enter moves as `row column` (e.g., `1 2`)
- Valid positions: 0-2 for both row and column
- First to get 3 in a row wins!

### **Example Game Flow**
```
========================================
    Welcome to Tic Tac Toe Game!      
========================================

Choose game mode:
1. Human vs Human
2. Human vs Computer
3. Computer vs Computer

Enter your choice (1-3): 2
Enter your name: Alice
Choose computer difficulty:
1. Easy
2. Medium
3. Hard
Enter difficulty (1-3): 2

Alice (X), enter your move (row column): 1 1
Computer (O) is thinking...
```

## 🏗️ Architecture Overview

### **Core Components**

#### **1. Data Layer** (`data/`)
- **CellValue**: Enum for X, O, EMPTY
- **GameStatus**: Game state management
- **Position**: 2D coordinate system
- **Move**: Move with timestamp and player

#### **2. Game Logic** (`game/`)
- **Board**: 3x3 grid with win/draw detection
- **TicTacToeGame**: Main game controller with observer pattern

#### **3. Player System** (`players/`)
- **Player**: Abstract base class
- **HumanPlayer**: Console input player
- **ComputerPlayer**: AI with multiple difficulty levels

#### **4. Management** (`managers/`)
- **GameManager**: Overall game flow and user interaction

#### **5. API Layer** (`apis/`)
- **GameAPI**: Interface for game operations
- **TicTacToeGameAPI**: Concrete implementation

## 🧪 Testing

### **Test Coverage**
- ✅ Basic game functionality
- ✅ Computer vs Computer gameplay
- ✅ API integration testing
- ✅ Move validation
- ✅ Win condition detection
- ✅ Draw detection

### **Running Tests**
```bash
java -cp ../bin TicTacToe.tester.TicTacToeTester
```

## 🔧 Extensibility

### **Adding New Features**
1. **New Player Types**: Extend `Player` class
2. **New AI Strategies**: Implement in `ComputerPlayer`
3. **New Observers**: Implement `GameObserver` interface
4. **New Game Modes**: Extend `GameManager`
5. **New Validators**: Implement validation logic

### **Design Principles**
- **Single Responsibility**: Each class has one purpose
- **Open/Closed**: Open for extension, closed for modification
- **Dependency Inversion**: Depend on abstractions
- **Interface Segregation**: Small, focused interfaces
- **Clean Architecture**: Clear separation of concerns

## 📚 Learning Objectives

This implementation demonstrates:
- **Object-Oriented Design**: Classes, inheritance, polymorphism
- **Design Patterns**: Observer, Strategy, Template Method
- **Clean Architecture**: Separation of concerns
- **Error Handling**: Robust error management
- **Testing**: Comprehensive test coverage
- **Documentation**: Clear, maintainable code

## 🎯 Future Enhancements

### **Potential Improvements**
1. **GUI Interface**: Swing/JavaFX implementation
2. **Network Play**: Multiplayer over network
3. **Tournament Mode**: Multiple games with scoring
4. **Advanced AI**: Minimax algorithm implementation
5. **Game Statistics**: Win/loss tracking
6. **Custom Board Sizes**: NxN Tic Tac Toe
7. **Sound Effects**: Audio feedback
8. **Save/Load Games**: Game state persistence

This implementation serves as an excellent example of clean, maintainable Java code with proper design patterns and architecture principles!
