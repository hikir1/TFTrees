package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.A_ComplexStatement;
import perl.aaron.TruthTrees.logic.Biconditional;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class NegBiconditional extends A_ComplexStatement implements I_ComplexNegation {
	public static final String TYPE_NAME = "Negated Biconditional";
	
	private final List<I_Statement> decomposition;
	private final I_Statement left, right;
	
	public NegBiconditional(final I_Statement a, final I_Statement b) {
		super(TYPE_NAME, Biconditional.SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		decomposition = List.of(new Conjunction(a.negated(), b), new Conjunction(a, b.negated()));
		left = a;
		right = b;
	}
	
	@Override
	public I_Statement getInner() {
		return new Biconditional(left, right);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyBranchDecomposition(branches, decomposition);
	}
	
}
