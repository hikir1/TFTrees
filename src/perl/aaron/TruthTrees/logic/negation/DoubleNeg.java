package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class DoubleNeg extends Negation implements SerialDecomposable {
	
	private final List<Statement> decomposition;
	
	public DoubleNeg(final Statement a) {
		super(List.of(a), a.negated());
		decomposition = List.of(a);
	}

	@Override
	public List<Statement> getModelDecomposition(final Branch sourceBranch) {
		return decomposition;
	}

}
