package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface NonDecomposable extends Decomposable {

	@Override
	default public void subVerifyDecomposition(final List<List<Statement>> branches, final List<Statement> model) throws UserError {
		assert branches != null;
		assert model != null;
		assert model.isEmpty() : "model should be empty";
		if (!branches.isEmpty())
			throw new UserError(this + " is already decomposed.");
	}
	
}
