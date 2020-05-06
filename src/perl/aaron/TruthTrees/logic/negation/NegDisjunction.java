package perl.aaron.TruthTrees.logic.negation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.Disjunction;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegDisjunction extends Negation implements SerialDecomposable {
	
	private final List<Statement> decomposition;
	
	public NegDisjunction(Statement...statements) {
		this(Arrays.asList(statements));
	}
	
	public NegDisjunction(List<Statement> statements) {
		super(statements, new Disjunction(statements));
		decomposition = statements.stream().map(Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<Statement> getModelDecomposition(Branch sourceBranch) {
		return decomposition;
	}

}
