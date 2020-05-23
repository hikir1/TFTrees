package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegBiconditional;
import perl.aaron.TruthTrees.util.UserError;

public class Biconditional extends A_CommutativeStatement {
	public static final String TYPE_NAME = "Biconditional";
	public static final String SYMBOL = "\u2194";
	
	private final I_Statement left, right;
	private final List<I_Statement> decomposition;

	public Biconditional(I_Statement a, I_Statement b) {
		super(TYPE_NAME, SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		left = a;
		right = b;
		decomposition = List.of(new Conjunction(a,b), new Conjunction(a.negated(), b.negated()));
	}
	
	
	@Override
	public I_Statement negated() {
		return new NegBiconditional(left, right);
	}


	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyBranchDecomposition(branches, decomposition);
	}

}
