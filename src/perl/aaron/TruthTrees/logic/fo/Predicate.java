package perl.aaron.TruthTrees.logic.fo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.logic.Binding;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.util.UserError;

public class Predicate extends Statement {
	private String symbol;
	private List<LogicObject> arguments;

	public Predicate(String symbol, List<LogicObject> arguments)
	{
		this.symbol = symbol;
		this.arguments = new ArrayList<LogicObject>(arguments);
	}
	
	@Override
	public String toString()
	{
		String argString = "";
		for (int i = 0; i < arguments.size(); i++)
		{
			if (i > 0)
				argString += ", ";
			argString += arguments.get(i).toString();
		}
		return symbol + "(" + argString + ")";
	}
	
	public String toStringParen()
	{
		return toString();
	}

	@Override
	public boolean equals(Statement other) {
		if (other instanceof Predicate)
		{
			Predicate otherPredicate = (Predicate) other;
			return symbol.equals(otherPredicate.symbol) && arguments.equals(otherPredicate.arguments);
		}
		return false;
	}

	@Override
	public Set<String> getVariables() {
		Set<String> union = new LinkedHashSet<String>();
		for (LogicObject arg : arguments)
		{
			union.addAll(arg.getVariables());
		}
		return union;
	}
	
	@Override
	public Set<String> getConstants() {
		Set<String> union = new LinkedHashSet<String>();
		for (LogicObject arg : arguments)
		{
			union.addAll(arg.getConstants());
		}
		return union;
	}

	@Override
	public Binding determineBinding(Statement unbound) throws UserError
	{
		if (!unbound.getClass().equals(this.getClass()))
			throw new UserError(this + " does not match " + unbound);
		Predicate unboundPred = (Predicate) unbound;
		if (arguments.size() != unboundPred.arguments.size())
			throw new UserError(this + " does not match " + unbound + ". Incompatible number of arguments.");
		Binding b = Binding.EMPTY_BINDING;
		for (int i = 0; i < arguments.size(); i++)
		{
			Binding curBinding = arguments.get(i).determineBinding(unboundPred.arguments.get(i));
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
