package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegBiconditional;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Biconditional extends Statement implements SerialDecomposable {
	public static final String TYPE_NAME = "Biconditional";
	public static final String SYMBOL = "\u2194";
	
	private final Statement left, right;
	private final List<Statement> decomposition;

	public Biconditional(Statement a, Statement b) {
		super(TYPE_NAME, SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		left = a;
		right = b;
		decomposition = List.of(new Conjunction(a,b), new Conjunction(a.negated(), b.negated()));
	}
	
	@Override
	public List<Statement> getModelDecomposition(Branch sourceBranch) {
		return decomposition;
	}
	
	@Override
	public Negation negated() {
		return new NegBiconditional(left, right);
	}

}
