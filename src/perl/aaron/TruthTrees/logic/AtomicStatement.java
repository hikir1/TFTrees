package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.Set;

import perl.aaron.TruthTrees.logic.negation.NegAtomicStatement;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.util.UserError;

public class AtomicStatement extends Statement implements NonDecomposable {
	private String symbol;
	/**
	 * Creates an atomic statement with a given symbol
	 * @param symbol The character representing the statement
	 */
	public AtomicStatement(String symbol)
	{
		this.symbol = symbol;
	}
	
	@Override
	public Negation negated() {
		return new NegAtomicStatement(symbol);
	}
	
	/**
	 * Returns the statement's symbol
	 * @return The character representing the statement
	 */
	public String getSymbol()
	{
		return symbol;
	}
	
	public String toString()
	{
		return symbol;
	}
	
	public String toStringParen()
	{
		return symbol;
	}
	
	public boolean equals(Statement other)
	{
		if (!(other instanceof AtomicStatement))
			return false;
		return ((AtomicStatement)other).getSymbol().equals(symbol);
	}
	
	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}
	
	@Override
	public Set<String> getConstants() {
		return Collections.emptySet();
	}
	
	@Override
	public Binding determineBinding(Statement unbound) throws UserError {
		if (unbound.equals(this)) return Binding.EMPTY_BINDING;
		throw new UserError(this + " does not match " + unbound);
	}
	
}
