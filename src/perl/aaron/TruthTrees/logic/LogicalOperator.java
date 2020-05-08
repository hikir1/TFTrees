package perl.aaron.TruthTrees.logic;

import perl.aaron.TruthTrees.util.UserError;

public abstract class LogicalOperator extends Statement {
	
	@Override
	public Binding determineBinding(Statement unbound) throws UserError
	{
		if (!unbound.getClass().equals(this.getClass()))
			throw new UserError(this + " does not match " + unbound);
		final var statements = getStatements();
		final var unboundStatements = unbound.getStatements();
		if (unboundStatements.size() != statements.size())
			throw new UserError(this + " does not match " + unbound + ". Incompatible number of operands.");
		Binding b = Binding.EMPTY_BINDING; //TODO: get rid of this
		for (int i = 0; i < statements.size(); i++)
		{
			Binding curBinding = statements.get(i).determineBinding(unboundStatements.get(i));
			if (curBinding != Binding.EMPTY_BINDING) {
				if (b == Binding.EMPTY_BINDING)
					b = curBinding;
				else if (!b.equals(curBinding))
					throw new UserError("Different bindings: " + b + ", " + curBinding);
			}
		}
		return b;
	}

}
