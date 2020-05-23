package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegDisjunction;
import perl.aaron.TruthTrees.util.UserError;

public class Disjunction extends A_CommutativeStatement {
	public static final String TYPE_NAME = "Disjunction";
	public static final String SYMBOL = "\u2228";
	
	public Disjunction(final I_Statement a, final I_Statement b) {
		this(List.of(a, b));
	}
	
	public Disjunction(final List<I_Statement> disjuncts) {
		super(TYPE_NAME, SYMBOL, List.copyOf(disjuncts));
	}
	
	@Override
	public I_Statement negated() {
		return new NegDisjunction(statements);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyBranchDecomposition(branches, statements);
	}

}
