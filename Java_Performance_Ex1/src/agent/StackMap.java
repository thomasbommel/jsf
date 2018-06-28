package agent;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

/**
 * Maps the Key (K) to a Stack of Values (V).
 */
public class StackMap<K,V> {

	private final HashMap<K, Stack<V>> map = new HashMap<>();
	
	public void add(K key, V value) {
		if (value == null) return;
		
		Stack<V> stack = map.get(key);
		if (stack == null) {
			stack = new Stack<>();
			map.put(key, stack);
		}
		stack.push(value);
	}
	
	public V remove(K key) {
		Stack<V> stack = map.get(key);
		if (stack == null)
			return null;
		else {
			return stack.pop();
		}
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}
	
	public Stack<V> getStack(K key) {
		return map.get(key);
	}
	
}