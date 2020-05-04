package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.util.UserError;

public class Disjunction extends LogicalOperator {
	/**
	 * Creates a Disjunction of the provided statements
	 * @param disjuncts The Statements being disjuncted
	 */
	public Disjunction(Statement... disjuncts) {
		statements = new ArrayList<Statement>();
		Collections.addAll(statements, disjuncts);
	}
	
	public Disjunction(List<Statement> disjuncts) {
		statements = new ArrayList<Statement>();
		statements.addAll(disjuncts);
	}
	
	public String toString()
	{
		ArrayList<Statement> statementsAL = (ArrayList<Statement>) statements;
		String statementString = "";
		for (int i = 0; i < statementsAL.size()-1; i++)
			statementString += statementsAL.get(i).toStringParen() + " \u2228 ";
		return statementString + statementsAL.get(statementsAL.size()-1).toStringParen();
	}

	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError
	{
		if (branches.size() != statements.size()) // there must be one branch per disjunct
			throw new UserError("There must be one branch per disjunct.");
		var statements = new ArrayList<Statement>(this.statements);
		for (List<Statement> branch : branches) {
			if (branch.size() != 1)
				throw new UserError("Each branch must contain exactly one decomposed statement. " + branch.size() + " given.");
			if (!statements.remove(branch.get(0)))
				throw new UserError("No disjunct matches " + branch.get(0));
		}
	}

	public boolean equals(Statement other)
	{
		if (!(other instanceof Disjunction))
			return false;
		List<Statement> otherStatements = ((Disjunction) other).getOperands();
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
		{
			union.addAll(curStatement.getVariables());
		}
		return union;
	}

	@Override
	public Set<String> getConstants() {
		Set<String> union = new LinkedHashSet<String>();
		for (Statement curStatement : statements)
		{
			union.addAll(curStatement.getConstants());
		}
		return union;
	}
}
