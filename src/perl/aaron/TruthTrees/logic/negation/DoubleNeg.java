package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.A_ComplexStatement;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class DoubleNeg extends A_ComplexStatement implements I_ComplexNegation {
	public static final String TYPE_NAME = "Double Negation";
	
	private final List<I_Statement> decomposition;
	private final I_Statement negand;
	
	public DoubleNeg(final I_Statement a) {
		super(TYPE_NAME, I_Negation.SYMBOL, List.of(a));
		assert a != null;
		decomposition = List.of(a);
		negand = a;
	}
	
	@Override
	public I_Statement getInner() {
		return negand;
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifySerialDecomposition(branches, decomposition);
	}

}
