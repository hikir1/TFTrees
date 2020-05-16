package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionSymbol extends LogicObject {
	public static final String TYPE_NAME = "Function";
	
	private final List<LogicObject> arguments;
	
	public FunctionSymbol(final String symbol, final List<LogicObject> arguments) {
		super(TYPE_NAME, symbol);
		assert arguments != null;
		this.arguments = List.copyOf(arguments);
	}
	
	List<LogicObject> getArguments() {
		// already immutable
		return arguments;
	}
	
	public String symString() {
		return arguments.stream().map(LogicObject::toString).collect(Collectors.joining(", ", symbol + "(", ")"));
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
