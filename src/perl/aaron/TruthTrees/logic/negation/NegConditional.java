package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.A_ComplexStatement;
import perl.aaron.TruthTrees.logic.Conditional;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class NegConditional extends A_ComplexStatement implements I_ComplexNegation{
	public static final String TYPE_NAME = "Negated Conditional";
	
	private final List<I_Statement> decomposition;
	private final I_Statement left, right;
	
	public NegConditional(I_Statement a, I_Statement b) {
		super(TYPE_NAME, Conditional.SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		decomposition = List.of(a, b.negated());
		left = a;
		right = b;
	}
	
	@Override
	public I_Statement getInner() {
		return new Conditional(left, right);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifySerialDecomposition(branches, decomposition);
	}
	
}
