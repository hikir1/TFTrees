package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.A_Statement;
import perl.aaron.TruthTrees.logic.AtomicStatement;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class NegAtomicStatement extends A_Statement implements I_Negation {
	public static final String TYPE_NAME = "Negated " + AtomicStatement.TYPE_NAME;
	
	public NegAtomicStatement(final String symbol) {
		super(TYPE_NAME, symbol);
	}
	
	@Override
	public A_Statement getInner() {
		return new AtomicStatement(symbol);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyNoDecomposition(branches);
	}
}
