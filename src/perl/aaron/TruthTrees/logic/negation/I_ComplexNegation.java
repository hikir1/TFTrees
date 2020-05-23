package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.I_ComplexStatement;

public interface I_ComplexNegation extends I_Negation, I_ComplexStatement {

	@Override
	default String symString() {
		return I_Negation.super.symString();
	}
	
	@Override
	default String symStringParen() {
		return I_Negation.super.symStringParen();
	}
	
	@Override
	default String innerSymStringParen() {
		return I_ComplexStatement.super.symStringParen();
	}
}
