package perl.aaron.TruthTrees.logic.negation;

import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegAtomicStatement extends Negation implements NonDecomposable {
	
	private final String symbol;
	
	public NegAtomicStatement(final String symbol) {
		
		this.symbol = symbol;
	}
	
}
