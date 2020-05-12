package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegAtomicStatement;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class AtomicStatement extends Statement implements NonDecomposable {
	public static final String TYPE_NAME = "Atomic Statment";
	/**
	 * Creates an atomic statement with a given symbol
	 * @param symbol The character representing the statement
	 */
	public AtomicStatement(String symbol)
	{
		super(TYPE_NAME, symbol, List.of());
	}
	
	@Override
	public Negation negated() {
		return new NegAtomicStatement(symbol);
	}
	
	@Override
	public String symString()
	{
		return symbol;
	}
	
	@Override
	public String symStringParen()
	{
		return symbol;
	}
	
}
