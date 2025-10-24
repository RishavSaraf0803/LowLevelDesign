package Multithreading.NestedMonitorLockout;

public class Main {

    public static void main(String[] args) {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        Waiter waiter = new Waiter(lock1, lock2);
        Notifier notifier = new Notifier(lock1, lock2);

        Thread waiterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                waiter.run();
            }
        });

        Thread notifierThread = new Thread(notifier);

        waiterThread.start();

        // Ensure waiter starts and waits before notifier notifies
        try {
            Thread.sleep(100); // Small delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        notifierThread.start();

        try {
            waiterThread.join();
            notifierThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
