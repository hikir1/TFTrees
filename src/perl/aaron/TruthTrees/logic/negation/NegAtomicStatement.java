package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.AStatement;
import perl.aaron.TruthTrees.logic.AtomicStatement;
import perl.aaron.TruthTrees.logic.NonDecomposable;

public class NegAtomicStatement extends AStatement implements Negation, NonDecomposable {
	public static final String TYPE_NAME = "Negated " + AtomicStatement.TYPE_NAME;
	
	public NegAtomicStatement(final String symbol) {
		super(TYPE_NAME, symbol);
	}
	
	@Override
	public AStatement getInner() {
		return new AtomicStatement(symbol);
	}
}
