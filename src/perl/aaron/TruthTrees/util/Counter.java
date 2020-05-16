package perl.aaron.TruthTrees.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Counter {
	
	public static <T> Map<T,Integer> count(Collection<? extends T> c) {
		assert c != null;
		var map = new HashMap<T,Integer>(c.size() * 2);
		c.forEach(t -> map.put(t, map.getOrDefault(t, 0) + 1));
		return Collections.unmodifiableMap(map);
	}
	
}
