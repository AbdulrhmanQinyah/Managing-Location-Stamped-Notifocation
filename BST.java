
class BSTNode<K extends Comparable<K>, T> {
	public T data;
	public K key;
	public BSTNode<K, T> left, right;

	public BSTNode(K key, T data) {
		this.data = data;
		this.key = key;
		left = right = null;
	}

}

public class BST<K extends Comparable<K>, T> implements Map<K, T> {
	BSTNode<K, T> root;
	BSTNode<K, T> crnt;

	public BST() {
		root = crnt = null;
	}

	public boolean empty() {
		return root == null;
	}

	public boolean full() {
		return false;
	}

	public void clear() {
		root = null;
	}

	public T retrieve() {
		return crnt.data;
	}

	public void update(T e) {
		crnt.data = e;
	}
	//////////////////////////////////////////////////////////////////////

	public boolean find(K key) {
		BSTNode<K, T> b = root;
		if (empty())
			return false;
		while (b != null) {
			if (key.compareTo(b.key) == 0) {
				crnt = b;
				return true;
			} else if (key.compareTo(b.key) < 0) {
				b = b.left;
			} else {
				b = b.right;
			}
		}
		return false;

	}

	private boolean findKey(K key) {
		BSTNode<K, T> b = root;
		BSTNode<K, T> b2 = root;
		if (empty())
			return false;
		while (b != null) {
			b2 = b;
			if (key.compareTo(b.key) == 0) {
				crnt = b;
				return true;
			} else if (key.compareTo(b.key) < 0) {
				b = b.left;
			} else {
				b = b.right;
			}
		}
		crnt = b2;
		return false;

	}

	/////////////////////////////////////////////////////////////////////

	public int nbKeyComp(K key) {
		BSTNode<K, T> b = root;
		int count = 0;
		if (empty()) {
			return count;
		}
		while (b != null) {
			if (key.compareTo(b.key) == 0) {
				count++;
				break;
			} else if (key.compareTo(b.key) < 0) {
				b = b.left;
				count++;
			} else {
				b = b.right;
				count++;
			}
		}
		return count;

	}

	//////////////////////////////////////////////////////////////////////

	public boolean insert(K key, T data) {
		// BSTNode<K,T> b = crnt;
		BSTNode<K, T> b2 = crnt;

		if (findKey(key)) {
			crnt = b2;
			return false;
		} else {
			BSTNode<K, T> k = new BSTNode<K, T>(key, data);
			if (empty()) {
				root = crnt = k;
				return true;
			} else {
				if (key.compareTo(crnt.key) < 0) {
					crnt.left = k;
				} else {
					crnt.right = k;
				}
				crnt = k;
				return true;
			}

		}
	}

	//////////////////////////////////////////////////////////////////////

	public boolean remove(K key) {
		BooleanWrapper removed = new BooleanWrapper(false);
		BSTNode<K, T> p;
		p = remove_aux(key, root, removed);
		crnt = root = p;
		return removed.isB();
	}

	private BSTNode<K, T> remove_aux(K key, BSTNode<K, T> p, BST<K, T>.BooleanWrapper removed) {
		BSTNode<K, T> q, child = null;
		if (p == null)
			return null;
		if (key.compareTo(p.key) < 0)
			p.left = remove_aux(key, p.left, removed); // go left
		else if (key.compareTo(p.key) > 0)
			p.right = remove_aux(key, p.right, removed); // go right
		else {
			removed.setB(true);
			if (p.left != null && p.right != null) { // two children
				q = find_min(p.right);
				p.key = q.key;
				p.data = q.data;
				p.right = remove_aux(q.key, p.right, removed);
			} else {
				if (p.right == null) // one child
					child = p.left;
				else if (p.left == null) // one child
					child = p.right;
				return child;
			}
		}
		return p;
	}

	private BSTNode<K, T> find_min(BSTNode<K, T> p) {
		if (p == null)
			return null;

		while (p.left != null) {
			p = p.left;
		}

		return p;
	}

	private class BooleanWrapper {
		boolean b;

		public BooleanWrapper(boolean g) {

			this.b = g;
		}

		public boolean isB() {
			return b;
		}

		public void setB(boolean b) {
			this.b = b;
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////

	public List<Pair<K, T>> getAll() {
		List<Pair<K, T>> l = new LinkedList<Pair<K, T>>();
		BSTNode<K, T> b = root;
		if (empty()) {
			return null;
		}

		return recGetAll(b, l);

	}

	// --------------------------------------

	private List<Pair<K, T>> recGetAll(BSTNode<K, T> b, List<Pair<K, T>> l) {

		if (b == null) {
			return null;
		} else {

			recGetAll(b.left, l);

			l.insert(new Pair<K, T>(b.key, b.data));

			recGetAll(b.right, l);
		}
		return l;

	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public List<Pair<K, T>> getRange(K k1, K k2) {

		List<Pair<K, T>> ll = new LinkedList<Pair<K, T>>();

		recGetRange(k1, k2, root, ll);
		return ll;
	}

	// ------------------------------

	private void recGetRange(K k1, K k2, BSTNode<K, T> p, List<Pair<K, T>> l) {
		if (p == null) {
			return;
		} else {

			if (k1.compareTo(p.key) < 0) {
				recGetRange(k1, k2, p.left, l);
			}
			if (k1.compareTo(p.key) <= 0 && k2.compareTo(p.key) >= 0) {
				l.insert(new Pair<K, T>(p.key, p.data));
			}
			if (k2.compareTo(p.key) > 0) {
				recGetRange(k1, k2, p.right, l);
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	private int recNbKeyComp(K k1, K k2, BSTNode<K, T> b) { 

		int count = 0;

		if (b == null) {
			return 0;
		}

		if (k1.compareTo(b.key) < 0) {
			count += recNbKeyComp(k1, k2, b.left);
		}
		if (k2.compareTo(b.key) > 0) {
			count += recNbKeyComp(k1, k2, b.right);
		}

		return count + 1;

	}

	// ----------------------------------------

	public int nbKeyComp(K k1, K k2) {

		return recNbKeyComp(k1, k2, root);

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	 public void printAsec(){
	 printAscending(root);
	 }
	 private void printAscending(BSTNode<K,T> node) {
	 // is there actually a node here
	 // or was this called from a node with no children
	 if(node != null) {
	 // print everything that's earlier than this node
	 printAscending(node.left);
	
	 // print this node's value
	 System.out.println(node.data);
	
	 // print everything that's afterthan this node
	 printAscending(node.right);
	 }
	 }

}