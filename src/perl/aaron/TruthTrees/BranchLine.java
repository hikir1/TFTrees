package perl.aaron.TruthTrees;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perl.aaron.TruthTrees.graphics.TreePanel;
import perl.aaron.TruthTrees.logic.AtomicStatement;
//import perl.aaron.TruthTrees.logic.Composable;
import perl.aaron.TruthTrees.logic.Decomposable;
import perl.aaron.TruthTrees.logic.Negation;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.util.UserError;

/**
 * A class that represents a single line in a branch, used for storing and verifying decompositions
 * @author Aaron
 *
 */
public class BranchLine {
	protected Branch parent;
	protected Statement statement;
	protected Set<Branch> selectedBranches; // holds the parent of the split that decomposes this line
	protected Set<BranchLine> selectedLines;
	protected BranchLine decomposedFrom;
	protected boolean isPremise;
	public static final Color SELECTED_COLOR = new Color(0.3f,0.9f,0.9f);
	public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
	public static final Color EDIT_COLOR = Color.GREEN;
  public boolean typing = false;
	public String currentTyping;
	public int decompNum;

	public BranchLine(Branch branch)
	{
		parent = branch;
		statement = null;
		selectedBranches = new LinkedHashSet<Branch>();
		selectedLines = new LinkedHashSet<BranchLine>();
		isPremise = false;
		decompNum = -1;
	}

	public String toString()
	{
		if (statement != null)
			return statement.toString();
		return "";
	}
	
	public void setIsPremise(boolean isPremise)
	{
		this.isPremise = isPremise;
	}
	
	public boolean isPremise()
	{
		return isPremise;
	}
	
	public void setStatement(Statement statement)
	{
		this.statement = statement;
	}
	
	public Statement getStatement()
	{
		return statement;
	}
	
	public int getWidth(FontMetrics f)
	{
    if (typing)
      return f.stringWidth(currentTyping);
    else
      return f.stringWidth(toString());
	}
	
	public Set<BranchLine> getSelectedLines()
	{
		return selectedLines;
	}
	
	public Set<Branch> getSelectedBranches()
	{
		return selectedBranches;
	}
	
	public void setDecomposedFrom(BranchLine decomposedFrom)
	{
		this.decomposedFrom = decomposedFrom;
	}
	
	public BranchLine getDecomposedFrom()
	{
		return decomposedFrom;
	}
	
	public Branch getParent()
	{
		return parent;
	}
	
	public void verifyDecomposition() throws UserError {
		verifyDecomposition(false, false);
	}
	
	public void verifyDecompositionOpen() throws UserError {
		verifyDecomposition(true, false);
	}
	
	public void verifyDecompositionOpen(boolean lax) throws UserError {
		verifyDecomposition(true, lax);
	}
	
	public void verifyDecomposition(boolean open, boolean lax) throws UserError
	{
		// if lax and no decomposition attempted, ignore
		// also make sure decoposedFrom exists, i.e. not pulled from thin air
		if(lax && decomposedFrom != null && selectedLines.isEmpty())
			return;
		
		// Check if the statement is decomposable and it is not the negation of an atomic statement
		if (statement == null)
			return;
		
		if (decomposedFrom == null && !isPremise)
			throw new UserError("Unexpected statement \"" + statement.toString() + "\" in tree");
		
		if (statement instanceof Decomposable &&
				!(statement instanceof Negation && (((Negation)statement).getNegand() instanceof AtomicStatement)))
		{
			if (selectedBranches.size() > 0) // branching decomposition (disjunction)
			{
				Set<BranchLine> usedLines = new LinkedHashSet<BranchLine>();
				for (Branch curRootBranch : selectedBranches)
				{
					List<List<Statement>> curTotalSet = new ArrayList<List<Statement>>();
					for (Branch curBranch : curRootBranch.getBranches())
					{
						List<Statement> curBranchSet = new ArrayList<Statement>();
						for (BranchLine curLine : selectedLines)
						{
							if (curLine.getParent() == curBranch)
							{
								curBranchSet.add(curLine.getStatement());
								usedLines.add(curLine);
							}
						}
						curTotalSet.add(curBranchSet);
					}
					if (selectedLines.size() > 0 &&
							!((Decomposable)statement).verifyDecomposition(curTotalSet,
							parent.getConstants(),
							parent.getConstantsBefore(selectedLines.iterator().next()))) {
						throw new UserError("Invalid decomposition of statement \"" + statement.toString() + "\"");
					}
				}
				if (!usedLines.equals(selectedLines)) // extra lines that were unused
					throw new UserError("Too many statements decomposed from \"" + statement.toString() + "\"");
				if (!BranchLine.satisfiesAllBranchesOpen(parent, selectedBranches))
					throw new UserError("Statement \"" + statement.toString() + "\" not decomposed in every " + (open? "open ": "") + "child branch");
			}
			else // non-branching decomposition (conjunction)
			{
				// A map of leaf branches to a list of statements in that branch and up that are selected
				Map<Branch, List<Statement>> branchMap = new LinkedHashMap<Branch, List<Statement>>();
				Set<Branch> selectedBranches = new LinkedHashSet<Branch>();
				// Add all branches that contain selected lines
				for (BranchLine curLine : selectedLines)
				{
					selectedBranches.add(curLine.getParent());
				}
				for (BranchLine curLine : selectedLines)
				{
					List<Statement> curList = null;
					// Check if this branch is in the map and add the statement to it
					if (branchMap.containsKey(curLine.getParent()))
						curList = branchMap.get(curLine.getParent());
					else // Check for child branches and add this line to all of those
					{
						boolean foundChildren = false;
						for (Branch curBranch : selectedBranches)
						{
							if (curBranch != curLine.getParent() && curBranch.isChildOf(curLine.getParent()))
							{
								System.out.println("Found child of " + curLine.getStatement());
								foundChildren = true;
								if (branchMap.containsKey(curBranch))
								{
									branchMap.get(curBranch).add(curLine.getStatement());
								}
								else
								{
									List<Statement> newList = new ArrayList<Statement>();
									newList.add(curLine.getStatement());
									branchMap.put(curBranch, newList);
								}
							}
						}
						if (!foundChildren)
						{
							curList = new ArrayList<Statement>();
							branchMap.put(curLine.getParent(), curList);
						}
					}
					if (curList != null)
						curList.add(curLine.getStatement());
				}
				for (Branch curBranch : branchMap.keySet())
				{
					List<List<Statement>> currentDecomp = new ArrayList<List<Statement>>();
					currentDecomp.add(branchMap.get(curBranch));
					if (!((Decomposable) statement).verifyDecomposition(currentDecomp,
							curBranch.getConstants(),
							curBranch.getConstantsBefore(selectedLines.iterator().next())))
					{
						throw new UserError("Invalid decomposition of statement \"" + statement.toString() + "\"");
					}
				}
				if (branchMap.size() == 0)
				{
					List<List<Statement>> currentDecomp = Collections.emptyList();
					Set<String> constants = Collections.emptySet();
					if (!((Decomposable) statement).verifyDecomposition(currentDecomp,constants,constants))
						throw new UserError("Statement \"" + statement.toString() + "\" has not been decomposed!");
					else
						return;
				}
				
				//EDIT: Fixed this for open case
				if(!BranchLine.satisfiesAllBranches(parent, branchMap.keySet(), open))
					throw new UserError("Statement \"" + statement.toString() + "\" not decomposed in every " + (open? "open ": "") + "child branch");
			}
		}
	}
	
	public static boolean satisfiesAllBranches(Branch root, Set<Branch> descendents) {
		return satisfiesAllBranches(root, descendents, false);
	}
	
	public static boolean satisfiesAllBranchesOpen(Branch root, Set<Branch> descendents) {
		return satisfiesAllBranches(root, descendents, true);
	}
	
	// EDIT: when a branch is open, only check if open branches satisfied            vvvvvvvvv
	public static boolean satisfiesAllBranches(Branch root, Set<Branch> descendents, boolean open)
	{
		///////////////////////////////// EDIT
		if(open && !TreePanel.checkForOpenBranch(root))
			return true;
		/////////////////////////////////
		
		if (descendents.contains(root) || root.isClosed())
			return true;
		else
		{
			if (root.getBranches().size() > 0)
			{
				for (Branch curBranch : root.getBranches())
				{
					if (!satisfiesAllBranches(curBranch, descendents, open))
						return false;
				}
				return true;
			}
			else
				return false;
		}
	}

	public Set<String> split(){
		// int count = 0;
		ExpressionParser.parseExpression(statement.toString());
		ArrayList<String> varList = ExpressionParser.variableList;
		
		return new HashSet<String>(varList);
	}

	
}
