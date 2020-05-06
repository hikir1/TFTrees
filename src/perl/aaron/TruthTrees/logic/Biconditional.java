package perl.aaron.TruthTrees.logic;

import java.util.List;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.logic.negation.NegBiconditional;
import perl.aaron.TruthTrees.logic.negation.Negation;

public class Biconditional extends BinaryOperator implements SerialDecomposable {
	
	private final List<Statement> decomposition;

	public Biconditional(Statement a, Statement b) {
		super(a, b);
		decomposition = List.of(new Conjunction(a,b), new Conjunction(a.negated(), b.negated()));
	}

//	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {
//		if (branches.size() != 2)
//			throw new UserError("Biconditionals should decompose into 2 branches. " + branches.size() + " given.");
//		List<Statement> b1 = branches.get(0), b2 = branches.get(1);
//		if(b1.size() != 1 || b2.size() != 1)
//			throw new UserError("Each branch should contain one decomposed statement.");
//		Statement decomp1 = b1.get(0), decomp2 = b2.get(0);
//		Conjunction AandB = new Conjunction(statements.get(0),statements.get(1));
//		Conjunction NotAandNotB = new Conjunction(new Negation(statements.get(0)), new Negation(statements.get(1)));
//		if (!decomp1.equals(AandB) || !decomp2.equals(NotAandNotB))
//			if(!decomp1.equals(NotAandNotB) || !decomp2.equals(AandB))
//				throw new UserError("One branch should contain the conjunction of the operands,\n" 
//						+ "and the other, the conjunction of the negation thereof.\n"
//						+ "A " + IFF + " B <=> (A " + AND + " B) " + OR + " (" + NEG +"A " + AND + " " + NEG + "B)");
//	}
	
	@Override
	public List<Statement> getModelDecomposition(Branch sourceBranch) {
		return decomposition;
	}
	
	@Override
	public Negation negated() {
		assert statements != null;
		assert statements.size() == 2;
		return new NegBiconditional(statements.get(0), statements.get(1));
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
