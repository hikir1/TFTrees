package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.util.Counter;

public abstract class ACommutativeStatement extends AComplexStatement {
	protected final Map<Statement,Integer> statementCounts;
	
	protected ACommutativeStatement(String typeName, String symbol, List<Statement> statements) {
		super(typeName, symbol, statements);
		statementCounts = Counter.count(statements);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		return statementCounts.equals(((ACommutativeStatement)other).statementCounts);
	}
	
	@Override
	public int hashCode() {
		return statementCounts.hashCode();
	}
}
