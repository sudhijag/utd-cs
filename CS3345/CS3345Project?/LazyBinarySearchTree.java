import java.lang.Math;

class LazyBinarySearchTree{
	private class TreeNode{
		int key=0;
		TreeNode leftChild=null;
		TreeNode rightChild=null;
		boolean deleted=false;
		
		TreeNode(){
			key=0;
			leftChild= null;
			rightChild= null;
			deleted= false;
		}
		
		TreeNode(int input_key){
			key=input_key;
			leftChild= null;
			rightChild= null;
			deleted= false;
		}
	}
	
	boolean successfulInsert=true;
	boolean successfulDelete=true;
	boolean isContained=true;
	
	private TreeNode root;
	
	LazyBinarySearchTree(){
		root=null;
	}
	
	/*
	 * returns the root
	 * 
	 * @return root   the root of the tree
	 * */
	TreeNode getRoot() {
		return root;
	}
	
	/*
	 * This method calls the recursive insert function to insert a key into the BST. 
	 * If the key is not between 1 and 99, an IllegalArgumentException is thrown.
	 * @param  key                 This is the key that we are trying to insert
	 * @return successfulInsert    Tells us if the key we were trying to insert was inserted successfully.
	 * */
	boolean insert(int key) {
		successfulInsert=true;
		
		try {
			if(key < 1 || key > 99) {
				System.out.println("Error raised in insert: IllegalArgumentException raised");
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException iae) {
			successfulInsert=false;
			return false;
		}
		
		System.out.println("Entering insert function");
		
		TreeNode nodeToAdd= new TreeNode(key);
		root= insert(nodeToAdd, root);
		
		return successfulInsert;
	}
	
	/*
	 * This method is the recursive insert function to insert a key into the BST. 
	 * If the current subtree is null, we insert the node there and return
	 * Else, we go right or left and recurse. 
	 * If we find a match, we check whether it is already deleted
	 * 
	 * @param n              This is the subtree we are examining and comparing with the key of nodeToInsert
	 * @param nodeToInsert   This is the node with the given key from the other insert function
	 * @return               The modified tree after the insert is performed
	 * */	
	TreeNode insert(TreeNode nodeToInsert, TreeNode n) { //n will be the start of that specific iteration
		if(n == null) {//i.e. there are no nodes in the tree
			n= nodeToInsert;
			System.out.print("Inserted!");
		}
		
		else if(n != null) {
			if(nodeToInsert.key < n.key) {//left is less
				//System.out.println("left is less");
				n.leftChild= insert(nodeToInsert, n.leftChild);
			}
			else if(nodeToInsert.key > n.key) {//right is more
				//System.out.println("right is more");
				n.rightChild= insert(nodeToInsert, n.rightChild);
			}
			else {
				System.out.println("There is already a node with that key!");
				successfulInsert=false;
				if(n.deleted == true) {
					n.deleted=false;
					return n;
				}
				else { //the node has not been deleted, i.e. you are trying to insert the same node twice.
					
				}
				return n;
			}
		}
		return n;
	}
	
	/*
	 * This method calls the recursive delete function to delete a key in the BST. 
	 * If the key is not between 1 and 99, an IllegalArgumentException is thrown.
	 * @param  key                 This is the key that we are trying to delete
	 * @return successfulDelete    Tells us if the key we were trying to insert was deleted successfully.
	 * */
	boolean delete(int key) {
		successfulDelete=true;
		
		try {
			if(key < 1 || key > 99) {
				System.out.println("Error raised in delete: IllegalArgumentException raised");
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException iae) {
			successfulDelete=false;
			return false;
		}
		
		System.out.println("Entering delete function");
		
		TreeNode nodeToDelete= new TreeNode(key);
		root=delete(nodeToDelete, root);
		
		return successfulDelete;
	}
	
	/*
	 * This method is the recursive delete function to delete a key into the BST. 
	 * If the current subtree is null, we know the node could not be found
	 * Else, we go right or left and recurse. 
	 * If we find a match, we check whether it is already deleted. If so, return false, else return true.
	 * 
	 * @param n              This is the subtree we are examining and comparing with the key of nodeToDelete
	 * @param nodeToDelete   This is the node with the given key from the other delete function
	 * @return               The modified tree after the delete is performed
	 * */
	TreeNode delete(TreeNode nodeToDelete, TreeNode n) {
		if(n == null) {//i.e. there are no nodes in the tree
			System.out.print("Not found");
			successfulDelete=false;
		}
		
		else if(n != null) {
			//System.out.println(nodeToDelete.key + "nodetodelete.key");
			//System.out.println(n.key + "n.key");
			if(nodeToDelete.key < n.key) {//left is less
				n.leftChild= delete(nodeToDelete, n.leftChild);
			}
			else if(nodeToDelete.key > n.key) {//right is more
				n.rightChild= delete(nodeToDelete, n.rightChild);
			}
			else{
				System.out.println("We found the node to delete!");
				
				if(n.deleted == true) {
					System.out.println("Bro, that node is already deleted...");
					successfulDelete=false;
					return n;
				}
				else {
					System.out.println("Deleting...");
					n.deleted= true;
					successfulDelete=true;
					return n;
				}
			}
		}
		
		return n;
	}
	
	/*
	 * This method calls the recursive contain function to see if a key is contained in the BST. 
	 * If the key is not between 1 and 99, an IllegalArgumentException is thrown.
	 * @param  key           This is the key that we are trying to find
	 * @return isContained   Tells us if the key we were trying to find is contained
	 * */
	boolean contains(int key) {
		try {
			if(key < 1 || key > 99) {
				System.out.println("Error raised in contains: IllegalArgumentException raised");
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException iae) {
			isContained=false;
			return false;
		}
		
		System.out.println("Entering contains function");
		
		TreeNode nodeToFind= new TreeNode(key);
		contains(nodeToFind, root);//we don't store the result because we are only examining the tree
		
		return isContained;
	}
	
	/*
	 * This method is the recursive contain function to see if a key is contained in the BST. 
	 * If the current subtree is null, we know the node could not be found
	 * Else, we go right or left and recurse. 
	 * If we find a match, we check whether it is already deleted. If so, return false, else return true.
	 * 
	 * @param n              This is the subtree we are examining and comparing with the key of nodeToFind
	 * @param nodeToFind     This is the node with the given key from the other contain function
	 * @return               The modified tree after the  is performed
	 * */
	TreeNode contains(TreeNode nodeToFind, TreeNode n) {
		if(n == null) {//i.e. there are no nodes in the tree
			System.out.print("Not found");
			return root;
		}
		
		else if(n != null) {
			if(nodeToFind.key == n.key) {
				if(n.deleted == false) {
					System.out.println("We found it, chief!");
					isContained=true;
					return root;
				}
				else {
					isContained=false;
					System.out.println("We found it, but that node is deleted");
					return null;
				}
			}
			if(nodeToFind.key < n.key) {//left is less
				//System.out.println("left is less");
				contains(nodeToFind, n.leftChild);
			}
			else if(nodeToFind.key > n.key) {//right is more
				//System.out.println("right is more");
				contains(nodeToFind, n.rightChild);
			}
		}
		
		return root;
	}
	
	/*
	 * This method is the minimum function. We start from the root and move left. For each element that is not deleted, 
	 * update it in the storer. Finally, return the value in the storer.
	 * 
	 * @return  storer.key     The minimum element of the tree, or -1 if no such element exists.
	 * */
	int findMin() {
		if(root==null) {
			return -1;
		}
		
		TreeNode mover=root;
		TreeNode storer=root;
		
		while(mover.leftChild != null) {
			mover= mover.leftChild;
			
			if(mover != null) {
				if(mover.deleted == false) {
					storer=mover;//store this node
				}
			}
		}
		
		System.out.println("Minimum: ");
		System.out.println(storer.key);
		return storer.key;
	}
	
	/*
	 * This method is the maximum function. We start from the root and move right. For each element that is not deleted, 
	 * update it in the storer. Finally, return the value in the storer.
	 * 
	 * @return storer.key      The maximum element of the tree, or -1 if no such element exists.
	 * */
	int findMax() {
		if(root==null) {
			return -1;
		}
		
		TreeNode mover=root;
		TreeNode storer=root;
		
		while(mover.rightChild != null) {
			mover= mover.rightChild;
			
			if(mover != null) {
				if(mover.deleted == false) {
					storer=mover;//store this node
				}
			}
		}
		
		System.out.println("Maximum: ");
		System.out.println(storer.key);
		return storer.key;
	}
	
	/*
	 * Calls the recursive size function.
	 * 
	 * @return  count    The number of nodes in the BST
	 * */
	int size() {
		int count=0; 
		
		if(root == null) {
			System.out.println("There are no nodes in this tree!");
			return 0;
		}
		
		count= size(root);
		return count;
	}
	
	/*
	 * This method is the recursive size function. Count starts at 1 for the root, and then counts the number of nodes 
	 * in the left and right subtrees. 
	 * 
	 * @return count   The number of nodes in the BST
	 * */
	int size(TreeNode n) {
		int count = 1;
		
		if(n.leftChild != null) {
			count += size(n.leftChild);
		}
		if(n.rightChild != null) {
	        count += size(n.rightChild);
		}	
		
		return count;
	}
	
	/*
	 * Calls the recursive height function.
	 * 
	 * @return  height    The number of nodes in the BST
	 * */	
	int height() {
		if(root == null) {
			System.out.println("There are no nodes in this tree!");
			return 0;
		}
		int height=0;
		
		height= height(root);
		
		return (height);//path length
	}
	
	/* Finds the height of the tree. The height of a null subtree or leaf nodes are 0. The height of the nodes above that are 1, 
	 * those above those are 1+1, and so on. The height is the maximum of the left and right subtrees.
	 * 
	 * @param n The subtree we are examining
	 * @return height the height of the subtree
	 * */
	int height(TreeNode n) {
		int height_right=0; 
		int height_left=0;
		int height = 0;
		
		if(n == null || (n.rightChild == null && n.leftChild == null) ) {
			return 0;
		}
		else {
			height_right= height(n.rightChild);
			height_left= height(n.leftChild);
			
			height= Math.max(height_right, height_left) + 1;
			return height;
		}
	}
	
	String treeString="";
	
	/* This function calls the traverse function. It is called when the user attempts to print the tree using System.out.print
	  * 
	  * @return treeString the string version of the tree after performing a preorder traversal.
	  * */
	 @Override
	 public String toString() { 
		 //System.out.print("We're in the override now!");
		 treeString="";
		 traverse(root);
		 return treeString;
	 } 
	 
	 /* This function basically ensures the treeString is empty before calling the traverse function.
	  * 
	  * @param n The subtree we are examining
	  * @return treeString the string version of the tree after performing a preorder traversal.
	  * */
	 String traverse2(TreeNode n){
		 treeString="";
		 String s= traverse(n);
		 return s;
	 }
	 
	 /* This function performs a recursive pre-order traversal of the tree. 
	  * For each node visited, the treeString is modified
	  * 
	  * @param n The subtree we are examining
	  * @return treeString the string version of the tree after performing a preorder traversal.
	  * */
	 String traverse(TreeNode n){ 
		if (n == null) {
		    return ""; 
		}
	
		if(n.deleted == false) {
			//System.out.print(n.key + " ");
			treeString= (treeString+ n.key + " ");
		}
		else if(n.deleted == true) {
			treeString= (treeString+ "*"+ n.key+ " ");
			//System.out.print("*" +n.key);
		}
  
	   traverse(n.leftChild); 
       traverse(n.rightChild);
       
       return treeString;
	 } 
}

