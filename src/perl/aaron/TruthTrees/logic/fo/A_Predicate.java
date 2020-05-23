package perl.aaron.TruthTrees.logic.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.A_Statement;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public abstract class A_Predicate extends A_Statement implements I_PredicateType {
	
	protected final List<A_LogicObject> arguments;
	
	public A_Predicate(final String typeName, final String symbol, final List<A_LogicObject> arguments) {
		super(typeName, symbol);
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	@Override
	public List<A_LogicObject> getArguments() {
		return arguments;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		var pred = (A_Predicate) other;
		return pred.symbol.equals(symbol) && pred.arguments.equals(arguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ arguments.hashCode();
	}
	
	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyNoDecomposition(branches);
	}
	
}
