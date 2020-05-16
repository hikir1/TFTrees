package perl.aaron.TruthTrees.logic.fo;

import java.util.LinkedHashSet;
import java.util.Set;

import perl.aaron.TruthTrees.logic.AStatement;
import perl.aaron.TruthTrees.util.UserError;

public abstract class Quantifier extends AStatement implements Decomposable {

	protected Variable var;
	protected AStatement statement;
	
	public Quantifier(Variable var, AStatement statement)
	{
		this.var = var;
		this.statement = statement;
	}

	@Override
	public Set<String> getVariables() {
		// Include all the unbound variables of the quantified statement
		Set<String> unbound = new LinkedHashSet<String>();
		unbound.addAll(statement.getVariables());
		// Bind the quantified variable
		unbound.removeAll(var.getVariables());
		return unbound;
	}
	
	@Override
	public Set<String> getConstants() {
		return statement.getConstants();
	}

	@Override
	public Binding determineBinding(AStatement unbound) throws UserError {
		if (!unbound.getClass().equals(this.getClass()))
			throw new UserError(this + " does not match " + unbound);
		Quantifier unboundQ = (Quantifier) unbound;
		if (!var.equals(unboundQ.var))
			throw new UserError(var + " does not match " + unboundQ.var);
		return statement.determineBinding(unboundQ.statement);
	}
	
	@Override
	public boolean equals(AStatement other)
	{
		System.out.println(getClass());
		System.out.println(other.getClass());
		if (getClass().equals(other.getClass()))
		{
			System.out.println("Same class");
			Quantifier otherQ = (Quantifier) other;
			return (var.equals(otherQ.var) && statement.equals(otherQ.statement));
		}
		else return false;
	}

	public Variable getQuantifiedVariable()
	{
		return var;
	}
	
	public AStatement getStatement()
	{
		return statement;
	}
	
}
