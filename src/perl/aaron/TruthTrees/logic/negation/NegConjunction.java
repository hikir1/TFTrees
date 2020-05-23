package perl.aaron.TruthTrees.logic.negation;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.A_ComplexStatement;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class NegConjunction extends A_ComplexStatement implements I_ComplexNegation {
	public static final String TYPE_NAME = "Negated Conjunction";
	
	final List<I_Statement> decomposition;
	
	public NegConjunction(I_Statement a, I_Statement b) {
		this(List.of(a,b));
	}
	
	public NegConjunction(List<I_Statement> statements) {
		super(TYPE_NAME, Conjunction.SYMBOL, List.copyOf(statements));
		assert statements != null;
		decomposition = statements.stream().map(I_Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public I_Statement getInner() {
		return new Conjunction(statements);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifyBranchDecomposition(branches, decomposition);
	}

}
