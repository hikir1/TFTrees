package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegConjunction;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Conjunction extends Statement implements SerialDecomposable {
	public static final String TYPE_NAME = "Conjunction";
	public static final String SYMBOL = "\u2227";
	
	public Conjunction(final Statement a, final Statement b) {
		this(List.of(a, b));
	}
	
	public Conjunction(final List<Statement> conjuncts) {
		super(TYPE_NAME, SYMBOL, List.copyOf(conjuncts));
		assert conjuncts != null;
	}
	
	@Override
	public List<Statement> getModelDecomposition(final Branch b) {
		// statements is already unmodifiable
		return statements;
	}
	
	@Override
	public Negation negated() {
		return new NegConjunction(statements);
	}

}
