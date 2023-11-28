// Liam Major 30223023

package utils;

public class Pair<K, V> {

	K key;
	
	V val;
	
	public Pair(K k, V v) {
		key = k;
		val = v;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return val;
	}
	
}
