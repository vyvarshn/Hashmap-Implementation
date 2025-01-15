import java.util.ArrayList;

public class Map<K,V> {

	ArrayList<MapNode<K,V>> buckets;
	int size;
	int numBuckets;
	
	public Map() {
		numBuckets=5;
		buckets= new ArrayList<>();
		for(int i=0;i<numBuckets;i++) {
			buckets.add(null);
		}
	}
	
	private int getBucketIndex(K Key) {
		int hashCode = Key.hashCode();
		return hashCode%numBuckets;
	}
	
	public void insert (K Key, V Value) {
		int bucketIndex= getBucketIndex(Key);
		MapNode<K,V> head = buckets.get(bucketIndex);
		while(head!=null) {
			if(head.key.equals(Key)) {
				head.value=Value;
				return;
			}
			head = head.next;
		}
		head = buckets.get(bucketIndex);
		MapNode<K,V> newElementNode = new MapNode<K,V>(Key,Value);
		newElementNode.next=head;
		buckets.set(bucketIndex, newElementNode);
		size++;
		if((1.0*size)/numBuckets>0.7) {
			rehash();
		}
	}
	
	public V removeKey(K key) {
		int bucketIndex= getBucketIndex(key);
		MapNode<K,V> head = buckets.get(bucketIndex);
		MapNode<K,V> prev = null;
		while (head!=null) {
			if(head.key.equals(key)) {
				if(prev==null) {
					buckets.set(bucketIndex, head.next);
					
				}else {
					prev.next= head.next;
				}
				return head.value;
			}
			prev = head;
			head = head.next;
		}
		return null;
	}
	
	public V getValue(K key) {
		MapNode<K,V> head = buckets.get(getBucketIndex(key));
		while(head!=null) {
			if(head.key.equals(key)) {
				return head.value;
			}
			head = head.next;
		}
		return null;
	}
	
	public int size() {
		return size;
	}
	
	public double loadFactor () {
		return (1.0*size)/numBuckets;
	}
	
	private void rehash() {
		System.out.println("rehashing required since load factor:" +loadFactor() +" is greater than 0.7");
		ArrayList<MapNode <K,V>> temp = buckets;
		buckets= new ArrayList<>();
		for(int i=0;i<2*numBuckets;i++) {
			buckets.add(null);
		}
		size=0;
		numBuckets*=2;
		for(int i=0; i< temp.size();i++) {
			MapNode<K,V> head = temp.get(i);
			while (head!=null) {
				K key = head.key;
				V value = head.value;
				insert (key,value);
				head = head.next;
			}
		}
	}

}
