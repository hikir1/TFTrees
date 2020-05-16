package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.stream.Collectors;

public interface ComplexStatement extends Statement {

	List<Statement> getStatements();
	
	@Override
	default String symString() {
		return getStatements().stream().map(Statement::symStringParen).collect(Collectors.joining(getSymbol()));
	}
	
	@Override
	default String symStringParen() {
		return "(" + symString() + ")";
	}
}
