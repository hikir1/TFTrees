package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.List;

public abstract class A_ComplexStatement extends A_Statement implements I_ComplexStatement {
	protected final List<I_Statement> statements;
	
	public A_ComplexStatement(String typeName, String symbol, List<I_Statement> statements) {
		super(typeName, symbol);
		assert statements != null;
		this.statements = statements;
	}
	
	@Override
	public List<I_Statement> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		return statements.equals(((A_ComplexStatement)other).statements);
	}
	
	@Override
	public int hashCode() {
		return statements.hashCode();
	}
	
}
