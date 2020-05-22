package perl.aaron.TruthTrees.logic.fo;

import perl.aaron.TruthTrees.logic.Statement;

public interface Quantifier extends Statement {
	
	Variable getVariable();
	Statement getStatement();
	
	@Override
	default String symString() {
		return getSymbol() + getVariable().symString() + " " + getStatement().symStringParen();
	}
	
}
