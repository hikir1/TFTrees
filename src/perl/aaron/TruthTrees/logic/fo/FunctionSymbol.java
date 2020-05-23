package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.I_ComplexSymbolString;
import perl.aaron.TruthTrees.logic.I_SymbolString;

public class FunctionSymbol extends A_LogicObject implements I_ComplexSymbolString {
	public static final String TYPE_NAME = "Function";
	
	private final List<A_LogicObject> arguments;
	
	public FunctionSymbol(final String symbol, final List<A_LogicObject> arguments) {
		super(TYPE_NAME, symbol);
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	List<A_LogicObject> getArguments() {
		// already immutable
		return arguments;
	}
	
	@Override
	public List<? extends I_SymbolString> getSymbolStrings() {
		return arguments;
	}
	
	public String symString() {
		return arguments.stream().map(A_LogicObject::toString).collect(Collectors.joining(", ", symbol + "(", ")"));
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof FunctionSymbol))
			return false;
		var ofunc = (FunctionSymbol) other;
		return ofunc.symbol.equals(symbol) && ofunc.arguments.equals(arguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ arguments.hashCode();
	}
	
}
