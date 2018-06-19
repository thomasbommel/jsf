package agent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Maps the Key (K) to a Queue of Values (V).
 */
public class QueueMap<K,V> {

	private final HashMap<K, Queue<V>> map = new HashMap<>();
	
	public void add(K key, V value) {
		if (value == null) return;
		
		Queue<V> queue = map.get(key);
		if (queue == null) {
			queue = new LinkedList<>();
			map.put(key, queue);
		}
		queue.offer(value);
	}
	
	public V remove(K key) {
		Queue<V> queue = map.get(key);
		if (queue == null)
			return null;
		else {
			return queue.poll();
		}
	}
	
}