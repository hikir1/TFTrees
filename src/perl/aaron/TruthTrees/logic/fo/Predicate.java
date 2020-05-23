package perl.aaron.TruthTrees.logic.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.negation.fo.NegPredicate;

public class Predicate extends A_Predicate {
	public static final String TYPE_NAME = "Predicate";
	
	public Predicate(final String symbol, final List<A_LogicObject> arguments) {
		super(TYPE_NAME, symbol, arguments);
	}
	
	@Override
	public Predicate withArgs(final List<A_LogicObject> arguments) {
		return new Predicate(symbol, arguments);
	}
	
	@Override
	public I_Statement negated() {
		return new NegPredicate(symbol, arguments);
	}

}
