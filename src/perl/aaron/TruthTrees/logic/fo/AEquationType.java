package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AEquationType extends APredicate implements EquationType {
	protected final Map<LogicObject,Set<PredicateType>> atomicPredicates;
	
	public AEquationType(
			final String typeName,
			final String symbol,
			final List<LogicObject> arguments,
			final Map<LogicObject, Set<PredicateType>> atomicPredicates) {
		super(typeName, symbol, arguments);
		assert arguments != null;
		assert arguments.size() >= 2;
		assert atomicPredicates != null;
		// do not copy, needs detect changes made by the class that calls verifyDecomposition()
		// "immutible" proxy given
		this.atomicPredicates = atomicPredicates;
	}
	
}
