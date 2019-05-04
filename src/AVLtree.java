import java.io.BufferedWriter;
import java.io.IOException;

//  The following website visualizes binary trees:
//
//  https://www.cs.usfca.edu/~galles/visualization/BST.html
//------------------------------------------------------------
//     AVL Tree is a binary tree with balance:
//
//     Balance = | height(left) - height(right) | <= 1
//
//           (K2)                           (K1)
//           /  \        Single             /  \
//          /    \       Rotate            /    \
//        (K1)   (C)     <----->         (A)   (K2)
//       /   \                                 /  \
//      /     \                               /    \
//    (A)     (B)                           (B)    (C)
//
//
//           (K2)                          (K3)
//           /  \       Double            /    \
//          /    \      Rotate           /      \
//       (K1)    (D)    <------>      (K1)       (K2)
//       /  \                        /   \       /   \
//      /    \                      /     \     /     \
//    (A)   (K3)                  (A)    (B)  (C)     (D)
//          /  \
//         /    \
//       (B)   (C)


public class AVLtree {
	
	AVLnode root;

//	----| The "print" methods: |------------------------------------------------------------
	
	public String printPreorder(){
		String s;
		if (root == null){
			System.out.println("Tree is empty.");
			return null;
		}
		else{
			System.out.print("Preorder   ");
			s = printPreorder(root);
			System.out.println();
		}
		return s;
	}
	
	public String printPreorder(AVLnode T){
		String s = "";
		if(T != null){
//			System.out.print(T.data+"  ");
			System.out.print(T.data+"("+getLeft(T)+" "+getRight(T)+")(h:"+T.height+")   ");
			s = s + T.data + "    ";
			s = s + printPreorder(T.left);
			s = s + printPreorder(T.right);
		}
		return s;
	}
	
	public String printInorder(){
		String s;
		if (root == null){
			System.out.println("Tree is empty.");
			return null;
		}
		else{
			System.out.print("Inorder    ");
			s = printInorder(root);
//			printInorder(root);
			System.out.println();
		}
		return s;
	}
	
	public String printInorder(AVLnode T){
		String s = "";
		if(T != null){
			s = s + printInorder(T.left);
//			System.out.print(T.data+"  ");
			System.out.print(T.data+"("+getLeft(T)+" "+getRight(T)+")(h:"+T.height+")   ");
			s = s + T.data + "    ";
			s = s + printInorder(T.right);
		}
		return s;
	}
	
	
	public String printPostorder(){
		String s;
		if (root == null){
			System.out.println("Tree is empty.");
			return null;
		}
		else{
			System.out.print("Postorder  ");
			s = printPostorder(root);
			System.out.println();
		}
		return s;
	}
	
	public String printPostorder(AVLnode T){
		String s = "";
		if(T != null){
			s = s + printPostorder(T.left);
			s = s + printPostorder(T.right);
//			System.out.print(T.data+"  ");
			System.out.print(T.data+"("+getLeft(T)+" "+getRight(T)+")(h:"+T.height+")   ");
			s = s + T.data + "    ";
		}
		return s;
	}
	
//	----| Rotation methods: |---------------------------------------------------------------
	
	public AVLnode singleRotateRightToLeft(AVLnode K1){
		AVLnode K2 = K1.right;
		K1.right = K2.left;
		K2.left = K1;
		K1.height = Math.max(getHeight(K1.left), getHeight(K1.right)) + 1;
		K2.height = Math.max(getHeight(K1), getHeight(K2.right)) + 1;
		return K2;
	}
	
	public AVLnode singleRotateLeftToRight(AVLnode K2){
		AVLnode K1 = K2.left;
		K2.left = K1.right;
		K1.right = K2;
		K2.height = Math.max(getHeight(K2.left), getHeight(K2.right)) + 1;
		K1.height = Math.max(getHeight(K1.left), K2.height) + 1;
		return K1;
	}
	
	public AVLnode doubleRotateClockwise(AVLnode K2){
		K2.left = singleRotateRightToLeft(K2.left);
		return (singleRotateLeftToRight(K2));
	}
	
	public AVLnode doubleRotateCounterClockwise(AVLnode K3){
		K3.right = singleRotateLeftToRight(K3.right);
		return (singleRotateRightToLeft(K3));
	}
	
//	----| height methods: |-----------------------------------------------------------------

	public int getHeight(){
		return getHeight(root);
	}
	
	private int getHeight(AVLnode T){
		if (T == null)
			return -1;
		else
			return T.height;
	}
	
	private int calculateHeight(AVLnode T){
		if (T == null)
			return -1;
		else
			return (Math.max(calculateHeight(T.left), calculateHeight(T.right)) + 1);
	}
	
//	----| Insertion: |----------------------------------------------------------------------
	
	public void insert(String x){
		root = insert(root, x);
	}
	
	private AVLnode insert(AVLnode T, String x){
		if (T == null){
			T = new AVLnode (x);
//			System.out.println(x+" has been inserted");
			T.height = calculateHeight(T);
		}
		else if (x.compareToIgnoreCase(T.data) < 0){		//  if (x < T.data)
			T.left = insert(T.left, x);
			T.height = calculateHeight(T);
			if ( getHeight(T.left) - getHeight(T.right) > 1 ){
				System.out.println("Height difference at "+T.data);
				if (x.compareToIgnoreCase(T.left.data) < 0){  // x < T.left.data  --> outside case
					T = singleRotateLeftToRight(T);
					System.out.println("singleRotateLeftToRight("+T.data+") has been called");
				}
				else {   // inside case
					T = doubleRotateClockwise(T);
					System.out.println("doubleRotateClockwise("+T.data+") has been called");
				}
			}
		}
		else {
			T.right = insert(T.right, x);
			T.height = calculateHeight(T);
			if ( getHeight(T.right) - getHeight(T.left) > 1 ){
				System.out.println("Height difference at "+T.data);
				if (x.compareToIgnoreCase(T.right.data) > 0){  // x > T.right.data  -->  outside case
					T = singleRotateRightToLeft(T);
					System.out.println("singleRotateRightToLeft("+T.data+") has been called");
				}
				else {  // inside case
					T = doubleRotateCounterClockwise(T);
					System.out.println("doubleRotateCounterClockwise("+T.data+") has been called");
				}
			}
		}
		return T;
	}	// end of insert(AVLnode, String)
	
//	----| getLeft & getRight: |-------------------------------------------------------------
	
	public String getLeft(AVLnode T){
		if (T.left != null)
			return T.left.data;
		else
			return null;
	}
	
	public String getRight(AVLnode T){
		if (T.right != null)
			return T.right.data;
		else
			return null;
	}
	
//	----| find node: |----------------------------------------------------------------------
	
	public AVLnode find(String x){
		return find(root, x);
	}
	
	private AVLnode find(AVLnode T, String x){
		if (T == null)
			return null;
		if (x.compareToIgnoreCase(T.data) < 0)		// x < T.data
			return ( find(T.left, x) );
		else if (x.compareToIgnoreCase(T.data) > 0)						// x > T.data
			return ( find(T.right, x));
		else return T;
	}
	
//	can be done in non-recursive method:
//	private Node find(Node T, int x){
//		while (T != null){
//			if (x < T.data)
//				T = T.left;
//			else if (x >= T.data)
//				T = T.right;
//			else
//				break;
//			return T;
//		}
//	}
	
	public AVLnode findMin(){
		return findMin(root);
	}
	
	private AVLnode findMin(AVLnode T){
		if (T == null)
			return null;
		else if (T.left == null)
			return T;
		else
			return (findMin(T.left))   ;
	}
	
	public AVLnode findMax(){
		return findMax(root);
	}
	
	private AVLnode findMax(AVLnode T){
		if (T == null)
			return null;
		else
			while(T.right != null)
				T = T.right;
		return T;
	}
	
//	----| getBalance |----------------------------------------------------------------------
	private int getBalance(AVLnode N){
		if (N == null)
			return 0;
		return getHeight(N.left) - getHeight(N.right);
	}
	
//	----| delete node: |--------------------------------------------------------------------
	public AVLnode delete(String x){
		return root = delete(root,x);
	}
	
	private AVLnode delete(AVLnode T, String x){
		AVLnode child=null, tmp;
		if (T == null)
			System.out.println("Error -->  Tree is empty");
		else if (x.compareToIgnoreCase(T.data) < 0){		// x < T.data
			T.left = delete(T.left, x);
			T.height = calculateHeight(T);
		}
		else if (x.compareToIgnoreCase(T.data) > 0){		// x > T.data
			T.right = delete(T.right, x);
			T.height = calculateHeight(T);
		}
		else if (T.left != null && T.right != null){
			tmp = findMin(T.right);
			T.data = tmp.data;
			T.right = delete(T.right, T.data);
			T.right.height = calculateHeight(T.right);
		}
		else {
			if (T.left == null)
				child = T.right;
			if (T.right == null)
				child = T.left;
			return child;
		}
		
		if (T == null)
			return T;
		
		if (getBalance(T) > 1 && getBalance(T.left) >= 0)
			return singleRotateLeftToRight(T);
		else if (getBalance(T) > 1 && getBalance(T.left) < 0)
			return doubleRotateClockwise(T);
		
		else if (getBalance(T) < -1 && getBalance(T.right) <= 0)
			return singleRotateRightToLeft(T);
		else if (getBalance(T) < -1 && getBalance(T.right) > 0)
			return doubleRotateCounterClockwise(T);
		
		return T;
	}	// end of delete(AVLnode, String)

//	----| Send to file Inorder traversal with meanings :|-----------------------------------
	
	public void sendToFileInorderWithMeanings(AVLtree tree, BufferedWriter buffer) throws IOException{
		sendToFileInorderWithMeanings(tree.root, buffer);
		return;
	}
	
	private void sendToFileInorderWithMeanings(AVLnode T, BufferedWriter buffer) throws IOException{
		if (T != null){
			sendToFileInorderWithMeanings(T.left, buffer);
			buffer.write("----------------");
			buffer.newLine();
			buffer.write(T.data+":");
			buffer.newLine();
			buffer.write("----------------");
			buffer.newLine();
			
			Node N = T.meanings.getFirst();
			while (N != null){
				buffer.write(N.data.toString());
				buffer.newLine();
				N = N.next;
			}
			sendToFileInorderWithMeanings(T.right, buffer);
		}
		return;
	}	//end of sendToFileInorderWithMeanings(AVLnode, BufferedWriter)
	
}	// end of class

