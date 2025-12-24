package Multithreading.ExplicitLocks.HandOverHand;

public class Inserter implements Runnable {

    private int value;
    private DoubleLinkedList list;
    public Inserter(int value,DoubleLinkedList list){
        this.value = value;
        this.list = list;
    }
    public void run(){ list.insert(value); }

}
