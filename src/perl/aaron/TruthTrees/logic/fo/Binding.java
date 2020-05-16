package perl.aaron.TruthTrees.logic.fo;

/**
 * A class that represents a binding of a given variable to a given constant
 */
public class Binding {
	public static final Binding EMPTY_BINDING = new Binding(new Constant(""), new Variable(""));
	private final Constant constant;
	private final Variable variable;
	
	/**
	 * Constructs a given binding of the given variable with the given constant
	 * @param constant The constant to bind to the variable 
	 * @param variable The variable to be bound
	 */
	public Binding(final Constant constant, final Variable variable) {
		assert constant != null;
		assert variable != null;
		this.constant = constant;
		this.variable = variable;
	}
	
	public Constant getConstant() {
		return constant;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	@Override
	public boolean equals(final Object other)
	{
		if (!(other instanceof Binding))
			return false;
		var otherB = (Binding) other;
		return (constant.equals(otherB.constant) && variable.equals(otherB.variable));
	}
	
	@Override
	public String toString() {
		return "Binding: " + variable + " binds to " + constant;
	}

}
