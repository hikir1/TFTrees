package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.Statement;

public interface Negation extends Statement {
	public static final String SYMBOL = "\u00AC";
	
	Statement getInner();
	
	@Override
	default Statement negated() {
		return new DoubleNeg(getInner());
	}
	
	default String innerSymStringParen() {
		return Statement.super.symStringParen();
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
