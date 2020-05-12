package perl.aaron.TruthTrees.logic.fo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.util.UserError;

public class ExistentialQuantifier extends Quantifier {

	public ExistentialQuantifier(Variable var, Statement statement) {
		super(var, statement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\u2203" + var.toString() + " " + statement.toStringParen();
	}

	@Override
	public String toStringParen() {
		return toString();
	}
	
	private String decompsToString(final List<Statement> decomps) {
		return decomps.stream().map(Statement::toString).collect(Collectors.joining(", "));
	}

	@Override
	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {

		if (branches.size() == 1) // Single intantiation with new constant
		{
			if (branches.get(0).size() != 1) // There should be only 1 statement
				throw new UserError("Too many decompositions for " + this + ": " + decompsToString(branches.get(0)));
			Binding b = branches.get(0).get(0).determineBinding(statement);
			if (constantsBefore.contains( b.getConstant().toString()))
				throw new UserError("Cannot reuse " + b.getConstant());
		}
		else if (branches.size() > 1)
		{
			Set<String> constantsInstantiated = new LinkedHashSet<String>();
			for (List<Statement> curBranch : branches)
			{
				if (curBranch.size() != 1) // There should be exactly 1 statement per branch
					throw new UserError("Too many decompositions for " + this + " in branch: " + decompsToString(branches.get(0)));
				final Binding b = curBranch.get(0).determineBinding(statement);
				constantsInstantiated.add(b.getConstant().toString());
			}
			// Check if every constant has been instantiated as well as a new constant
			if (!constantsInstantiated.containsAll(constantsBefore))
				throw new UserError("Not every constant has been instantiated:\nConstants before: "
						+ constantsBefore + "\nConstants instantiated: " + constantsInstantiated);
			if (constantsInstantiated.size() != constantsBefore.size() + 1)
				throw new UserError("Exactly one new constant must be instantiated");
		}
		else
			throw new UserError("Not decomposed!");
	}

}
