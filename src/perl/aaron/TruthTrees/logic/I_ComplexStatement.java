package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.stream.Collectors;

public interface I_ComplexStatement extends I_Statement, I_ComplexSymbolString.WithParen {

	List<I_Statement> getStatements();
	
	@Override
	default List<? extends I_SymbolString> getSymbolStrings() {
		return getStatements();
	}
	
	@Override
	default String symString() {
		return getStatements().stream().map(s -> s.symStringParen()).collect(Collectors.joining(getSymbol()));
	}

}
