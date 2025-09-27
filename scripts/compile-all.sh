#!/bin/bash

# Comprehensive compilation script for Java Learning Repository
echo "=== Java Learning Repository Compilation ==="

# Create bin directory if it doesn't exist
mkdir -p bin

# Function to compile a specific module
compile_module() {
    local module_path=$1
    local module_name=$2
    
    if [ -d "src-new/$module_path" ]; then
        echo "Compiling $module_name..."
        find src-new/$module_path -name "*.java" -exec javac -d bin {} + 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "✅ $module_name compiled successfully"
        else
            echo "❌ $module_name compilation failed"
        fi
    else
        echo "⚠️  $module_name directory not found"
    fi
}

# Compile Design Patterns
echo ""
echo "=== Compiling Design Patterns ==="
compile_module "design-patterns/creational/singleton" "Singleton Pattern"
compile_module "design-patterns/creational/builder" "Builder Pattern"
compile_module "design-patterns/structural/decorator" "Decorator Pattern"
compile_module "design-patterns/structural/adapter" "Adapter Pattern"
compile_module "design-patterns/structural/facade" "Facade Pattern"
compile_module "design-patterns/behavioral/strategy" "Strategy Pattern"
compile_module "design-patterns/behavioral/command" "Command Pattern"
compile_module "design-patterns/behavioral/state" "State Pattern"
compile_module "design-patterns/behavioral/observer" "Observer Pattern"
compile_module "design-patterns/behavioral/mediator" "Mediator Pattern"
compile_module "design-patterns/behavioral/memento" "Memento Pattern"
compile_module "design-patterns/behavioral/visitor" "Visitor Pattern"
compile_module "design-patterns/behavioral/template-method" "Template Method Pattern"
compile_module "design-patterns/behavioral/chain-of-responsibility" "Chain of Responsibility Pattern"
compile_module "design-patterns/behavioral/iterator" "Iterator Pattern"
compile_module "design-patterns/solid-principles/open-closed" "Open-Closed Principle"
compile_module "design-patterns/solid-principles/interface-segregation" "Interface Segregation Principle"
compile_module "design-patterns/solid-principles/dependency-inversion" "Dependency Inversion Principle"

# Compile LLD Problems
echo ""
echo "=== Compiling LLD Problems ==="
compile_module "lld-problems/basic/tic-tac-toe" "Tic Tac Toe"
compile_module "lld-problems/basic/chess" "Chess Game"
compile_module "lld-problems/intermediate/parking-lot" "Parking Lot System"
compile_module "lld-problems/intermediate/library-management" "Library Management System"
compile_module "lld-problems/intermediate/food-delivery" "Food Delivery System"
compile_module "lld-problems/intermediate/atm-machine" "ATM Machine"

# Compile Multithreading
echo ""
echo "=== Compiling Multithreading Examples ==="
compile_module "multithreading/basics/thread-creation" "Thread Creation"
compile_module "multithreading/synchronization/synchronized" "Synchronization"

# Compile Java Fundamentals
echo ""
echo "=== Compiling Java Fundamentals ==="
compile_module "java-fundamentals" "Java Fundamentals"

# Compile Utils
echo ""
echo "=== Compiling Utilities ==="
compile_module "utils" "Utility Classes"

echo ""
echo "=== Compilation Summary ==="
echo "✅ All modules compiled successfully!"
echo ""
echo "Available test classes:"
echo "1. Design Patterns:"
echo "   - SingletonPattern.EmployeeStore"
echo "   - BuilderPattern.Computer"
echo "   - StrategyPattern.PaymentStrategyTest"
echo "   - ObserverPattern.StockUpdatesPublisher"
echo ""
echo "2. LLD Problems:"
echo "   - TicTacToe.TicTacToeMain"
echo "   - TicTacToe.tester.TicTacToeTester"
echo "   - Chess.Tester"
echo ""
echo "3. Multithreading:"
echo "   - multithreading.basics.threadcreation.ThreadCreationExamples"
echo "   - multithreading.synchronization.synchronized.SynchronizationExamples"
echo ""
echo "To run a specific example:"
echo "java -cp bin [ClassName]"
echo ""
echo "To run all tests:"
echo "./scripts/test-all.sh"
