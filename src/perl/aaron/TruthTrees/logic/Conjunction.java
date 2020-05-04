package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.util.UserError;

public class Conjunction extends LogicalOperator {
	/**
	 * Creates a Conjunction of the provided statements
	 * @param disjuncts The Statements being conjuncted
	 */
	public Conjunction(Statement... conjuncts) {
		statements = new ArrayList<Statement>();
		Collections.addAll(statements, conjuncts);
	}
	
	public Conjunction(List<Statement> conjuncts) {
		statements = new ArrayList<Statement>();
		statements.addAll(conjuncts);
	}
	
	public String toString()
	{
		ArrayList<Statement> statementsAL = (ArrayList<Statement>) statements;
		String statementString = "";
		for (int i = 0; i < statementsAL.size()-1; i++)
			statementString += statementsAL.get(i).toStringParen() + " \u2227 ";
		return statementString + statementsAL.get(statementsAL.size()-1).toStringParen();
	}

	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {
		if (branches.size() != 1) // There should be only 1 branch
			throw new UserError("Conjunction decomposition should not produce branches");
		var statements = new ArrayList<>(this.statements);
		if (branches.get(0).size() != statements.size()) // One decomposed statement per conjunct
			throw new UserError("Conjunction decomposition should produce " + branches.get(0).size() + " statements. " + statements.size() + " given.");
		for (Statement curStatement : branches.get(0))			// ... and every statement must match up to a conjunct
			if (!statements.remove(curStatement))
				throw new UserError("No conjunct matches " + curStatement);
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Conjunction))
			return false;
		List<Statement> otherStatements = ((Conjunction) other).getOperands();
		if (statements.size() != otherStatements.size())
			return false;
		for (int i = 0; i < statements.size(); i++)
		{
			//TODO accept statements in different order?
			if ( !(statements.get(i).equals(otherStatements.get(i))) )
				return false;
		}
		return true;
	}

	@Override
	public Set<String> getVariables() {
		Set<String> union = new LinkedHashSet<String>();
		for (Statement curStatement : statements)
			union.addAll(curStatement.getVariables());
		return union;
	}

	@Override
	public Set<String> getConstants() {
		Set<String> union = new LinkedHashSet<String>();
		for (Statement curStatement : statements)
			union.addAll(curStatement.getConstants());
		return union;
	}
}
