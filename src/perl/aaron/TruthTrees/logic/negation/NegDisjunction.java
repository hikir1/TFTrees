package perl.aaron.TruthTrees.logic.negation;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.A_ComplexStatement;
import perl.aaron.TruthTrees.logic.Disjunction;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.util.UserError;

public class NegDisjunction extends A_ComplexStatement implements I_ComplexNegation {
	public static final String TYPE_NAME = "Negated Disjunction";
	
	private final List<I_Statement> decomposition;
	
	public NegDisjunction(I_Statement a, I_Statement b) {
		this(List.of(a, b));
	}
	
	public NegDisjunction(List<I_Statement> statements) {
		super(TYPE_NAME, Disjunction.SYMBOL, List.copyOf(statements));
		assert statements != null;
		decomposition = statements.stream().map(I_Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public I_Statement getInner() {
		return new Disjunction(statements);
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifySerialDecomposition(branches, decomposition);
	}

}
