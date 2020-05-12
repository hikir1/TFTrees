package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.LogicObject;
import perl.aaron.TruthTrees.logic.fo.Predicate;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class NegPredicate extends Negation implements NonDecomposable{
	public static final String TYPE_NAME = "Negated Predicate";
	
	private final List<LogicObject> arguments;
	
	public NegPredicate(String symbol, List<LogicObject> arguments) {
		super(TYPE_NAME, symbol, List.of());
		assert symbol != null;
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	@Override
	protected Statement getInner() {
		return new Predicate(symbol, arguments);
	}
	
	@Override
	protected String innerSymStringParen() {
		return arguments.stream().map(Object::toString).collect(Collectors.joining(", ", symbol + "(", ")"));
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof NegPredicate))
			return false;
		var negpred = (NegPredicate) other;
		return negpred.symbol.equals(symbol) && negpred.arguments.equals(arguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ arguments.hashCode();
	}
}
