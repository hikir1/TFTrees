package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.AEquationType;
import perl.aaron.TruthTrees.logic.fo.Equation;
import perl.aaron.TruthTrees.logic.fo.EquationType;
import perl.aaron.TruthTrees.logic.fo.LogicObject;
import perl.aaron.TruthTrees.logic.fo.PredicateType;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.util.Counter;

public class Inequation extends AEquationType implements Negation, NonDecomposable {
	public static final String TYPE_NAME = "inequality";
	public static final String SYMBOL = "\u2260";
	
	private final Map<LogicObject,Integer> argumentsCount;
	
	public Inequation(LogicObject left, LogicObject right, final Map<LogicObject, Set<PredicateType>> atomicPredicates) {
		super(TYPE_NAME, SYMBOL, List.of(left, right), atomicPredicates);
		argumentsCount = Counter.count(arguments);
	}
	
	public Inequation withArgs(List<LogicObject> arguments) {
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
	public Statement getInner() {
		return new Equation(arguments, atomicPredicates);
	}
	
	@Override
	public String symString() {
		return super.symString();
	}


}
