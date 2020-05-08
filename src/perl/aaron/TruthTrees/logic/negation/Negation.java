package perl.aaron.TruthTrees.logic.negation;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.logic.LogicalOperator;
import perl.aaron.TruthTrees.logic.Statement;

public abstract class Negation extends LogicalOperator {
	
	protected final Statement appearance;
	
	/**
	 * Creates a Negation of a given statement
	 * @param proposition The Statement to be negated
	 */
	public Negation(List<Statement> operands, Statement appearance)
	{
		statements = Collections.unmodifiableList(operands);
		this.appearance = appearance;
	}
	
	@Override
	public Negation negated() {
		assert statements != null;
		assert statements.size() == 2;
		return new DoubleNeg(appearance);
	}
	
	/**
	 * Returns the negated statement
	 * @return The negated statement
	 */
	public Statement getNegand()
	{
		return appearance;
	}
	
	public String toString() {
		return "\u00AC"+appearance.toStringParen();
	}
	
	public String toStringParen() {
		return toString();
	}

	public boolean equals(final Statement other) { //TODO: fix this!!!??? (not for conditional)
		if (!other.getClass().equals(this.getClass()))
			return false;
		return ((Negation)other).statements.equals(statements); // doesnt work out of order
	}

	@Override
	public Set<String> getVariables() {
		return statements.get(0).getVariables();
	}

	@Override
	public Set<String> getConstants() {
		return statements.get(0).getConstants();
	}

}
