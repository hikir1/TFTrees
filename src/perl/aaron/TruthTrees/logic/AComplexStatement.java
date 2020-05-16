package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.List;

public abstract class AComplexStatement extends AStatement implements ComplexStatement {
	protected final List<Statement> statements;
	
	public AComplexStatement(String typeName, String symbol, List<Statement> statements) {
		super(typeName, symbol);
		assert statements != null;
		this.statements = statements;
	}
	
	@Override
	public List<Statement> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		return statements.equals(((AComplexStatement)other).statements);
	}
	
	@Override
	public int hashCode() {
		return statements.hashCode();
	}
}
