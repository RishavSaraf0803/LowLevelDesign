package IterPattern.Structures;

import java.util.List;

public class LinkedList implements Iterable {

	
	private  LinkedListNode head;
	
	public LinkedList(LinkedListNode head) {
		super();
		this.head = head;
	}
	
	public void insert(LinkedListNode node) {
		
		LinkedListNode previous = head;
		while(previous.getNext() != null) {
			previous = previous.getNext();
		}
		previous.setNext(node);
	}
	

	@Override
	public Iterator getIterator() {
		// TODO Auto-generated method stub
		return new LinkedListIterator(this.head);
	}

}
