package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.util.UserError;

public abstract class Statement implements Decomposable {
	public abstract Negation negated();
	
	protected abstract List<Statement> getStatements();
	protected abstract String getSymbol();
	
	public final void verifyDecomposition(final List<List<Statement>> branches, final Branch sourceBranch) throws UserError {
		assert branches != null;
		assert sourceBranch != null;		

		final var model = getModelDecomposition(sourceBranch);
		assert model != null : "getModelDecomposition() for '" + this + "' should not return null";
		try {
			subVerifyDecomposition(branches, model);
		}
		catch(UserError e) {
			throw new UserError("Invalid decomposition of " + this + ":\n" + e.getMessage());
		}
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!getClass().equals(other.getClass()))
			return false;
		var unmatched = new ArrayList<>(getStatements());
		for (var statement : ((Statement)other).getStatements())
			if(!unmatched.remove(statement))
				return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (getClass().hashCode() << 2) + getStatements().stream().collect(Collectors.summingInt(s -> s.hashCode())); 
	}
	
	@Override
	public String toString() {
		return getStatements().stream().map(Statement::toStringParen).collect(Collectors.joining(" " + getSymbol() + " "));
	}
	
	/**
	 * Returns the statement as a string with parenthesis surrounding it
	 * @return The statement string w/ parenthesis
	 */
	public String toStringParen()
	{
		return "("+toString()+")";
	}
	
	/**
	 * Returns the list of unbound variables in this statement
	 */
	public abstract Set<String> getVariables();
	
	/**
	 * Returns the list of constants in this statement
	 */
	public abstract Set<String> getConstants();
	
	/**
	 * Attempts to determine a binding that would make the unbound statement equivalent to this one.
	 * @param unbound A statement containing an unbound variable that will be bound
	 * @return The Binding that makes the statements equivalent or null if there is no such binding
	 */
	public abstract Binding determineBinding(Statement unbound) throws UserError;
		
}
