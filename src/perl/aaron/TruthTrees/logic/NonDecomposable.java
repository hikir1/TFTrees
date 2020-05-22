package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface NonDecomposable extends Statement {

	@Override
	default void subVerifyDecomposition(final List<List<Statement>> branches) throws UserError {
		assert branches != null;
		if (!branches.isEmpty())
			throw new UserError(this + " is already decomposed.");
	}
	
}
