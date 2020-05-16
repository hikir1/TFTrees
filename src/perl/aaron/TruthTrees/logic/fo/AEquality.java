package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AEquality extends APredicate {
	protected final Map<LogicObject,Set<APredicate>> atomicPredicates;
	
	public AEquality(
			final String typeName,
			final String symbol,
			final List<LogicObject> arguments,
			final Map<LogicObject, Set<APredicate>> atomicPredicates) {
		super(typeName, symbol, arguments);
		assert arguments != null;
		assert arguments.size() >= 2;
		assert atomicPredicates != null;
		// do not copy, needs detect changes made by the class that calls verifyDecomposition()
		// "immutible" proxy given
		this.atomicPredicates = atomicPredicates;
	}
	
	@Override
	public String symString() {
		return arguments.stream().map(a -> a.toString()).collect(Collectors.joining(" = "));
	}
	
}
