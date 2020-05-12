package perl.aaron.TruthTrees.logic.negation;

import java.util.List;

import perl.aaron.TruthTrees.logic.AtomicStatement;
import perl.aaron.TruthTrees.logic.NonDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegAtomicStatement extends Negation implements NonDecomposable {
	public static final String TYPE_NAME = "Negated Atomic Sentence";
	
	public NegAtomicStatement(final String symbol) {
		super(TYPE_NAME, symbol, List.of());
	}
	
	@Override
	protected Statement getInner() {
		return new AtomicStatement(symbol);
	}
	
	@Override
	protected String innerSymStringParen() {
		return symbol;
	}
}
