package perl.aaron.TruthTrees.logic;

import java.util.List;

public interface I_ComplexSymbolString extends I_SymbolString {
	
	List<? extends I_SymbolString> getSymbolStrings();
	
	public static interface WithParen extends I_ComplexSymbolString {
		@Override
		default String symStringParen() {
			return "(" + symString() + ")";
		}
	}
	
}
