package IterPattern.Structures;

public class BinarySearchTree implements Iterable {

	private final TreeNode root;
	
	public BinarySearchTree(TreeNode root) {
		super();
		this.root = root;
	}

	
	@Override
	public Iterator getIterator() {
		// TODO Auto-generated method stub
		return new BSTIterator(this.root);
	}

}
