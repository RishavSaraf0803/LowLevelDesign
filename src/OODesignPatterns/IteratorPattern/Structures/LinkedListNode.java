package IterPattern.Structures;

public class LinkedListNode {
	
	private final int data;
	private LinkedListNode next;
	
	public LinkedListNode(int data) {
		this.data = data;
		this.next  = null;
		
	}

	public LinkedListNode getNext() {
		return next;
	}

	

	public int getData() {
		return data;
	}

	public void setNext(LinkedListNode node) {
		// TODO Auto-generated method stub
		next = node;
	}
	
	
	

}
