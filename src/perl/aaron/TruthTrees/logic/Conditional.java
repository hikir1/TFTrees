package perl.aaron.TruthTrees.logic;

import static perl.aaron.TruthTrees.Symbols.IMPLIES;
import static perl.aaron.TruthTrees.Symbols.NEG;
import static perl.aaron.TruthTrees.Symbols.OR;

import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.util.UserError;

public class Conditional extends BinaryOperator {

	public Conditional(Statement a, Statement b)
	{
		super(a,b);
	}
	public String toString() {
		return  statements.get(0).toStringParen()+ " \u2192 " +statements.get(1).toStringParen();
	}
	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {
		if (branches.size() != 2) // conditionals decompose into 2 branches (implication to a disjunction)
			throw new UserError("Conditionals should decompose into 2 branches. " + branches.size() + " given.");
		if (branches.get(0).size() != 1 || branches.get(1).size() != 1) // each branch should have 1 statement
			throw new UserError("Each branch should contain 1 statement.");
		Statement antecedentNeg = new Negation(statements.get(0)); // a -> b <=> ~a v b
		Statement consequent = statements.get(1);
		Statement a = branches.get(0).get(0);
		Statement b = branches.get(1).get(0);
		if((!antecedentNeg.equals(a) || !consequent.equals(b)) &&
				(!antecedentNeg.equals(b) || !consequent.equals(a)))
			throw new UserError("One branch should contain the negated anticedent, and the other, the consequent.\n"
					+ "A " + IMPLIES + " B <=> " + NEG + "A " + OR + " B");
	}
	public boolean equals(Statement other) {
		if (!(other instanceof Conditional))
			return false;
		List<Statement> otherStatements = ((Conditional) other).getOperands();
		return (statements.get(0).equals(otherStatements.get(0))) && (statements.get(1).equals(otherStatements.get(1)));
	}

}
