package perl.aaron.TruthTrees.logic.fo;

import perl.aaron.TruthTrees.logic.A_SymbolString;

/**
 * A LogicObject represents some object in the universe of discourse. This could be a constant, variable or function object.
 */
public abstract class A_LogicObject extends A_SymbolString {
	
	protected A_LogicObject(final String typeName, final String symbol) {
		super(typeName, symbol);
	}
	
}
