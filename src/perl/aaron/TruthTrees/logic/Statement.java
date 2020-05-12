package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.Negation;
import perl.aaron.TruthTrees.util.UserError;

public abstract class Statement implements Decomposable {
	public abstract Negation negated();
	
	protected final String typeName;
	protected final String symbol;
	protected final List<Statement> statements;
	
	protected Statement(final String typeName, final String symbol, final List<Statement> statements) {
		assert typeName != null;
		assert symbol != null;
		assert statements != null;
		this.typeName = typeName;
		this.symbol = symbol;
		this.statements = Collections.unmodifiableList(statements);
	}
	
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
		if (other == null)
			return false;
		if (!getClass().equals(other.getClass()))
			return false;
		var unmatched = new ArrayList<>(statements);
		for (var statement : ((Statement)other).statements)
			if(!unmatched.remove(statement))
				return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return (getClass().hashCode() << 2) + statements.stream().collect(Collectors.summingInt(s -> s.hashCode())); 
	}
	
	public String symString() {
		return statements.stream().map(Statement::symStringParen).collect(Collectors.joining(" " + symbol + " "));
	}
	
	/**
	 * Returns the statement as a string with parenthesis surrounding it
	 * @return The statement string w/ parenthesis
	 */
	public String symStringParen()
	{
		return "("+toString()+")";
	}
	
	@Override
	public String toString() {
		return typeName + " " + this;
	}
	
//	/**
//	 * Returns the list of unbound variables in this statement
//	 */
//	public Set<String> getVariables() {
//		var vars = new HashSet<String>();
//		for (Statement statement : statements)
//			vars.addAll(statement.getVariables());
//		return vars;
//		// TODO cache result
//	}
//	
//	/**
//	 * Returns the list of constants in this statement
//	 */
//	public Set<String> getConstants() {
//		var consts = new HashSet<String>();
//		for (Statement statement : statements)
//			consts.addAll(statement.getConstants());
//		return consts;
//		// TODO cache result
//	}
	
//	/**
//	 * Attempts to determine a binding that would make the unbound statement equivalent to this one.
//	 * @param unbound A statement containing an unbound variable that will be bound
//	 * @return The Binding that makes the statements equivalent or null if there is no such binding
//	 */
//	public Binding determineBinding(final Statement unbound) throws UserError {
//		assert unbound != null;
//		try {
//			if (!getClass().equals(unbound.getClass()))
//				throw new UserError("");
//			if (statements.size() != unbound.statements.size())
//				throw new UserError("Incompatible number of operands: " + statements.size() + " and " + unbound.statements.size());
//			Binding binding = Binding.EMPTY_BINDING;
//			for (int i = 0; i < statements.size(); i++) {
//				Binding b = statements.get(i).determineBinding(unbound.statements.get(i));
//				if (binding == Binding.EMPTY_BINDING)
//					binding = b;
//				else if(b != Binding.EMPTY_BINDING && !b.equals(binding))
//					throw new UserError("Different bindings: " + binding + ", " + b);
//			}
//			return binding;
//		}
//		catch(UserError e) {
//			throw new UserError(this + " does not match " + unbound + ".\n" + e.getMessage());
//		}
//	}
		
}
