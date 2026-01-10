package Multithreading.ExplicitLocks.HandOverHand;

public class DoubleLinkedList {

    Node head;
    Node tail;
    public DoubleLinkedList() {
        this.head = new Node(Integer.MAX_VALUE);

        this.tail = new Node(Integer.MIN_VALUE);
        head.setNext(tail);
        tail.setPrevious(head);
        head.setPrevious(null);
        tail.setNext(null);
    }
    public  void insert(int value) {

        Node curr = head, next = null;
        curr.getLock().lock();
        while(true){
            next = curr.getNext();
            next.getLock().lock();
            if(next.getData() <= value){
                Node newNode = new Node(value);
                newNode.setNext(next);
                newNode.setPrevious(curr);
                curr.setNext(newNode);
                next.setPrevious(newNode);
                break;
            }
            curr.getLock().unlock();
            curr = next;
        }
        curr.getLock().unlock();
        next.getLock().unlock();
    }


    public void display() {
        Node curr = head;
        while(curr != null) {
            System.out.print(curr.getData() + " ");
            curr = curr.getNext();
        }
    }
}
