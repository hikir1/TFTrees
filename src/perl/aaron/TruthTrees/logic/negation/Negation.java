package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.Statement;

public abstract class Negation extends Statement {
	public static final String SYMBOL = "\u00AC";
	
	protected abstract Statement getInner();
	
	/**
	 * Creates a Negation of a given statement
	 * @param proposition The Statement to be negated
	 */
	public Negation(final String name, final String innerSymbol, final List<Statement> statements)
	{
		super(name, innerSymbol, statements);
	}
	
	@Override
	public final Negation negated() {
		return new DoubleNeg(getInner());
	}
	
	// to be overridden by subclasses
	protected String innerSymStringParen() {
		return super.symStringParen();
	}
	
	@Override
	public final String symString() {
		return SYMBOL + innerSymStringParen();
	}

	@Override
	public final String symStringParen() {
		return symString();
	}

}
