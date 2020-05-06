package perl.aaron.TruthTrees.logic;

import static perl.aaron.TruthTrees.Symbols.AND;
import static perl.aaron.TruthTrees.Symbols.IFF;
import static perl.aaron.TruthTrees.Symbols.NEG;
import static perl.aaron.TruthTrees.Symbols.OR;

import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.util.UserError;

public class Biconditional extends BinaryOperator {

	public Biconditional(Statement a, Statement b) {
		super(a, b);
	}

	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {
		if (branches.size() != 2)
			throw new UserError("Biconditionals should decompose into 2 branches. " + branches.size() + " given.");
		List<Statement> b1 = branches.get(0), b2 = branches.get(1);
		if(b1.size() != 1 || b2.size() != 1)
			throw new UserError("Each branch should contain one decomposed statement.");
		Statement decomp1 = b1.get(0), decomp2 = b2.get(0);
		Conjunction AandB = new Conjunction(statements.get(0),statements.get(1));
		Conjunction NotAandNotB = new Conjunction(new Negation(statements.get(0)), new Negation(statements.get(1)));
		if (!decomp1.equals(AandB) || !decomp2.equals(NotAandNotB))
			if(!decomp1.equals(NotAandNotB) || !decomp2.equals(AandB))
				throw new UserError("One branch should contain the conjunction of the operands,\n" 
						+ "and the other, the conjunction of the negation thereof.\n"
						+ "A " + IFF + " B <=> (A " + AND + " B) " + OR + " (" + NEG +"A " + AND + " " + NEG + "B)");
	}

	public String toString() {
		return statements.get(0).toStringParen() + " \u2194 " + statements.get(1).toStringParen();
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Biconditional))
			return false;
		Biconditional otherBiconditional = (Biconditional) other;
		for (int i = 0; i < 2; i++)
		{
			if (!statements.get(i).equals(otherBiconditional.getOperands().get(i)))
				return false;
		}
		return true;
	}

}
