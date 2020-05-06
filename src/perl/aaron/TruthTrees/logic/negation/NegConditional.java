package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.Conditional;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegConditional extends Negation implements SerialDecomposable {
	
	private final List<Statement> decomposition;
	
	public NegConditional(Statement a, Statement b) {
		super(List.of(a, b), new Conditional(a, b));
		decomposition = List.of(a, b.negated());
	}

	@Override
	public List<Statement> getModelDecomposition(final Branch sourceBranch) {
		return decomposition;
	}

}
