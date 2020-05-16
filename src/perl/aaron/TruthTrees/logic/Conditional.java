package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegConditional;

public class Conditional extends AComplexStatement implements BranchDecomposable {
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
	public List<Statement> getModelDecomposition() {
		return decomposition;
	}
	
	@Override
	public Statement negated() {
		return new NegConditional(left, right);
	}
	
}
