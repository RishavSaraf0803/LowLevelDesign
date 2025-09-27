package IterPattern.Structures;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyList list = new MyList();
		list.insert(1);list.insert(2);list.insert(3);list.insert(4);list.insert(5);
		
		LinkedList linkedList = new LinkedList(new LinkedListNode(10));
		linkedList.insert(new LinkedListNode(110));
		linkedList.insert(new LinkedListNode(220));
		linkedList.insert(new LinkedListNode(330));
		
		display(list);
		display(linkedList);
		
		
	}
	
	private static void display(Iterable iterable) {
		Iterator iterator = iterable.getIterator();
		while(iterator.hasNext()) {
			System.out.print(iterator.next() + " ");
		}
		
		System.out.println();
		
	}

}
