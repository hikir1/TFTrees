package perl.aaron.TruthTrees.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Counter<T> {
	private final Map<T,Integer> map;
	
	public Counter(Collection<? extends T> c) {
		map = new HashMap<>(c.size() * 2);
		c.forEach(k -> map.put(k, map.getOrDefault(k, 0) + 1));
	}
}
