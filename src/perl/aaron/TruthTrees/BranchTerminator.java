package perl.aaron.TruthTrees;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import perl.aaron.TruthTrees.logic.Decomposable;
import perl.aaron.TruthTrees.logic.A_Statement;
import perl.aaron.TruthTrees.logic.negation.I_Negation;
import perl.aaron.TruthTrees.util.UserError;

public class BranchTerminator extends BranchLine {

	private boolean close = true;
	
	public BranchTerminator(Branch branch) {
		super(branch);
	}
	
	public String toString()
	{
		if (close)
			return "\u2715";
		else
			return "\u25EF";
	}
	
	public void setIsPremise(boolean isPremise) {}
	public boolean isPremise() { return false; }
	public void setStatement(A_Statement s) {}
	public void setDecomposedFrom(BranchLine decomposedFrom) {}
	public BranchLine getDecomposedFrom() { return null; }
	
	public void verifyDecomposition() throws UserError
	{
		if (close)
		{
			if (selectedLines.size() != 2)
				throw new UserError("Invalid number of supporting statements for branch termination");
			ArrayList<BranchLine> selectedList = new ArrayList<BranchLine>();
			selectedList.addAll(selectedLines);
			A_Statement atomic;
			A_Statement negatedStatement;
			if (!(selectedList.get(0).getStatement() instanceof Decomposable))
			{
				atomic = selectedList.get(0).getStatement();
				negatedStatement = selectedList.get(1).getStatement();
			}
			else if (!(selectedList.get(1).getStatement() instanceof Decomposable))
			{
				atomic = selectedList.get(1).getStatement();
				negatedStatement = selectedList.get(0).getStatement();
			}
			else
				throw new UserError("No atomic statement found in branch termination justification");
			
			if (!(negatedStatement instanceof I_Negation))
				throw new UserError("No negation found in branch termination justification");
			
			I_Negation negated = (I_Negation) negatedStatement;
			if (!negated.getNegand().equals(atomic))
				throw new UserError("Incorrect atomic statement/negation pair in branch termination justification");
		}
		else // Open branch
		{
			// Check to make sure all lines above this are decomposed
			Branch curBranch = this.parent;
			Set<I_Negation> negations = new LinkedHashSet<I_Negation>();
			Set<A_Statement> atomics = new LinkedHashSet<A_Statement>();
			while (curBranch != null)
			{
				for (int i = 0; i < curBranch.numLines(); i++)
				{
					BranchLine curLine = curBranch.getLine(i);
					if (curLine.getStatement() instanceof Decomposable)
					{
						boolean returnError = true;
						if (curLine.getStatement() instanceof I_Negation)
						{
							I_Negation neg = (I_Negation) curLine.getStatement();
							negations.add(neg);
							if (!(neg.getNegand() instanceof Decomposable))
								returnError = false;
						}
						if (curLine.getSelectedLines().size() == 0 && returnError)
							throw new UserError("There are statements in this branch that still need to be decomposed!");
					}
					else if (curLine.getStatement() != null)
						atomics.add(curLine.getStatement());
				}
				curBranch = curBranch.getRoot();
			}
			for (A_Statement curAtomic : atomics)
			{
				if (negations.contains(new I_Negation(curAtomic)))
					throw new UserError("There exists an atomic statement and its negation in this branch!");
			}
		}
	}
	
	/**
	 * Returns true if this terminator closes a branch, false if it marks it as open
	 * @return True if this terminator closes a branch, false if it marks it as open
	 */
	public boolean isClose()
	{
		return close;
	}
	
	/**
	 * Swaps the type of terminator between close and open
	 */
	public void switchIsClose()
	{
		close = !close;
	}

}
