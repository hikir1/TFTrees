package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.ComplexStatement;

public interface ComplexNegation extends Negation, ComplexStatement {

	@Override
	default String symString() {
		return Negation.super.symString();
	}
	
	@Override
	default String symStringParen() {
		return Negation.super.symStringParen();
	}
	
	@Override
	default String innerSymStringParen() {
		return ComplexStatement.super.symStringParen();
	}
}
