package Multithreading.ExplicitLocks.ReadWriteLockWithWriteStarvation;

public class Main {

public static void main(String[] args) {
    ReadWriteLock lock = new ReadWriteLock();
    Store store = new Store(lock);

    Thread writer = new Thread(new Writer(store));

    Thread[] readers = new Thread[10];
    for (int i = 0; i < 10; i++) {
        readers[i] = new Thread(new Reader(store));
    }

    writer.start();
    for (int i = 0; i < 10; i++) {
        readers[i].start();
    }
}

}
