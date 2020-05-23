package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.fo.A_LogicObject;
import perl.aaron.TruthTrees.logic.fo.A_Predicate;
import perl.aaron.TruthTrees.logic.fo.Predicate;
import perl.aaron.TruthTrees.logic.negation.I_Negation;

public class NegPredicate extends A_Predicate implements I_Negation {
	public static final String TYPE_NAME = "Negated Predicate";
	
	public NegPredicate(String symbol, List<A_LogicObject> arguments) {
		super(TYPE_NAME, symbol, arguments);
	}
	
	@Override
	public I_Statement getInner() {
		return new Predicate(symbol, arguments);
	}
	
	@Override
	public String innerSymStringParen() {
		return super.symStringParen();
	}
	
	@Override
	public A_Predicate withArgs(List<A_LogicObject> arguments) {
		return new NegPredicate(symbol, arguments);
	}

	@Override
	public String symString() {
		return I_Negation.super.symString();
	}
	
}
