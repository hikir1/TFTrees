package perl.aaron.TruthTrees.logic.fo;

import java.util.List;

import perl.aaron.TruthTrees.logic.AStatement;

public abstract class APredicate extends AStatement implements PredicateType {
	
	protected final List<LogicObject> arguments;
	
	public APredicate(final String typeName, final String symbol, final List<LogicObject> arguments) {
		super(typeName, symbol);
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	@Override
	public List<LogicObject> getArguments() {
		return arguments;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		var pred = (APredicate) other;
		return pred.symbol.equals(symbol) && pred.arguments.equals(arguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ arguments.hashCode();
	}
	
}
