package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.util.UserError;

public interface Decomposable {
	
//	public void verifyDecomposition(
//			final List<List<Statement>> branches,
//			final Set<String> branchConstants,
//			final Set<String> constantsBefore) throws UserError;
	
// To be implemented later...	
	
	public List<Statement> getModelDecomposition(final Branch sourceBranch);
	
	/**
	 * Checks if a list of branches properly decomposes a statement
	 * @param branches A list of branches (each being a list of statements) to be verified
	 */
	default public void verifyDecomposition(
			final List<List<Statement>> branches,
			final Branch sourceBranch) throws UserError {
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
	
	public void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError;
}
