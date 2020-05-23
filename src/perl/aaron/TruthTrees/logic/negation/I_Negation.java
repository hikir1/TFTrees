package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.I_Statement;

public interface I_Negation extends I_Statement {
	public static final String SYMBOL = "\u00AC";
	
	I_Statement getInner();
	
	@Override
	default I_Statement negated() {
		return new DoubleNeg(getInner());
	}
	
	default String innerSymStringParen() {
		return I_Statement.super.symStringParen();
	}
	
	@Override
	default String symString() {
		return SYMBOL + innerSymStringParen();
	}

	@Override
	default String symStringParen() {
		return symString();
	}

}
