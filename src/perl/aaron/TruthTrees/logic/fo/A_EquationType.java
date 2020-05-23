package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class A_EquationType extends A_Predicate implements I_EquationType {
	protected final Map<A_LogicObject,Set<I_PredicateType>> atomicPredicates;
	
	public A_EquationType(
			final String typeName,
			final String symbol,
			final List<A_LogicObject> arguments,
			final Map<A_LogicObject, Set<I_PredicateType>> atomicPredicates) {
		super(typeName, symbol, arguments);
		assert arguments != null;
		assert arguments.size() >= 2;
		assert atomicPredicates != null;
		// do not copy, needs detect changes made by the class that calls verifyDecomposition()
		// "immutible" proxy given
		this.atomicPredicates = atomicPredicates;
	}
	
}
