package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.logic.negation.NegConjunction;

public class Conjunction extends ACommutativeStatement implements SerialDecomposable {
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
	public List<Statement> getModelDecomposition() {
		// statements is already unmodifiable
		return statements;
	}
	
	@Override
	public Statement negated() {
		return new NegConjunction(statements);
	}

}
