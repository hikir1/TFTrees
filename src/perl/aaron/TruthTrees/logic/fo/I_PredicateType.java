package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.I_ComplexSymbolString;
import perl.aaron.TruthTrees.logic.I_Statement;

public interface I_PredicateType extends I_Statement, I_ComplexSymbolString {
	
	public List<A_LogicObject> getArguments();
	
	// used by Equation
	public I_PredicateType withArgs(List<A_LogicObject> arguments);
	
	@Override
	default public List<A_LogicObject> getSymbolStrings() {
		return getArguments();
	}
	
	@Override
	default public String symString() {
		return getArguments().stream().map(arg -> arg.symStringParen()).collect(Collectors.joining(", ", getSymbol() + "(", ")"));
	}
	
	// symStringParen() default is already correct (no parenthesis)
	
}
