package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.util.UserError;

interface Decomposable {
	List<Statement> getModelDecomposition(final Branch sourceBranch);

	void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError;
}
