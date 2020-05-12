package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.Conditional;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegConditional extends Negation implements SerialDecomposable {
	public static final String TYPE_NAME = "Negated Conditional";
	
	private final List<Statement> decomposition;
	private final Statement left, right;
	
	public NegConditional(Statement a, Statement b) {
		super(TYPE_NAME, Conditional.SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		decomposition = List.of(a, b.negated());
		left = a;
		right = b;
	}

	@Override
	public List<Statement> getModelDecomposition(final Branch sourceBranch) {
		return decomposition;
	}
	
	@Override
	protected Statement getInner() {
		return new Conditional(left, right);
	}
	
	// just like conditional, need to override equals() because not commutative
	// no need to override hashcode because still valid
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof NegConditional))
			return false;
		return statements.equals(((NegConditional)other).statements);
	}

}
