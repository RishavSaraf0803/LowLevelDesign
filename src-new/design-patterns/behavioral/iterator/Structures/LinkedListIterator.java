package IterPattern.Structures;

public class LinkedListIterator implements Iterator{

	
	private  LinkedListNode head;
	
	public LinkedListIterator(LinkedListNode head) {
		super();
		this.head = head;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return head == null ? false : true;
	}

	@Override
	public int next()  {
		// TODO Auto-generated method stub
		int curr = this.head.getData();
		head = head.getNext();
		return curr;
	}

}
