package perl.aaron.TruthTrees.logic.fo;

import perl.aaron.TruthTrees.logic.ASymbolString;

/**
 * A LogicObject represents some object in the universe of discourse. This could be a constant, variable or function object.
 */
public abstract class LogicObject extends ASymbolString {
	
	protected LogicObject(final String typeName, final String symbol) {
		super(typeName, symbol);
	}
	
}
