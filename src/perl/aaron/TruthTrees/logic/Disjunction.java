package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegDisjunction;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Disjunction extends Statement implements BranchDecomposable {
	public static final String TYPE_NAME = "Disjunction";
	public static final String SYMBOL = "\u2228";
	
	public Disjunction(final Statement a, final Statement b) {
		this(List.of(a, b));
	}
	
	public Disjunction(final List<Statement> disjuncts) {
		super(TYPE_NAME, SYMBOL, List.copyOf(disjuncts));
	}
	
	@Override
	public List<Statement> getModelDecomposition(final Branch b) {
		// statements is already unmodifiable
		return statements;
	}
	
	@Override
	public Negation negated() {
		assert statements != null;
		return new NegDisjunction(statements);
	}

}
