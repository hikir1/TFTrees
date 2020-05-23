package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegConjunction;
import perl.aaron.TruthTrees.util.UserError;

public class Conjunction extends A_CommutativeStatement {
	public static final String TYPE_NAME = "Conjunction";
	public static final String SYMBOL = "\u2227";
	
	public Conjunction(final I_Statement a, final I_Statement b) {
		this(List.of(a, b));
	}
	
	public Conjunction(final List<I_Statement> conjuncts) {
		super(TYPE_NAME, SYMBOL, List.copyOf(conjuncts));
		assert conjuncts != null;
	}
	
	@Override
	public I_Statement negated() {
		return new NegConjunction(statements);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifySerialDecomposition(branches, statements);
	}

}
