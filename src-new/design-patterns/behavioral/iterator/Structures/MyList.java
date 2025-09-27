package IterPattern.Structures;

import java.util.ArrayList;
import java.util.List;

public class MyList implements Iterable {

	private final List<Integer> internalList;
	
	public MyList() {
		super();
		this.internalList = new ArrayList<>();
	}
	public void insert(Integer x) {this.internalList.add(x);}
	
	
	public List<Integer> getInternalList() {
		return internalList;
	}
	@Override
	public Iterator getIterator() {
		// TODO Auto-generated method stub
		return new MyListIterator(this.internalList);
	}

}
