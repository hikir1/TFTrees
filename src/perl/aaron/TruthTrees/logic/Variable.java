package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.Set;

import perl.aaron.TruthTrees.logic.fo.LogicObject;
import perl.aaron.TruthTrees.util.UserError;

public class Variable extends LogicObject {
	
	private String name;
	
	public Variable(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
	
	@Override
	public Set<String> getVariables()
	{
		return Collections.singleton(name);
	}

	@Override
	public Set<String> getConstants() {
		return Collections.emptySet();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Variable)
		{
			Variable otherVar = (Variable) other;
			return otherVar.name.equals(this.name);
		}
		return false;
	}

	@Override
	public Binding determineBinding(LogicObject unbound) throws UserError {
		if (this.equals(unbound))
			return Binding.EMPTY_BINDING;
		throw new UserError(this + " does not match " + unbound);
	}

}
