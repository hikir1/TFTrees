package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.AComplexStatement;
import perl.aaron.TruthTrees.logic.Conditional;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegConditional extends AComplexStatement implements ComplexNegation, SerialDecomposable {
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
	public List<Statement> getModelDecomposition() {
		return decomposition;
	}
	
	@Override
	public Statement getInner() {
		return new Conditional(left, right);
	}
	
}
