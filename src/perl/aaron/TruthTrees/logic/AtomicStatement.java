package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegAtomicStatement;
import perl.aaron.TruthTrees.util.UserError;

public class AtomicStatement extends A_Statement {
	public static final String TYPE_NAME = "Atomic Statment";
	
	public AtomicStatement(String symbol){
		super(TYPE_NAME, symbol);
	}
	
	@Override
	public I_Statement negated() {
		return new NegAtomicStatement(symbol);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyNoDecomposition(branches);
	}
	
}
