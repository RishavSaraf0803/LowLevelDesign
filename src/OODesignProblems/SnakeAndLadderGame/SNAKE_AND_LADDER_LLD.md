# Snake and Ladder Game - Low Level Design

## Table of Contents
1. [Review of Current Implementation](#review-of-current-implementation)
2. [Game Overview](#game-overview)
3. [Requirements](#requirements)
4. [Core Components](#core-components)
5. [Class Diagram](#class-diagram)
6. [Design Patterns Used](#design-patterns-used)
7. [Game Flow](#game-flow)
8. [Sequence Diagrams](#sequence-diagrams)
9. [Complete Implementation Guide](#complete-implementation-guide)
10. [Advanced Features](#advanced-features)

---

## Review of Current Implementation

### What You Have
- ✅ Good structure with `Game` interface
- ✅ `Board` abstract class with inheritance
- ✅ `BoardFactory` for Factory pattern
- ✅ `Dice` class with roll logic
- ✅ `Player` class with basic attributes
- ✅ `CellState` enum
- ✅ Queue for turn management
- ✅ Game state management methods in interface

### What's Missing
- ❌ **Snake and Ladder entities** - No Snake/Ladder classes
- ❌ **Cell implementation** - Cell class is empty
- ❌ **Jump logic** - No snake/ladder movement
- ❌ **Game loop** - startGame() and other methods are empty
- ❌ **Win condition** - No logic to determine winner
- ❌ **Player movement** - No movePlayer() logic
- ❌ **Board initialization** - prepareBoard() not implemented
- ❌ **Import statements** - Missing imports (List, ArrayList, Queue, Map, HashMap)
- ❌ **Board constructor** - Abstract Board missing constructor

### Issues in Current Design
1. **Empty Cell class** - Should contain position and jump information
2. **CellState approach** - Cell shouldn't have state; Snake/Ladder should be separate entities
3. **Missing Jump class** - Need abstraction for Snake and Ladder
4. **No validation** - No checks for valid dice rolls, positions
5. **DotColor enum** - Not used properly in design
6. **Board.prepareBoard()** - Method called but not defined in abstract class
7. **playerPositions Map** - Redundant as Player already has position field

---

## Game Overview

### Classic Rules
1. **Board**: 10x10 grid (100 cells), numbered 1-100
2. **Players**: 2-4 players, each with a token
3. **Dice**: Standard 6-sided dice
4. **Start**: All players start at position 0 (before cell 1)
5. **Win**: First player to reach exactly cell 100 wins
6. **Exact Landing**: If dice roll overshoots 100, player stays in place
7. **Snakes**: Move player down from head to tail
8. **Ladders**: Move player up from bottom to top

### Turn Flow
1. Player rolls dice
2. Move token forward by dice value
3. Check if landed on snake/ladder
4. If snake/ladder, move to destination
5. Check if player won (reached 100)
6. Next player's turn

---

## Requirements

### Functional Requirements
1. **Board Setup**:
   - Configurable board size (default 10x10 = 100 cells)
   - Random or fixed snake/ladder placement
   - No overlapping snakes/ladders
   - Validate positions (start < end for ladder, start > end for snake)

2. **Player Management**:
   - 2-4 players
   - Each player has unique name and token color
   - Track current position for each player
   - Maintain turn order

3. **Game Mechanics**:
   - Roll dice (1-6)
   - Move player by dice value
   - Handle snake/ladder jumps
   - Detect win condition (exactly reach 100)
   - Handle overshoot (stay in place if roll > remaining cells)

4. **Game States**:
   - NOT_STARTED
   - IN_PROGRESS
   - PAUSED
   - FINISHED

5. **Additional Features**:
   - Undo/Redo moves
   - Save/Load game
   - Display board state
   - Show game history

### Non-Functional Requirements
1. **Extensibility**: Easy to add new rules (e.g., special cells)
2. **Maintainability**: Clean separation of concerns
3. **Testability**: Easy to unit test components
4. **Configurability**: Configurable board size, snake/ladder count

---

## Core Components

### 1. Enums

#### GameState
```java
public enum GameState {
    NOT_STARTED,
    IN_PROGRESS,
    PAUSED,
    FINISHED
}
```

#### PlayerColor
```java
public enum PlayerColor {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    PURPLE,
    ORANGE;

    public String getColorCode() {
        // Returns ANSI color codes for console display
    }
}
```

---

## Class Diagram

### Core Data Classes

#### 1. Cell
Represents a single cell on the board
```java
public class Cell {
    private final int position;
    private Jump jump;  // null if no snake/ladder

    public Cell(int position) {
        this.position = position;
        this.jump = null;
    }

    public int getPosition() {
        return position;
    }

    public boolean hasJump() {
        return jump != null;
    }

    public Jump getJump() {
        return jump;
    }

    public void setJump(Jump jump) {
        this.jump = jump;
    }
}
```

#### 2. Jump (Abstract)
Base class for Snake and Ladder
```java
public abstract class Jump {
    protected int start;
    protected int end;

    public Jump(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public abstract String getType();
}
```

#### 3. Snake
```java
public class Snake extends Jump {
    public Snake(int head, int tail) {
        super(head, tail);
        if (head <= tail) {
            throw new IllegalArgumentException(
                "Snake head must be greater than tail"
            );
        }
    }

    @Override
    public String getType() {
        return "Snake";
    }
}
```

#### 4. Ladder
```java
public class Ladder extends Jump {
    public Ladder(int bottom, int top) {
        super(bottom, top);
        if (bottom >= top) {
            throw new IllegalArgumentException(
                "Ladder bottom must be less than top"
            );
        }
    }

    @Override
    public String getType() {
        return "Ladder";
    }
}
```

#### 5. Player
```java
public class Player {
    private final String id;
    private final String name;
    private final PlayerColor color;
    private int position;

    public Player(String name, PlayerColor color) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.color = color;
        this.position = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + " (" + color + ")";
    }
}
```

#### 6. Dice
```java
public class Dice {
    private final int minValue;
    private final int maxValue;
    private final int numberOfDice;
    private final Random random;

    public Dice() {
        this(1, 6, 1);
    }

    public Dice(int minValue, int maxValue, int numberOfDice) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numberOfDice = numberOfDice;
        this.random = new Random();
    }

    public int roll() {
        int sum = 0;
        for (int i = 0; i < numberOfDice; i++) {
            sum += random.nextInt(maxValue - minValue + 1) + minValue;
        }
        return sum;
    }

    public int getMaxValue() {
        return maxValue * numberOfDice;
    }
}
```

#### 7. Move
Represents a single move in the game (for undo/redo)
```java
public class Move {
    private final Player player;
    private final int fromPosition;
    private final int toPosition;
    private final int diceValue;
    private final boolean hadJump;
    private final Long timestamp;

    public Move(Player player, int fromPosition, int toPosition,
                int diceValue, boolean hadJump) {
        this.player = player;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.diceValue = diceValue;
        this.hadJump = hadJump;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
}
```

---

### Core Logic Classes

#### 8. Board
```java
public class Board {
    private final int size;
    private final Cell[] cells;
    private final List<Snake> snakes;
    private final List<Ladder> ladders;

    public Board(int size) {
        this.size = size;
        this.cells = new Cell[size + 1];  // index 0 unused, 1-100 used
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 1; i <= size; i++) {
            cells[i] = new Cell(i);
        }
    }

    public void addSnake(Snake snake) {
        validateJump(snake);
        snakes.add(snake);
        cells[snake.getStart()].setJump(snake);
    }

    public void addLadder(Ladder ladder) {
        validateJump(ladder);
        ladders.add(ladder);
        cells[ladder.getStart()].setJump(ladder);
    }

    private void validateJump(Jump jump) {
        if (jump.getStart() < 1 || jump.getStart() > size ||
            jump.getEnd() < 1 || jump.getEnd() > size) {
            throw new IllegalArgumentException("Jump positions out of bounds");
        }
        if (cells[jump.getStart()].hasJump()) {
            throw new IllegalArgumentException(
                "Cell " + jump.getStart() + " already has a jump"
            );
        }
    }

    public Cell getCell(int position) {
        if (position < 0 || position > size) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        return cells[position];
    }

    public int getSize() {
        return size;
    }

    public List<Snake> getSnakes() {
        return new ArrayList<>(snakes);
    }

    public List<Ladder> getLadders() {
        return new ArrayList<>(ladders);
    }
}
```

#### 9. BoardBuilder
```java
public class BoardBuilder {
    private int size = 100;
    private List<Snake> snakes = new ArrayList<>();
    private List<Ladder> ladders = new ArrayList<>();

    public BoardBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public BoardBuilder addSnake(int head, int tail) {
        snakes.add(new Snake(head, tail));
        return this;
    }

    public BoardBuilder addLadder(int bottom, int top) {
        ladders.add(new Ladder(bottom, top));
        return this;
    }

    public BoardBuilder withRandomSnakesAndLadders(int numSnakes, int numLadders) {
        Random random = new Random();
        Set<Integer> usedPositions = new HashSet<>();

        // Add random snakes
        for (int i = 0; i < numSnakes; i++) {
            int head, tail;
            do {
                head = random.nextInt(size - 20) + 20;  // From 20 to size
                tail = random.nextInt(head - 10) + 1;   // Below head
            } while (usedPositions.contains(head) || usedPositions.contains(tail));

            usedPositions.add(head);
            snakes.add(new Snake(head, tail));
        }

        // Add random ladders
        for (int i = 0; i < numLadders; i++) {
            int bottom, top;
            do {
                bottom = random.nextInt(size - 20) + 1;  // From 1 to size-20
                top = random.nextInt(size - bottom) + bottom + 10;
            } while (usedPositions.contains(bottom) || usedPositions.contains(top));

            usedPositions.add(bottom);
            ladders.add(new Ladder(bottom, top));
        }

        return this;
    }

    public Board build() {
        Board board = new Board(size);
        for (Snake snake : snakes) {
            board.addSnake(snake);
        }
        for (Ladder ladder : ladders) {
            board.addLadder(ladder);
        }
        return board;
    }
}
```

#### 10. SnakeAndLadderGame
```java
public class SnakeAndLadderGame {
    private final Board board;
    private final Dice dice;
    private final Queue<Player> players;
    private final List<Player> allPlayers;
    private final Stack<Move> moveHistory;
    private final Stack<Move> redoStack;
    private GameState state;
    private Player winner;

    public SnakeAndLadderGame(Board board, Dice dice, List<Player> players) {
        if (players.size() < 2 || players.size() > 4) {
            throw new IllegalArgumentException("Game requires 2-4 players");
        }

        this.board = board;
        this.dice = dice;
        this.allPlayers = new ArrayList<>(players);
        this.players = new LinkedList<>(players);
        this.moveHistory = new Stack<>();
        this.redoStack = new Stack<>();
        this.state = GameState.NOT_STARTED;
        this.winner = null;
    }

    public void startGame() {
        if (state != GameState.NOT_STARTED) {
            throw new IllegalStateException("Game already started");
        }
        state = GameState.IN_PROGRESS;
        System.out.println("Game started with " + players.size() + " players!");
        playGame();
    }

    private void playGame() {
        while (state == GameState.IN_PROGRESS) {
            Player currentPlayer = players.poll();
            takeTurn(currentPlayer);

            if (winner != null) {
                state = GameState.FINISHED;
                announceWinner();
                break;
            }

            players.offer(currentPlayer);
        }
    }

    private void takeTurn(Player player) {
        System.out.println("\n" + player.getName() + "'s turn");
        System.out.println("Current position: " + player.getPosition());

        int diceValue = dice.roll();
        System.out.println("Rolled: " + diceValue);

        int newPosition = player.getPosition() + diceValue;

        // Check if overshoots
        if (newPosition > board.getSize()) {
            System.out.println("Overshoot! Staying at " + player.getPosition());
            return;
        }

        // Move to new position
        int oldPosition = player.getPosition();
        player.setPosition(newPosition);
        System.out.println("Moved to: " + newPosition);

        // Check for snake/ladder
        boolean hadJump = false;
        Cell cell = board.getCell(newPosition);
        if (cell.hasJump()) {
            Jump jump = cell.getJump();
            hadJump = true;
            System.out.println("Found " + jump.getType() + "!");
            System.out.println("Moving from " + newPosition + " to " + jump.getEnd());
            player.setPosition(jump.getEnd());
            newPosition = jump.getEnd();
        }

        // Record move
        moveHistory.push(new Move(player, oldPosition, newPosition, diceValue, hadJump));
        redoStack.clear();

        // Check win condition
        if (player.getPosition() == board.getSize()) {
            winner = player;
        }
    }

    public void undoMove() {
        if (moveHistory.isEmpty()) {
            System.out.println("No moves to undo");
            return;
        }

        Move lastMove = moveHistory.pop();
        lastMove.getPlayer().setPosition(lastMove.getFromPosition());
        redoStack.push(lastMove);

        System.out.println("Undid move for " + lastMove.getPlayer().getName());
    }

    public void redoMove() {
        if (redoStack.isEmpty()) {
            System.out.println("No moves to redo");
            return;
        }

        Move moveToRedo = redoStack.pop();
        moveToRedo.getPlayer().setPosition(moveToRedo.getToPosition());
        moveHistory.push(moveToRedo);

        System.out.println("Redid move for " + moveToRedo.getPlayer().getName());
    }

    public void pauseGame() {
        if (state == GameState.IN_PROGRESS) {
            state = GameState.PAUSED;
            System.out.println("Game paused");
        }
    }

    public void resumeGame() {
        if (state == GameState.PAUSED) {
            state = GameState.IN_PROGRESS;
            System.out.println("Game resumed");
            playGame();
        }
    }

    public void restartGame() {
        // Reset all player positions
        for (Player player : allPlayers) {
            player.setPosition(0);
        }

        // Clear history
        moveHistory.clear();
        redoStack.clear();

        // Reset game state
        players.clear();
        players.addAll(allPlayers);
        state = GameState.NOT_STARTED;
        winner = null;

        System.out.println("Game restarted!");
    }

    private void announceWinner() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("GAME OVER!");
        System.out.println(winner.getName() + " wins!");
        System.out.println("=".repeat(50));
    }

    public void displayBoard() {
        // Implementation for visual board display
        System.out.println("\nBoard Configuration:");
        System.out.println("Snakes:");
        for (Snake snake : board.getSnakes()) {
            System.out.println("  " + snake.getStart() + " -> " + snake.getEnd());
        }
        System.out.println("Ladders:");
        for (Ladder ladder : board.getLadders()) {
            System.out.println("  " + ladder.getStart() + " -> " + ladder.getEnd());
        }
    }

    public void displayPlayerPositions() {
        System.out.println("\nPlayer Positions:");
        for (Player player : allPlayers) {
            System.out.println("  " + player.getName() + ": " + player.getPosition());
        }
    }

    // Getters
    public GameState getState() {
        return state;
    }

    public Player getWinner() {
        return winner;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(allPlayers);
    }
}
```

#### 11. GameStateManager (for Save/Load)
```java
public class GameStateManager {

    public static class GameSnapshot {
        private final Map<String, Integer> playerPositions;
        private final List<Move> moveHistory;
        private final GameState state;
        private final long timestamp;

        public GameSnapshot(List<Player> players, List<Move> moveHistory,
                          GameState state) {
            this.playerPositions = new HashMap<>();
            for (Player player : players) {
                playerPositions.put(player.getId(), player.getPosition());
            }
            this.moveHistory = new ArrayList<>(moveHistory);
            this.state = state;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters
    }

    public static GameSnapshot saveGame(SnakeAndLadderGame game) {
        return new GameSnapshot(
            game.getPlayers(),
            new ArrayList<>(game.getMoveHistory()),
            game.getState()
        );
    }

    public static void loadGame(SnakeAndLadderGame game, GameSnapshot snapshot) {
        // Restore player positions
        for (Player player : game.getPlayers()) {
            Integer position = snapshot.getPlayerPositions().get(player.getId());
            if (position != null) {
                player.setPosition(position);
            }
        }

        // Restore move history
        game.getMoveHistory().clear();
        game.getMoveHistory().addAll(snapshot.getMoveHistory());

        System.out.println("Game loaded from snapshot at " +
                         new Date(snapshot.getTimestamp()));
    }
}
```

---

## Design Patterns Used

### 1. Builder Pattern
- `BoardBuilder` - Fluent API for board construction
- Handles complex board setup with snakes and ladders

### 2. Factory Pattern
- `BoardFactory` (you already have this)
- Can create different types of boards (classic, mini, mega)

### 3. Command Pattern
- `Move` class encapsulates player moves
- Enables undo/redo functionality

### 4. Memento Pattern
- `GameSnapshot` - Saves game state for save/load
- Preserves encapsulation

### 5. Strategy Pattern (Extension)
- Can add different dice strategies (loaded dice, double roll, etc.)
- Different win conditions

### 6. Template Method Pattern
- `Jump` abstract class with `getType()` template method
- Snake and Ladder implement specific behavior

---

## Game Flow

### Initialization Flow
```
1. Create Board using BoardBuilder
   - Set size
   - Add snakes and ladders (random or fixed)

2. Create Players
   - 2-4 players with names and colors

3. Create Dice
   - Standard 6-sided or custom

4. Create Game
   - Pass board, dice, and players

5. Start Game
```

### Turn Flow
```
1. Get current player from queue
2. Roll dice
3. Calculate new position (current + dice)
4. Check if overshoot (> 100)
   - If yes: Stay in place
   - If no: Move to new position
5. Check if cell has snake/ladder
   - If yes: Move to destination
6. Record move in history
7. Check win condition (position == 100)
   - If yes: Announce winner, end game
   - If no: Add player back to queue
8. Next player's turn
```

---

## Sequence Diagrams

### Scenario 1: Normal Turn (No Snake/Ladder)

```
Player -> Game : takeTurn()
Game -> Dice : roll()
Dice --> Game : returns 4
Game -> Player : getPosition() [returns 5]
Game : Calculate new position = 5 + 4 = 9
Game -> Player : setPosition(9)
Game -> Board : getCell(9)
Board --> Game : Cell(9) [no jump]
Game -> MoveHistory : push(Move)
Game : Check win condition [false]
Game : Next player's turn
```

### Scenario 2: Landing on Snake

```
Player -> Game : takeTurn()
Game -> Dice : roll()
Dice --> Game : returns 3
Game -> Player : getPosition() [returns 25]
Game : Calculate new position = 25 + 3 = 28
Game -> Player : setPosition(28)
Game -> Board : getCell(28)
Board --> Game : Cell(28) [has Snake to 10]
Game : Display "Found Snake!"
Game -> Player : setPosition(10)
Game -> MoveHistory : push(Move with jump=true)
Game : Check win condition [false]
Game : Next player's turn
```

### Scenario 3: Landing on Ladder

```
Player -> Game : takeTurn()
Game -> Dice : roll()
Dice --> Game : returns 6
Game -> Player : getPosition() [returns 15]
Game : Calculate new position = 15 + 6 = 21
Game -> Player : setPosition(21)
Game -> Board : getCell(21)
Board --> Game : Cell(21) [has Ladder to 42]
Game : Display "Found Ladder!"
Game -> Player : setPosition(42)
Game -> MoveHistory : push(Move with jump=true)
Game : Check win condition [false]
Game : Next player's turn
```

### Scenario 4: Winning Move

```
Player -> Game : takeTurn()
Game -> Dice : roll()
Dice --> Game : returns 3
Game -> Player : getPosition() [returns 97]
Game : Calculate new position = 97 + 3 = 100
Game -> Player : setPosition(100)
Game -> Board : getCell(100)
Board --> Game : Cell(100) [no jump]
Game -> MoveHistory : push(Move)
Game : Check win condition [TRUE]
Game : Set winner = Player
Game : Change state to FINISHED
Game : announceWinner()
```

### Scenario 5: Overshoot

```
Player -> Game : takeTurn()
Game -> Dice : roll()
Dice --> Game : returns 5
Game -> Player : getPosition() [returns 98]
Game : Calculate new position = 98 + 5 = 103
Game : Check if > 100 [TRUE]
Game : Display "Overshoot! Staying at 98"
Game : Player position unchanged
Game : Next player's turn
```

---

## Complete Implementation Guide

### Step 1: Create Enums
```java
// GameState.java
// PlayerColor.java
```

### Step 2: Create Jump Hierarchy
```java
// Jump.java (abstract)
// Snake.java (extends Jump)
// Ladder.java (extends Jump)
```

### Step 3: Create Core Data Classes
```java
// Cell.java - with Jump reference
// Player.java - with all attributes
// Dice.java - with roll logic
// Move.java - for undo/redo
```

### Step 4: Create Board
```java
// Board.java - with cells array
// BoardBuilder.java - for fluent construction
```

### Step 5: Implement Game Logic
```java
// SnakeAndLadderGame.java - main game logic
// Implement: startGame(), takeTurn(), checkWin()
```

### Step 6: Add Undo/Redo
```java
// Implement undoMove() and redoMove()
// Use Stack<Move> for history
```

### Step 7: Add Save/Load
```java
// GameStateManager.java
// Implement saveGame() and loadGame()
```

### Step 8: Create Main Class
```java
// GameRunner.java - demo the game
```

### Step 9: Add Display Methods
```java
// displayBoard() - show snakes and ladders
// displayPlayerPositions()
// Visual board rendering (optional)
```

### Step 10: Testing
```java
// Unit tests for each component
// Integration tests for game scenarios
```

---

## Sample Usage

### Example 1: Simple Game
```java
public class GameRunner {
    public static void main(String[] args) {
        // Build board
        Board board = new BoardBuilder()
            .setSize(100)
            .addSnake(99, 54)
            .addSnake(70, 55)
            .addSnake(52, 42)
            .addSnake(25, 2)
            .addLadder(6, 25)
            .addLadder(11, 40)
            .addLadder(60, 85)
            .addLadder(46, 90)
            .build();

        // Create players
        List<Player> players = Arrays.asList(
            new Player("Alice", PlayerColor.RED),
            new Player("Bob", PlayerColor.BLUE)
        );

        // Create dice
        Dice dice = new Dice();

        // Create and start game
        SnakeAndLadderGame game = new SnakeAndLadderGame(board, dice, players);
        game.displayBoard();
        game.startGame();
    }
}
```

### Example 2: Game with Random Board
```java
public class RandomGameRunner {
    public static void main(String[] args) {
        // Build board with random snakes and ladders
        Board board = new BoardBuilder()
            .setSize(100)
            .withRandomSnakesAndLadders(8, 8)
            .build();

        // Create players
        List<Player> players = Arrays.asList(
            new Player("Charlie", PlayerColor.GREEN),
            new Player("Diana", PlayerColor.YELLOW),
            new Player("Eve", PlayerColor.PURPLE)
        );

        // Create dice
        Dice dice = new Dice();

        // Create game
        SnakeAndLadderGame game = new SnakeAndLadderGame(board, dice, players);

        // Display configuration
        game.displayBoard();

        // Start game
        game.startGame();
    }
}
```

### Example 3: Interactive Game with Save/Load
```java
public class InteractiveGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Setup game
        Board board = new BoardBuilder()
            .withRandomSnakesAndLadders(6, 6)
            .build();

        List<Player> players = new ArrayList<>();
        System.out.print("Enter number of players (2-4): ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        PlayerColor[] colors = PlayerColor.values();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i+1) + " name: ");
            String name = scanner.nextLine();
            players.add(new Player(name, colors[i]));
        }

        Dice dice = new Dice();
        SnakeAndLadderGame game = new SnakeAndLadderGame(board, dice, players);

        game.displayBoard();

        System.out.println("\nPress Enter to roll dice, 'u' to undo, 's' to save...");

        // Game loop would be modified for interactive input
        game.startGame();
    }
}
```

---

## Advanced Features

### 1. Special Cells
```java
public enum CellType {
    NORMAL,
    BOOST,      // Extra dice roll
    PENALTY,    // Skip next turn
    TELEPORT,   // Jump to random cell
    SAFE_ZONE   // Cannot be affected by snakes
}
```

### 2. Power-ups
```java
public class PowerUp {
    private PowerUpType type;
    private int duration;

    public enum PowerUpType {
        DOUBLE_DICE,     // Roll two dice
        IMMUNITY,        // Immune to snakes
        LADDER_BOOST,    // All ladders go 2x distance
        REROLL          // Reroll dice once
    }
}
```

### 3. Multiplayer Modes
- **Competitive**: First to 100 wins
- **Cooperative**: All players must reach 100
- **Race Mode**: Timed game, highest position wins
- **Team Mode**: 2v2, team with first player to 100 wins

### 4. Different Board Sizes
```java
public enum BoardSize {
    MINI(36),      // 6x6
    CLASSIC(100),  // 10x10
    MEGA(144),     // 12x12
    CUSTOM(0);     // User-defined

    private final int cells;
}
```

### 5. AI Players
```java
public class AIPlayer extends Player {
    private DifficultyLevel difficulty;

    public enum DifficultyLevel {
        EASY,    // Random moves
        MEDIUM,  // Slight strategy
        HARD     // Optimal play with probability
    }

    @Override
    public void takeTurn() {
        // AI decision making
    }
}
```

### 6. Statistics Tracking
```java
public class GameStatistics {
    private int totalMoves;
    private int snakesHit;
    private int laddersClimbed;
    private long gameTime;
    private Map<Player, PlayerStats> playerStats;

    public void recordMove(Move move) {
        // Track statistics
    }

    public void displayStats() {
        // Show game statistics
    }
}
```

### 7. Custom Rules
```java
public interface GameRule {
    boolean canMove(Player player, int diceValue);
    int modifyDiceValue(int diceValue);
    boolean shouldSkipTurn(Player player);
}

public class SixRollsAgainRule implements GameRule {
    @Override
    public boolean canMove(Player player, int diceValue) {
        return true;
    }

    @Override
    public int modifyDiceValue(int diceValue) {
        return diceValue;
    }

    @Override
    public boolean shouldSkipTurn(Player player) {
        return false;
    }
}
```

---

## Key Differences from Your Implementation

| Aspect | Your Implementation | Correct Implementation |
|--------|-------------------|----------------------|
| Cell | Empty class | Contains position and Jump reference |
| Snake/Ladder | No classes | Separate Snake and Ladder classes extending Jump |
| Jump abstraction | None | Abstract Jump class with polymorphism |
| CellState enum | Used in Cell | Not needed; use Jump object instead |
| Player position | Has position field | Same, but properly used |
| playerPositions Map | Separate tracking | Removed; use Player.position directly |
| Game loop | Empty methods | Complete implementation |
| Win condition | Not implemented | Check position == board size |
| Overshoot handling | Not implemented | Stay in place if dice > remaining |
| Undo/Redo | Empty methods | Stack-based implementation |
| Move history | Not tracked | Move class with history stack |
| Board initialization | prepareBoard() not defined | Proper cell array initialization |
| Validation | None | Validate jumps, positions, player count |
| Turn management | Queue setup but not used | Proper queue poll/offer pattern |

---

## Testing Checklist

### Unit Tests
- [ ] Cell: position, jump assignment
- [ ] Snake: validation (head > tail)
- [ ] Ladder: validation (bottom < top)
- [ ] Player: position updates
- [ ] Dice: roll within range
- [ ] Board: add snakes/ladders, validation
- [ ] Move: record keeping

### Integration Tests
- [ ] Normal turn flow
- [ ] Landing on snake
- [ ] Landing on ladder
- [ ] Winning move
- [ ] Overshoot handling
- [ ] Undo/redo moves
- [ ] Multiple players
- [ ] Turn order maintenance

### Edge Cases
- [ ] Rolling 6 at position 95 (overshoot)
- [ ] Landing on 100 exactly
- [ ] Snake at position 99
- [ ] Ladder at position 1
- [ ] Multiple undo operations
- [ ] Undo when no history
- [ ] Invalid board configurations
- [ ] Invalid player count (< 2 or > 4)

---

## Conclusion

Your current implementation has a good structure with interfaces and the right pattern ideas, but lacks the core implementation details. The main gaps are:

1. **Missing entities**: Snake, Ladder, and proper Cell implementation
2. **No game logic**: Turn handling, win condition, overshoot check
3. **Empty methods**: All Game interface methods need implementation
4. **No Jump abstraction**: Need polymorphic approach for snakes and ladders
5. **Builder pattern**: BoardBuilder provides cleaner board setup
6. **Move tracking**: Needed for undo/redo functionality

This LLD provides a complete, extensible, and maintainable design for a Snake and Ladder game that follows SOLID principles and implements proper OOP concepts.
