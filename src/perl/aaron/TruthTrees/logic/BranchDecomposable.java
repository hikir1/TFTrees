package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface BranchDecomposable extends Decomposable {
	
	@Override
	default public void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError {
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
	
}
