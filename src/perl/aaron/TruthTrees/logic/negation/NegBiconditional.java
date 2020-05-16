package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.AComplexStatement;
import perl.aaron.TruthTrees.logic.Biconditional;
import perl.aaron.TruthTrees.logic.BranchDecomposable;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.Statement;

public class NegBiconditional extends AComplexStatement implements ComplexNegation, BranchDecomposable {
	public static final String TYPE_NAME = "Negated Biconditional";
	
	private final List<Statement> decomposition;
	private final Statement left, right;
	
	public NegBiconditional(final Statement a, final Statement b) {
		super(TYPE_NAME, Biconditional.SYMBOL, List.of(a, b));
		assert a != null;
		assert b != null;
		decomposition = List.of(new Conjunction(a.negated(), b), new Conjunction(a, b.negated()));
		left = a;
		right = b;
	}
	
	@Override
	public List<Statement> getModelDecomposition() {
		return decomposition;
	}
	
	@Override
	public Statement getInner() {
		return new Biconditional(left, right);
	}
	
}
