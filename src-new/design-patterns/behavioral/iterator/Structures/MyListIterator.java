package IterPattern.Structures;

import java.util.List;

public class MyListIterator implements Iterator {

	private final List<Integer> internalList;
	private Integer index;
	private final Integer size;
	
	public MyListIterator(List<Integer> list) {
		super();
		this.internalList = list;
		this.index = 0;
		this.size = list.size();
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return index < size;
	}

	@Override
	public int next() {
		// TODO Auto-generated method stub
		
		return internalList.get(index++);
			
		
		
	}

}
