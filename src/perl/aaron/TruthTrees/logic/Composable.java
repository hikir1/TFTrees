package perl.aaron.TruthTrees.logic;

import java.util.Set;

import perl.aaron.TruthTrees.BranchLine;

public interface Composable {
	/**
	 * Checks if a list of branches properly decomposes a statement
	 * @param branches A list of branches (each being a list of statements) to be verified
	 * @param constants All constants in the leaf branch
	 * @param constantsBefore All constants before the statement selected
	 * @return True if the branches decompose the statement, false otherwise
	 */
	public abstract String verifyComposition(Set<BranchLine> selectedBranchLines);

}