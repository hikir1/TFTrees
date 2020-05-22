package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface ModelDecomposable extends Statement {
	
	List<Statement> getModelDecomposition();
	
	void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError;
	
	@Override
	default void subVerifyDecomposition(final List<List<Statement>> branches) throws UserError {
		List<Statement> model = getModelDecomposition();
		assert model != null : "getModelDecomposition() for '" + this + "' should not return null";
		subVerifyDecomposition(branches, model);
	}
}
