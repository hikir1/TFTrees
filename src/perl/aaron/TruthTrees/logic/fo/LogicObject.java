package perl.aaron.TruthTrees.logic.fo;

import java.util.Set;

import perl.aaron.TruthTrees.logic.Binding;
import perl.aaron.TruthTrees.util.UserError;

/**
 * A LogicObject represents some object in the universe of discourse. This could be a constant, variable or function object.
 */
public abstract class LogicObject {

	public abstract Set<String> getVariables();
	public abstract Set<String> getConstants();
	public abstract boolean equals(Object other);
	public abstract Binding determineBinding(LogicObject unbound) throws UserError;
}
