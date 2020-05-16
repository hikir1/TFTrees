package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.AComplexStatement;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class DoubleNeg extends AComplexStatement implements ComplexNegation, SerialDecomposable {
	public static final String TYPE_NAME = "Double Negation";
	
	private final List<Statement> decomposition;
	private final Statement negand;
	
	public DoubleNeg(final Statement a) {
		super(TYPE_NAME, Negation.SYMBOL, List.of(a));
		assert a != null;
		decomposition = List.of(a);
		negand = a;
	}
	
	@Override
	public Statement getInner() {
		return negand;
	}

	@Override
	public List<Statement> getModelDecomposition() {
		return decomposition;
	}

}
