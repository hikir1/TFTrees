package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface Statement extends SymbolString {
	List<Statement> getModelDecomposition();

	void subVerifyDecomposition(final List<List<Statement>> branches, List<Statement> model) throws UserError;
	
	Statement negated();
	
}
