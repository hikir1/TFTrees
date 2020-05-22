package perl.aaron.TruthTrees.logic;

public interface SymbolString {
	
	default String symString() {
		return getSymbol();
	}
	
	default String symStringParen() {
		return symString();
	}
	
	String getSymbol();
}
