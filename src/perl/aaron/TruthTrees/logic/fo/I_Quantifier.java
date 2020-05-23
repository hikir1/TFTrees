package perl.aaron.TruthTrees.logic.fo;

import perl.aaron.TruthTrees.logic.I_Statement;

public interface I_Quantifier extends I_Statement {
	
	Variable getVariable();
	I_Statement getStatement();
	
	@Override
	default String symString() {
		return getSymbol() + getVariable().symString() + " " + getStatement().symStringParen();
	}
	
}
