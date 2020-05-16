package perl.aaron.TruthTrees.logic.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.negation.fo.NegPredicate;

public class Predicate extends APredicate implements NonDecomposable {
	public static final String TYPE_NAME = "Predicate";
	
	public Predicate(final String symbol, final List<LogicObject> arguments) {
		super(TYPE_NAME, symbol, arguments);
	}
	
	@Override
	public Predicate newInstance(final List<LogicObject> arguments) {
		return new Predicate(symbol, arguments);
	}
	
	@Override
	public Statement negated() {
		return new NegPredicate(symbol, arguments);
	}

}
