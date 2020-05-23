package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.fo.A_EquationType;
import perl.aaron.TruthTrees.logic.fo.A_LogicObject;
import perl.aaron.TruthTrees.logic.fo.Equation;
import perl.aaron.TruthTrees.logic.fo.I_PredicateType;
import perl.aaron.TruthTrees.logic.negation.I_Negation;
import perl.aaron.TruthTrees.util.Counter;

public class Inequation extends A_EquationType implements I_Negation {
	public static final String TYPE_NAME = "inequality";
	public static final String SYMBOL = "\u2260";
	
	private final Map<A_LogicObject,Integer> argumentsCount;
	
	public Inequation(A_LogicObject left, A_LogicObject right, final Map<A_LogicObject, Set<I_PredicateType>> atomicPredicates) {
		super(TYPE_NAME, SYMBOL, List.of(left, right), atomicPredicates);
		argumentsCount = Counter.count(arguments);
	}
	
	public Inequation withArgs(List<A_LogicObject> arguments) {
		assert arguments != null;
		assert arguments.size() == 2;
		return new Inequation(arguments.get(0), arguments.get(1), atomicPredicates);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Inequation))
			return false;
		return argumentsCount.equals(((Inequation)other).argumentsCount);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ argumentsCount.hashCode();
	}

	@Override
	public I_Statement getInner() {
		return new Equation(arguments, atomicPredicates);
	}
	
	@Override
	public String symString() {
		return super.symString();
	}


}
