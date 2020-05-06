package perl.aaron.TruthTrees.logic.negation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.BranchDecomposable;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.Statement;

public class NegConjunction extends Negation implements BranchDecomposable {
	
	final List<Statement> decomposition;
	
	public NegConjunction(Statement...statements) {
		this(Arrays.asList(statements));
	}
	
	public NegConjunction(List<Statement> statements) {
		super(statements, new Conjunction(statements));
		decomposition = statements.stream().map(Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<Statement> getModelDecomposition(final Branch sourceBranch) {
		return decomposition;
	}

}
