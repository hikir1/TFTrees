package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.Biconditional;
import perl.aaron.TruthTrees.logic.BranchDecomposable;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.Statement;

public class NegBiconditional extends Negation implements BranchDecomposable {
	
	private final List<Statement> decomposition;
	
	public NegBiconditional(final Statement a, final Statement b) {
		super(List.of(a, b), new Biconditional(a, b));
		decomposition = List.of(new Conjunction(a.negated(), b), new Conjunction(a, b.negated()));
	}
	
	@Override
	public List<Statement> getModelDecomposition(final Branch b) {
		return decomposition;
	}
	
}
