package multithreading.basics.threadcreation;

/**
 * Comprehensive examples of thread creation in Java
 * Demonstrates different ways to create and manage threads
 */
public class ThreadCreationExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Thread Creation Examples ===\n");
        
        // Example 1: Extending Thread class
        demonstrateThreadExtension();
        
        // Example 2: Implementing Runnable interface
        demonstrateRunnableImplementation();
        
        // Example 3: Lambda expressions
        demonstrateLambdaThreads();
        
        // Example 4: Thread with parameters
        demonstrateThreadWithParameters();
        
        // Example 5: Thread lifecycle
        demonstrateThreadLifecycle();
        
        System.out.println("=== All Examples Completed ===");
    }
    
    /**
     * Example 1: Creating threads by extending Thread class
     */
    private static void demonstrateThreadExtension() {
        System.out.println("1. Thread Creation by Extending Thread Class");
        System.out.println("=============================================");
        
        Thread thread1 = new MyThread("Thread-1");
        Thread thread2 = new MyThread("Thread-2");
        
        thread1.start();
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * Example 2: Creating threads by implementing Runnable interface
     */
    private static void demonstrateRunnableImplementation() {
        System.out.println("2. Thread Creation by Implementing Runnable");
        System.out.println("===========================================");
        
        Runnable task1 = new MyRunnable("Task-1");
        Runnable task2 = new MyRunnable("Task-2");
        
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        
        thread1.start();
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * Example 3: Creating threads using Lambda expressions
     */
    private static void demonstrateLambdaThreads() {
        System.out.println("3. Thread Creation using Lambda Expressions");
        System.out.println("===========================================");
        
        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Lambda Thread-1: Count " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Lambda Thread-2: Count " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        
        thread1.start();
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * Example 4: Thread with parameters
     */
    private static void demonstrateThreadWithParameters() {
        System.out.println("4. Thread Creation with Parameters");
        System.out.println("===================================");
        
        Thread thread1 = new Thread(new ParameterizedTask("Worker-1", 3));
        Thread thread2 = new Thread(new ParameterizedTask("Worker-2", 5));
        
        thread1.start();
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    /**
     * Example 5: Thread lifecycle demonstration
     */
    private static void demonstrateThreadLifecycle() {
        System.out.println("5. Thread Lifecycle Demonstration");
        System.out.println("=================================");
        
        Thread lifecycleThread = new Thread(() -> {
            System.out.println("Thread State: " + Thread.currentThread().getState());
            System.out.println("Thread Name: " + Thread.currentThread().getName());
            System.out.println("Thread Priority: " + Thread.currentThread().getPriority());
            System.out.println("Thread ID: " + Thread.currentThread().getId());
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("Thread execution completed");
        });
        
        System.out.println("Before start - State: " + lifecycleThread.getState());
        lifecycleThread.start();
        System.out.println("After start - State: " + lifecycleThread.getState());
        
        try {
            lifecycleThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("After completion - State: " + lifecycleThread.getState());
        System.out.println();
    }
}

/**
 * Custom thread class extending Thread
 */
class MyThread extends Thread {
    private final String threadName;
    
    public MyThread(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        System.out.println("Starting " + threadName);
        
        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println(threadName + ": Count " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " interrupted");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Exiting " + threadName);
    }
}

/**
 * Custom runnable class implementing Runnable
 */
class MyRunnable implements Runnable {
    private final String taskName;
    
    public MyRunnable(String name) {
        this.taskName = name;
    }
    
    @Override
    public void run() {
        System.out.println("Starting " + taskName);
        
        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println(taskName + ": Count " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(taskName + " interrupted");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Exiting " + taskName);
    }
}

/**
 * Parameterized task class
 */
class ParameterizedTask implements Runnable {
    private final String workerName;
    private final int workCount;
    
    public ParameterizedTask(String name, int count) {
        this.workerName = name;
        this.workCount = count;
    }
    
    @Override
    public void run() {
        System.out.println("Starting " + workerName + " with " + workCount + " tasks");
        
        try {
            for (int i = 1; i <= workCount; i++) {
                System.out.println(workerName + ": Processing task " + i + "/" + workCount);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(workerName + " interrupted");
            Thread.currentThread().interrupt();
        }
        
        System.out.println(workerName + " completed all tasks");
    }
}
