package Multithreading.ExplicitLocks.HandOverHand;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {

    private int value;
    private Node next;
    private Node previous;
    private Lock lock;


    public Node(int data) {
        this.value = data;
        this.next = null;
        this.previous = null;
        this.lock = new ReentrantLock();
    }

    public int getData() {
        return value;
    }
    public Node getNext() {
        return next;
    }
    public Node getPrevious() {
        return previous;
    }
    public void setNext(Node next) {
        this.next = next;
    }
    public void setPrevious(Node previous) {
        this.previous = previous;
    }
    public Lock getLock() {
        return lock;
    }
}
