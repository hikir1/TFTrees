package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegConditional;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Conditional extends Statement implements BranchDecomposable {
	public static final String TYPE_NAME = "Conditional";
	public static final String SYMBOL = "\u2192";
	
	private final List<Statement> decomposition;
	private final Statement left, right;

	public Conditional(final Statement a, final Statement b)
	{
		super(TYPE_NAME, SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		left = a;
		right = b;
		decomposition = List.of(a.negated(), b);
	}
	
	@Override
	public List<Statement> getModelDecomposition(final Branch b) {
		return decomposition;
	}
	
	@Override
	public Negation negated() {
		return new NegConditional(left, right);
	}
	
	// need to override equals() because not commutative
	// no need to override hashcode because still valid
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Conditional))
			return false;
		return statements.equals(((Conditional)other).statements);
	}
	
}
