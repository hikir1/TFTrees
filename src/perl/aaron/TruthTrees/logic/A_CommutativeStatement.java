package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.util.Counter;

public abstract class A_CommutativeStatement extends A_ComplexStatement {
	protected final Map<I_Statement,Integer> statementCounts;
	
	protected A_CommutativeStatement(String typeName, String symbol, List<I_Statement> statements) {
		super(typeName, symbol, statements);
		statementCounts = Counter.count(statements);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		return statementCounts.equals(((A_CommutativeStatement)other).statementCounts);
	}
	
	@Override
	public int hashCode() {
		return statementCounts.hashCode();
	}
}