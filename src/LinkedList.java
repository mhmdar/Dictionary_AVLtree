

public class LinkedList {
	private Node first , last;
	private int size;
	
	public LinkedList(){	// Default constructor
	}

	//-----| Print |------------------------------------
	public String print(){
		String s = "";
		if (size != 0){
//			System.out.println("Number of elements = "+size);
			Node current = first;
			while (current != null){
				System.out.print(current.data+" ");
				s = s + current.data + "\n";
				current = current.next;
			}
			System.out.println("\n");
		} else {
			System.out.println("Linked List is empty");
			s = null;
		}
		return s;
	}
	
	//-----| getSize |---------------------------------
	public int getSize() {
		return size;
	}
		
	//-----| getFirst |--------------------------------- 
	public Node getFirst(){
		if (size == 0) return null;
		return first;
	}
	
	//-----| getLast |-----------------------------------
	public Node getLast(){
		if (size == 0) return null;
		else return last;
	}
	
	//-----| getNode |---------------------------------- 
	public Node getNode(int index){
		if (size == 0)
			return null;
		else if (index >= size)
			return null;
		else {
			Node current = first;
			for (int i=0 ; i<index ; i++)
				current = current.next;
			return current;
		}
	}
	
	//-----| getValue |----------------------------------
	public Object getData(int index){
		if (size == 0) return null;
		else if (size > 0){
			Node current = first;
			for (int i=0 ; i<size ; i++){
				if (i == index)			// when we compare an object we use .equals
					return current.data;
				current = current.next;
			}
		}
		return -1;
	}
		
	//-----| addFirst |---------------------------------
	public void addFirst(String s){
		Node temp;
		temp = new Node(s);
		if (size == 0)
			first = last = temp;
		else {
			temp.next = first;
			first = temp;
		}
		size++;
	}
	
	//-----| addLast |-----------------------------------
	
	public void addLast (String s) {
		if (size == 0)
			first = last = new Node(s);
		else {
			last.next = new Node(s);
			last = last.next;
		}
		size++;
	}
	
	//-----| add |--------------------------------------
	public void add (String s , int index) {
		if (index == 0)
			addFirst(s);
		else if (index >= size)
			addLast(s);
		else {
			Node temp = new Node(s);
			Node current = first;
			for (int i=0 ; i<index-1 ; i++)
				current = current.next;
			temp.next = current.next;
			current.next = temp;
			size++;
		}
	}
	
	//-----| setData |----------------------------------
		public void setData (String s , int index) {
			if (index >= size)
				addLast(s);
			else {
				Node current = first;
				for (int i=0 ; i<index ; i++)
					current = current.next;
				current.data = s;
			}
		}
	
	//-----| removeFirst |------------------------------
	public boolean removeFirst() {
		if (size == 0) return false;
		else {
			if (size == 1)
				first = last = null;
			else
				first = first.next;
			size--;
			return true;
		}
	}
	
	//-----| removeLast |-------------------------------
	public boolean removeLast() {
		if (size == 0) return false;
		else if (size == 1)
			first = last = null;
		else {
			Node current = first;
			for (int i=0 ; i<size-2 ; i++)
				current = current.next;
			current.next = null;
			last = current;
		}
		size--;
		return true;
	}
	
	//-----| remove |-----------------------------------
	public boolean remove(int index) {
		if (index == 0)
			return removeFirst();
		else if (index == size)
			return removeLast();
		else if ((index>0)&&(index<size)) {
			Node current = first;
			for (int i=0 ; i<index -1; i++)
				current = current.next;
			current.next = current.next.next;
			size--;
			return true;
		}
		else return false;
	}
	
	//-----| search |-----------------------------------
	public int search (String x) {
		if (size > 0){
			Node current = first;
			for (int i=0 ; i<size ; i++){
				if (x.equals(current.data))			// when we compare an object we use .equals
					return i;
				current = current.next;
			}
		}
		return -1;
	}
	

}	// end of class






