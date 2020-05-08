package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegBiconditional;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Biconditional extends LogicalOperator implements SerialDecomposable {

	private final List<Statement> statements;
	private final List<Statement> decomposition;

	public Biconditional(Statement a, Statement b) {
		decomposition = List.of(new Conjunction(a,b), new Conjunction(a.negated(), b.negated()));
	}
	
	@Override
	public List<Statement> getModelDecomposition(Branch sourceBranch) {
		return decomposition;
	}
	
	@Override
	public Negation negated() {
		assert statements != null;
		assert statements.size() == 2;
		return new NegBiconditional(statements.get(0), statements.get(1));
	}

	@Override
	protected List<String> getStatements() {
		
	}

}
