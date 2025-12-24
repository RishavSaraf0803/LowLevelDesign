
public class Main {
    public static void main(String[] args) {
        SimpleLock lock = new SimpleLock();
        Writer worker1 = new Writer(lock, 1);
        Writer worker2 = new Writer(lock, 2);
        worker1.start();
        worker2.start();
        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread: End of program");
    }   
}
