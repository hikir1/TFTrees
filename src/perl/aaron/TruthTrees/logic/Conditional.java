package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegConditional;
import perl.aaron.TruthTrees.util.UserError;

public class Conditional extends A_ComplexStatement {
	public static final String TYPE_NAME = "Conditional";
	public static final String SYMBOL = "\u2192";
	
	private final List<I_Statement> decomposition;
	private final I_Statement left, right;

	public Conditional(final I_Statement a, final I_Statement b)
	{
		super(TYPE_NAME, SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		left = a;
		right = b;
		decomposition = List.of(a.negated(), b);
	}

	
	@Override
	public I_Statement negated() {
		return new NegConditional(left, right);
	}


	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyBranchDecomposition(branches, decomposition);
	}
	
}
