package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.util.UserError;

public interface Statement extends SymbolString {

	void subVerifyDecomposition(final List<List<Statement>> branches) throws UserError;
	
	Statement negated();
	
}
