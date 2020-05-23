package perl.aaron.TruthTrees.logic;

public interface I_SymbolString {
	
	default String symString() {
		return getSymbol();
	}
	
	default String symStringParen() {
		return symString();
	}
	
	String getSymbol();
}
