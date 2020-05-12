package perl.aaron.TruthTrees.logic.negation;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.BranchDecomposable;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.Statement;

public class NegConjunction extends Negation implements BranchDecomposable {
	public static final String TYPE_NAME = "Negated Conjunction";
	
	final List<Statement> decomposition;
	
	public NegConjunction(Statement a, Statement b) {
		this(List.of(a,b));
	}
	
	public NegConjunction(List<Statement> statements) {
		super(TYPE_NAME, Conjunction.SYMBOL, List.copyOf(statements));
		assert statements != null;
		decomposition = statements.stream().map(Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<Statement> getModelDecomposition(final Branch sourceBranch) {
		return decomposition;
	}
	
	@Override
	protected Statement getInner() {
		return new Conjunction(statements);
	}

}
