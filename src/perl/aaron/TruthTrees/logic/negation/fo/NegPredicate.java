package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.APredicate;
import perl.aaron.TruthTrees.logic.fo.LogicObject;
import perl.aaron.TruthTrees.logic.fo.Predicate;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class NegPredicate extends APredicate implements Negation, NonDecomposable{
	public static final String TYPE_NAME = "Negated Predicate";
	
	public NegPredicate(String symbol, List<LogicObject> arguments) {
		super(TYPE_NAME, symbol, arguments);
	}
	
	@Override
	public Statement getInner() {
		return new Predicate(symbol, arguments);
	}
	
	@Override
	public String innerSymStringParen() {
		return super.symStringParen();
	}
	
	@Override
	public APredicate withArgs(List<LogicObject> arguments) {
		return new NegPredicate(symbol, arguments);
	}

	@Override
	public String symString() {
		return Negation.super.symString();
	}
	
}
