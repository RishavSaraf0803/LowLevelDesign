#!/bin/bash

# Comprehensive test script for Java Learning Repository
echo "=== Java Learning Repository Test Suite ==="

# Function to run a test class
run_test() {
    local class_name=$1
    local test_name=$2
    
    echo "Running $test_name..."
    java -cp bin $class_name 2>/dev/null
    if [ $? -eq 0 ]; then
        echo "✅ $test_name passed"
    else
        echo "❌ $test_name failed"
    fi
    echo ""
}

# Test Design Patterns
echo "=== Testing Design Patterns ==="
run_test "SingletonPattern.EmployeeStore" "Singleton Pattern Test"
run_test "BuilderPattern.Computer" "Builder Pattern Test"
run_test "StrategyPattern.PaymentStrategyTest" "Strategy Pattern Test"
run_test "ObserverPattern.StockUpdatesPublisher" "Observer Pattern Test"

# Test LLD Problems
echo "=== Testing LLD Problems ==="
run_test "TicTacToe.tester.TicTacToeTester" "Tic Tac Toe Test"
run_test "Chess.Tester" "Chess Game Test"

# Test Multithreading
echo "=== Testing Multithreading ==="
run_test "multithreading.basics.threadcreation.ThreadCreationExamples" "Thread Creation Test"
run_test "multithreading.synchronization.synchronized.SynchronizationExamples" "Synchronization Test"

echo "=== Test Suite Completed ==="
