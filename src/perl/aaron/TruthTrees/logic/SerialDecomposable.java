package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface SerialDecomposable extends ModelDecomposable {
	
	@Override
	default void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError {
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
			throw new UserError(this + " should produce " + model.size()
			+ " decomposed statements. " + statements.size() + " given.");
		for (final var statement : statements)
			if (!model.remove(statement))
				throw new UserError("Unexpected statement: " + statement);
	}
}
