package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.util.UserError;

public abstract class A_Statement extends A_SymbolString implements I_Statement {
	
	protected abstract void subVerifyDecomposition(final List<List<I_Statement>> branches) throws UserError;
	
	protected A_Statement(final String typeName, final String symbol) {
		super(typeName, symbol);
	}
	
	public final void verifyDecomposition(final List<List<I_Statement>> branches, final Branch sourceBranch) throws UserError {
		assert branches != null;
		assert sourceBranch != null;

		try {
			subVerifyDecomposition(branches);
		}
		catch(UserError e) {
			throw new UserError("Invalid decomposition of " + this + ":\n" + e.getMessage());
		}
	}
	
	public static void verifyNoDecomposition(final List<List<I_Statement>> branches) throws UserError {
		assert branches != null;
		if (!branches.isEmpty())
			throw new UserError("This statement is already decomposed.");
	}
	
	public static void verifyBranchDecomposition(final List<List<I_Statement>> branches, List<I_Statement> model) throws UserError {
		assert branches != null;
		assert model != null;
		assert !model.isEmpty() : "model should not be empty";
		// make a mutible copy of model
		model = new ArrayList<>(model);
		if (branches.size() != model.size())
			throw new UserError("There must be one branch per operand. "
					+ model.size() + " expected. " + branches.size() + " given.");
		for (final var branch : branches) {
			if (branch.size() != 1)
				throw new UserError("Each branch must contain exactly 1 decomposed statement. "
						+ branch.size() + " given.");
			if (!model.remove(branch.get(0)))
				throw new UserError("Unexpected statement: " + branch.get(0));
		}
	}
	
	public static void verifySerialDecomposition(final List<List<I_Statement>> branches, List<I_Statement> model) throws UserError {
		assert branches != null;
		assert model != null;
		assert !model.isEmpty() : "model should not be empty";
		// make a mutible copy
		model = new ArrayList<>(model);
		if (branches.isEmpty())
			throw new UserError("Not decomposed");
		if (branches.size() > 1)
			throw new UserError("Decomposition of this statement should not produce branches.");
		final var statements = branches.get(0);
		if(statements.size() != model.size())
			throw new UserError("Expected " + model.size()
			+ " decomposed statements. " + statements.size() + " given.");
		for (final var statement : statements)
			if (!model.remove(statement))
				throw new UserError("Unexpected statement: " + statement);
	}
	
}
