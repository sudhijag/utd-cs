import java.util.*;

public class Product implements IDedObject{
	private int productID;
	private String productName;
	private String supplierName;
	
	public static void main(String args[]) {
		LinkedList LL=new LinkedList();
		
		int user_input=0;
		while(user_input != 7) {
			printMenu();
			Scanner input= new Scanner(System.in);
			user_input= input.nextInt();
			
			System.out.println();
			if(user_input == 1) {
				LL.makeEmpty();
			}
			else if(user_input == 2) {
				System.out.print("Enter your product ID:");
				int tempint= input.nextInt();
				
				IDedObject result= LL.findID(tempint);
			}
			else if(user_input == 3) {
				System.out.print("Enter product ID: ");
				int tempint= input.nextInt();
					
				String garbage= input.nextLine();//it is known that there is a newline that is automatically read
				System.out.print("Enter product name: ");
				String tempString= input.nextLine();
				
				System.out.print("Enter supplier name: ");
				String tempString2= input.nextLine();
				
				Product x= new Product(tempint, tempString, tempString2);
				LL.insertAtFront(x);
			}
			else if(user_input == 4) {
				IDedObject result= LL.deleteFromFront();
			}
			else if(user_input == 5) {
				System.out.print("Enter product ID: ");
				int ID= input.nextInt();
				
				IDedObject result= LL.delete(ID);
			}
			else if(user_input == 6) {
				LL.printAllRecords();
			}
			else if(user_input == 7) {
				System.out.print("Thank you. Exiting...");
			}
		}
		
	}
	
	Product(){
		productID=0;
		productName="";
		supplierName="";
	}
	
	Product(int pID, String pName, String sName){
		productID= pID;
		productName= pName;
		supplierName= sName;
	}
	
	static void printMenu() {
		int user_input=0;
		
		System.out.println();
		System.out.println("1. Make Empty"); /// This Option makes the Linked list empty
		System.out.println("2. Find ID");	/// print all details of the product ID, if it is in the list , if not print appropriate message
		System.out.println("3. Insert At Front"); // Get the product magazine details from the user and add it to the front of the list
		System.out.println("4. Delete From Front"); //Print the first item on the list and then delete it.
		System.out.println("5. Delete ID"); // Print particular IDed item and then delete it.
		System.out.println("6. Print All Records"); // Print all the records in the list
		System.out.println("7. Done"); ///Quit the program. For every other option after task completion display menu again.
		
		System.out.print("Your Choice: ");

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		// Directions: returns product ID
		return productID;
	}

	@Override
	public void printID() {
		// TODO Auto-generated method stub
		// Directions: Print all 3 fields on separate lines
		System.out.println("productID: " + productID);
		System.out.println("productName: " + productName);
		System.out.println("supplierName: " + supplierName);
		
	}
}

/*
 * AnyType(
*/

interface IDedObject{
	int getID();
	void printID();
	
}

class Node<AnyType extends IDedObject>{
	AnyType data;
	Node<AnyType> next;
	
	Node(){
		data= null;
		next= null;
	}
	
	Node(AnyType userdata, Node<AnyType> usernext){
		data= userdata;//possible error, is this valid?
		next= usernext;
	}
}

class LinkedList<AnyType extends IDedObject>{
	Node <AnyType> head;
	
	LinkedList(){
		//make an empty list
		head=null;
	}
	boolean insertAtFront(AnyType x) {
		//1. search linkedlist for id x
		Node<AnyType> mover= head;
		
		while(mover != null) {
			System.out.print("mover: ");
			mover.data.printID();
			
			if(mover.data.getID() == x.getID()) {
				System.out.println("There is already an item with the product id: "+ x.getID());
				return false;
			}
			mover = mover.next;
		}
		
		//2. Insert at head
		Node<AnyType> newNode= new Node<AnyType>(x,head);
		head = newNode;
		
		System.out.println("Done inserting");
		
		return true;
	}
	
	void printAllRecords() {
		Node <AnyType> mover= head;
		
		if(head == null) {
			System.out.println("There are no nodes in the linked list at present!");
			return;
		}
		
		while(mover != null) {
			System.out.println();
			mover.data.printID();
			mover= mover.next;
		}
	}
	
	AnyType findID(int ID) {
		Node <AnyType> mover=head;

		if(head==null) {
			System.out.println("There are no nodes in the list!");
			return null;
		}
		
		while(mover != null) {
			
			if(mover.data.getID() == ID) {
				System.out.println("We found your ID: " + ID);
				return mover.data;
			}
			
			mover= mover.next;
		}
		System.out.println("We couldn't find your ID: "+ ID);
		return null;
	}
	
	
	AnyType delete(int ID){
		if(head == null) {
			System.out.println("No nodes to delete!");
			return null;
		}
		
		//In case the node to be deleted is at the front
		if(head.data.getID() == ID) {
			deleteFromFront();
			
			if(head != null) {
				return head.data;
			}
			else {
				return null;
			}
		}
		//i.e. node not found
		if(findID(ID) == null) {
			System.out.println("There is no product with that ID, cannot delete");
			return null;
		}
			
			
		Node <AnyType> previous= head;
		Node <AnyType> mover= head.next;
			
		while(mover.data.getID() != ID) {
			mover=mover.next;
			previous=previous.next;
		}
		
		if(mover.next == null) {
			previous.next= null;
			//delete p
			mover.data= null;
			mover.next = null;
			
			if(head != null) {
				return head.data;
			}
			else {
				return null;
			}
		}
		else {
			previous.next = mover.next;
			
			mover.data = null;
			mover.next= null;
			
			if(head != null) {
				return head.data;
			}
			else {
				return null;
			}
		}
		//return head.data;
	}
	
	AnyType deleteFromFront(){
		Node <AnyType> toBeDeleted=head;
		Node <AnyType> storer= head;
		
		if(head == null) {
			System.out.print("No nodes to delete!");
			return null;
		}
		
		storer= toBeDeleted.next;
		head=storer;
		
		toBeDeleted.next=null;
		toBeDeleted.data= null;
		
		if(head != null) {
			return head.data;
		}
		else {
			return null;
		}
	}
	
	void makeEmpty() {
		Node <AnyType> toBeDeleted=head;
		Node <AnyType> storer= head;
		
		if(head == null) {
			System.out.println("Deleted it all!");
			return;
		}
		
		while(head != null) {
			deleteFromFront();
		}
		
		System.out.println("Deleted it all!");
	}
}