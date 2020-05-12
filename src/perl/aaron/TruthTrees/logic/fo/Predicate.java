package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.logic.negation.fo.NegPredicate;

public class Predicate extends Statement implements NonDecomposable{
	public static final String TYPE_NAME = "Predicate";
	
	private final List<LogicObject> arguments;

	public Predicate(String symbol, List<LogicObject> arguments) {
		super(TYPE_NAME, symbol, List.of());
		assert symbol != null;
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	@Override
	public Negation negated() {
		return new NegPredicate(symbol, arguments);
	}
	
	@Override
	public String symString() {
		return arguments.stream().map(Object::toString).collect(Collectors.joining(", ", symbol + "(", ")"));
	}
	
	@Override
	public String symStringParen() {
		return symString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Predicate))
			return false;
		var pred = (Predicate) other;
		return pred.symbol.equals(symbol) && pred.arguments.equals(arguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ arguments.hashCode();
	}

}
