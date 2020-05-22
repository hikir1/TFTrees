package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.ComplexSymbolString;
import perl.aaron.TruthTrees.logic.Statement;

public interface PredicateType extends Statement, ComplexSymbolString {
	
	public List<LogicObject> getArguments();
	
	// used by Equation
	public PredicateType withArgs(List<LogicObject> arguments);
	
	@Override
	default public List<LogicObject> getSymbolStrings() {
		return getArguments();
	}
	
	@Override
	default public String symString() {
		return getArguments().stream().map(arg -> arg.symStringParen()).collect(Collectors.joining(", ", getSymbol() + "(", ")"));
	}
	
	// symStringParen() default is already correct (no parenthesis)
	
}
