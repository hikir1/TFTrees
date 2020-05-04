package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public abstract class LogicalOperator extends Statement implements Decomposable {
	
	protected List<Statement> statements;
	
	/**
	 * Returns the list of connected operands
	 * @return
	 */
	public List<Statement> getOperands() {
		return Collections.unmodifiableList(statements);
	}

	@Override
	public Binding determineBinding(Statement unbound) throws UserError
	{
		if (!unbound.getClass().equals(this.getClass()))
			throw new UserError(this + " does not match " + unbound);
		LogicalOperator unboundOp = (LogicalOperator) unbound;
		if (unboundOp.statements.size() != statements.size())
			throw new UserError(this + " does not match " + unbound + ". Incompatible number of operands.");
		Binding b = Binding.EMPTY_BINDING; //TODO: get rid of this
		for (int i = 0; i < statements.size(); i++)
		{
			Binding curBinding = statements.get(i).determineBinding(unboundOp.statements.get(i));
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
