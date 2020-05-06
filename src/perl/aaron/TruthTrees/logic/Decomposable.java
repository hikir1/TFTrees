package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.util.UserError;

public interface Decomposable {
	/**
	 * Checks if a list of branches properly decomposes a statement
	 * @param branches A list of branches (each being a list of statements) to be verified
	 * @param constants All constants in the leaf branch
	 * @param constantsBefore All constants before the statement selected
	 * @return True if the branches decompose the statement, false otherwise
	 */
	public void verifyDecomposition(List<List<Statement>> branches, Set<String> branchConstants, Set<String> constantsBefore) throws UserError;
}
