package multithreading.synchronization.synchronized;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Comprehensive examples of synchronization in Java
 * Demonstrates synchronized keyword, locks, and thread safety
 */
public class SynchronizationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Synchronization Examples ===\n");
        
        // Example 1: Synchronized method
        demonstrateSynchronizedMethod();
        
        // Example 2: Synchronized block
        demonstrateSynchronizedBlock();
        
        // Example 3: Static synchronization
        demonstrateStaticSynchronization();
        
        // Example 4: Synchronized collection
        demonstrateSynchronizedCollection();
        
        // Example 5: Atomic operations
        demonstrateAtomicOperations();
        
        System.out.println("=== All Examples Completed ===");
    }
    
    /**
     * Example 1: Synchronized method demonstration
     */
    private static void demonstrateSynchronizedMethod() {
        System.out.println("1. Synchronized Method Example");
        System.out.println("===============================");
        
        Counter counter = new Counter();
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final count: " + counter.getCount());
        System.out.println();
    }
    
    /**
     * Example 2: Synchronized block demonstration
     */
    private static void demonstrateSynchronizedBlock() {
        System.out.println("2. Synchronized Block Example");
        System.out.println("=============================");
        
        BankAccount account = new BankAccount(1000);
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.withdraw(100);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account.deposit(50);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final balance: " + account.getBalance());
        System.out.println();
    }
    
    /**
     * Example 3: Static synchronization demonstration
     */
    private static void demonstrateStaticSynchronization() {
        System.out.println("3. Static Synchronization Example");
        System.out.println("==================================");
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                StaticCounter.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                StaticCounter.increment();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final static count: " + StaticCounter.getCount());
        System.out.println();
    }
    
    /**
     * Example 4: Synchronized collection demonstration
     */
    private static void demonstrateSynchronizedCollection() {
        System.out.println("4. Synchronized Collection Example");
        System.out.println("===================================");
        
        SharedList sharedList = new SharedList();
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedList.add("Thread-1-" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedList.add("Thread-2-" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final list size: " + sharedList.size());
        System.out.println("List contents: " + sharedList.getList());
        System.out.println();
    }
    
    /**
     * Example 5: Atomic operations demonstration
     */
    private static void demonstrateAtomicOperations() {
        System.out.println("5. Atomic Operations Example");
        System.out.println("=============================");
        
        AtomicCounter atomicCounter = new AtomicCounter();
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicCounter.increment();
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicCounter.increment();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final atomic count: " + atomicCounter.getCount());
        System.out.println();
    }
}

/**
 * Counter class with synchronized method
 */
class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}

/**
 * Bank account class with synchronized blocks
 */
class BankAccount {
    private double balance;
    
    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) {
        synchronized (this) {
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Withdrawn: " + amount + ", Balance: " + balance);
            } else {
                System.out.println("Insufficient funds for withdrawal: " + amount);
            }
        }
    }
    
    public void deposit(double amount) {
        synchronized (this) {
            balance += amount;
            System.out.println("Deposited: " + amount + ", Balance: " + balance);
        }
    }
    
    public synchronized double getBalance() {
        return balance;
    }
}

/**
 * Static counter class with static synchronization
 */
class StaticCounter {
    private static int count = 0;
    
    public static synchronized void increment() {
        count++;
        System.out.println("Static count: " + count);
    }
    
    public static synchronized int getCount() {
        return count;
    }
}

/**
 * Shared list class with synchronization
 */
class SharedList {
    private final java.util.List<String> list = new java.util.ArrayList<>();
    
    public synchronized void add(String item) {
        list.add(item);
        System.out.println("Added: " + item + ", Size: " + list.size());
    }
    
    public synchronized int size() {
        return list.size();
    }
    
    public synchronized java.util.List<String> getList() {
        return new java.util.ArrayList<>(list);
    }
}

/**
 * Atomic counter class using AtomicInteger
 */
class AtomicCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
}
