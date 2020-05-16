package perl.aaron.TruthTrees.logic.fo;

/**
 * A constant represents a name for exactly one specific object.
 * E.x. john, marie, a, b, c1, c2 etc.
 */
public class Constant extends LogicObject {
	public static final String TYPE_NAME = "Constant";
	
	public Constant(String symbol) {
		super(TYPE_NAME, symbol);
	}

}
