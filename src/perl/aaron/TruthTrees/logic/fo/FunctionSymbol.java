package perl.aaron.TruthTrees.logic.fo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.logic.Binding;
import perl.aaron.TruthTrees.util.UserError;

public class FunctionSymbol extends LogicObject {
	
	private String symbol;
	private List<LogicObject> arguments;
	
	public FunctionSymbol(String symbol, List<LogicObject> arguments)
	{
		this.symbol = symbol;
		this.arguments = new ArrayList<LogicObject>(arguments);
	}

	@Override
	public Set<String> getVariables() {
		Set<String> allVariables = new LinkedHashSet<String>();
		for (LogicObject obj : arguments)
		{
			allVariables.addAll(obj.getVariables());
		}
		return allVariables;
	}

	@Override
	public Set<String> getConstants() {
		Set<String> allConstants = new LinkedHashSet<String>();
		for (LogicObject obj : arguments)
		{
			allConstants.addAll(obj.getConstants());
		}
		if (getVariables().size() == 0)
			allConstants.add(toString());
		return allConstants;
	}
	
	public String toString()
	{
		String argString = "";
		for (int i = 0; i < arguments.size(); i++)
		{
			if (i > 0)
				argString += ", ";
			argString += arguments.get(0).toString();
		}
		return symbol + "(" + argString + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof FunctionSymbol)
		{
			FunctionSymbol otherFunction = (FunctionSymbol) other;
			return (otherFunction.symbol == this.symbol) && arguments.equals(otherFunction.arguments);
		}
		return false;
	}

	@Override
	public Binding determineBinding(LogicObject unbound) throws UserError {
		if (!(unbound instanceof FunctionSymbol))
			throw new UserError(this + " does not match " + unbound);
		FunctionSymbol unboundFunc = (FunctionSymbol) unbound;
		if (!symbol.equals(unboundFunc.symbol))
			throw new UserError("Incompatible functions: " + symbol + " & " + unboundFunc.symbol);
		if (arguments.size() != unboundFunc.arguments.size())
			throw new UserError("Incompatible numer of arguments: " + arguments.size() + " & " + unboundFunc.arguments.size());
		Binding b = Binding.EMPTY_BINDING;
		for (int i = 0; i < arguments.size(); i++)
		{
			Binding curBinding = arguments.get(i).determineBinding(unboundFunc.arguments.get(i));
			if (curBinding != Binding.EMPTY_BINDING) {
				if (b == Binding.EMPTY_BINDING)
					b = curBinding;
				else if (!b.equals(curBinding))
					throw new UserError("Different bindings: " + b + ", " + curBinding);
			}
		}
		return b;
	}

}
