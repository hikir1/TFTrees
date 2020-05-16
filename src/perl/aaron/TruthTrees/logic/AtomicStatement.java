package perl.aaron.TruthTrees.logic;

import perl.aaron.TruthTrees.logic.negation.NegAtomicStatement;

public class AtomicStatement extends AStatement implements NonDecomposable {
	public static final String TYPE_NAME = "Atomic Statment";
	
	public AtomicStatement(String symbol){
		super(TYPE_NAME, symbol);
	}
	
	@Override
	public Statement negated() {
		return new NegAtomicStatement(symbol);
	}
	
}
