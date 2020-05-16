package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.AEquality;
import perl.aaron.TruthTrees.logic.fo.APredicate;
import perl.aaron.TruthTrees.logic.fo.Equality;
import perl.aaron.TruthTrees.logic.fo.LogicObject;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.util.Counter;

public class Inequality extends AEquality implements Negation, NonDecomposable {
	public static final String TYPE_NAME = "inequality";
	public static final String SYMBOL = "\u2260";
	
	private final Map<LogicObject,Integer> argumentsCount;
	
	public Inequality(LogicObject left, LogicObject right, final Map<LogicObject, Set<APredicate>> atomicPredicates) {
		super(TYPE_NAME, SYMBOL, List.of(left, right), atomicPredicates);
		argumentsCount = Counter.count(arguments);
	}
	
	public Inequality newInstance(List<LogicObject> arguments) {
		assert arguments != null;
		assert arguments.size() == 2;
		return new Inequality(arguments.get(0), arguments.get(1), atomicPredicates);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Inequality))
			return false;
		return argumentsCount.equals(((Inequality)other).argumentsCount);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ argumentsCount.hashCode();
	}

	@Override
	public Statement getInner() {
		return new Equality(arguments, atomicPredicates);
	}


}
