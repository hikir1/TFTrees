package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.util.UserError;

public abstract class AStatement extends ASymbolString implements Statement {
	
	protected AStatement(final String typeName, final String symbol) {
		super(typeName, symbol);
	}
	
	public final void verifyDecomposition(final List<List<Statement>> branches, final Branch sourceBranch) throws UserError {
		assert branches != null;
		assert sourceBranch != null;		

		final var model = getModelDecomposition();
		assert model != null : "getModelDecomposition() for '" + this + "' should not return null";
		try {
			subVerifyDecomposition(branches, model);
		}
		catch(UserError e) {
			throw new UserError("Invalid decomposition of " + this + ":\n" + e.getMessage());
		}
	}
	
}
