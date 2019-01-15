package perl.aaron.TruthTrees.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.BranchLine;
import perl.aaron.TruthTrees.BranchTerminator;
import perl.aaron.TruthTrees.ExpressionParser;
import perl.aaron.TruthTrees.logic.Conjunction;
import perl.aaron.TruthTrees.logic.Decomposable;
import perl.aaron.TruthTrees.logic.Negation;
import perl.aaron.TruthTrees.logic.Statement;

/**
 * An extension of JPanel for displaying and interacting with a sequence of TreeLines
 * @author Aaron Perl, Sarah Mogielnicki
 *
 */
public class TreePanel extends JPanel {
	
	private static final long serialVersionUID = 2267768929169530856L;
	private static final int UNDO_STACK_SIZE = 32;
	private static final int REDO_STACK_SIZE = 32;

	private Branch root;
	private Point center;
	private Point prevCenter;
	private Point clickPoint;
	private float size;
	private int maxWidth;
	private BranchLine editLine;
	private Map<Branch, JButton> addBranchMap;
	private Map<Branch, JButton> addLineMap;
	private Map<Branch, JButton> branchMap;
	private Map<Branch, JButton> terminateMap;
	private Map<JTextField, BranchLine> lineMap;
	private Map<BranchLine, JTextField> reverseLineMap;
	private Set<BranchLine> selectedLines;
	private Set<Branch> selectedBranches;
	private Branch premises;
	private Deque<Branch> undoStack;
	private Deque<Branch> redoStack;
	
	public TreePanel()
	{
		this(true);
	}
	
	public TreePanel(boolean addFirstLine)
	{
		super();
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		setLayout(null);
		center = new Point(0,-50);
		size = 12f;
		maxWidth = 0;
		editLine = null;
		selectedLines = null;
		selectedBranches = null;
		addBranchMap = new HashMap<Branch, JButton>();
		addLineMap = new HashMap<Branch, JButton>();
		branchMap = new HashMap<Branch, JButton>();
		terminateMap = new HashMap<Branch, JButton>();
		lineMap = new HashMap<JTextField, BranchLine>();
		reverseLineMap = new HashMap<BranchLine, JTextField>();
		this.setFont(this.getFont().deriveFont(size));
		premises = addBranch(null, true);
		undoStack = new ArrayDeque<Branch>(UNDO_STACK_SIZE);
		redoStack = new ArrayDeque<Branch>(REDO_STACK_SIZE);
		
		root = addBranch(premises, false);
		
		setFocusable(true);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				prevCenter = center;
				clickPoint = e.getPoint();
				requestFocus();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				center = new Point(e.getPoint().x - clickPoint.x + prevCenter.x, e.getPoint().y - clickPoint.y + prevCenter.y);
				moveComponents();
				validate();
				repaint();
			}
		});
	}
	
	private void recordState()
	{
		if (premises != null)
		{
			Branch treeCopy = premises.deepCopy();

			Map<BranchLine, BranchLine> lineMap = new HashMap<BranchLine, BranchLine>();
			Map<Branch, Branch> branchMap = new HashMap<Branch, Branch>();
			
			mapNewToOld(premises, treeCopy, lineMap, branchMap);
			addLineReferences(lineMap, branchMap);
			
			undoStack.push(treeCopy);
			redoStack = new ArrayDeque<>();
		}
	}
	
	/**
	 * Maps every item in a copy of a branch to the corresponding item in the original
	 * @param oldBranch The original branch
	 * @param newBranch The copy of the original branch
	 * @param lineMap A map of old lines to new lines to be populated
	 * @param branchMap A map of old branches to new branches to be populated
	 */
	private void mapNewToOld(Branch oldBranch, Branch newBranch,
	  Map<BranchLine, BranchLine> lineMap,
	  Map<Branch, Branch> branchMap)
	{
		branchMap.put(oldBranch, newBranch);
		for (int i = 0; i < oldBranch.numLines(); i++)
		{
			lineMap.put(oldBranch.getLine(i), newBranch.getLine(i));
		}
		Iterator<Branch> oldIter = oldBranch.getBranches().iterator();
		Iterator<Branch> newIter = newBranch.getBranches().iterator();
		while (oldIter.hasNext())
		{
			mapNewToOld(oldIter.next(), newIter.next(), lineMap, branchMap);
		}
	}
	
	/**
	 * Adds all references (decompositions, etc.) from lines in a tree to a copy
	 * of that tree, updating them to point to the components in the copy
	 * @param lineMap The map of old lines to each corresponding lines in the copy
	 * @param branchMap The map of branches to each corresponding branch in the copy
	 */
	private void addLineReferences(Map<BranchLine, BranchLine> lineMap,
	  Map<Branch, Branch> branchMap)
	{
		for (BranchLine oldLine : lineMap.keySet())
		{
			BranchLine newLine = lineMap.get(oldLine);
			
			BranchLine oldDecomposedFrom = oldLine.getDecomposedFrom();
			BranchLine newDecomposedFrom = lineMap.get(oldDecomposedFrom);
			newLine.setDecomposedFrom(newDecomposedFrom);
			
			Set<BranchLine> oldSelectedLineSet = oldLine.getSelectedLines();
			Set<BranchLine> newSelectedLineSet = newLine.getSelectedLines();
			for (BranchLine oldSelected : oldSelectedLineSet)
			{
				BranchLine newSelected = lineMap.get(oldSelected);
				newSelectedLineSet.add(newSelected);
			}
			
			Set<Branch> oldSelectedBranchSet = oldLine.getSelectedBranches();
			Set<Branch> newSelectedBranchSet = newLine.getSelectedBranches();
			for (Branch oldSelected : oldSelectedBranchSet)
			{
				Branch newSelected = branchMap.get(oldSelected);
				newSelectedBranchSet.add(newSelected);
			}
			
			newLine.setIsPremise(oldLine.isPremise());			
		}
	}
	
	/**
	 * Undoes the previous state change.
	 */
	public void undoState()
	{
		if (!undoStack.isEmpty())
		{
			redoStack.push(premises.deepCopy());
			premises = undoStack.pop();
			root = premises.getBranches().iterator().next();
			editLine = null;
			resetAllComponents();
			moveComponents();
			repaint();
		}
	}
	
	/**
	 * Performs the previously undone state change again
	 */
	public void redoState()
	{
		if (!redoStack.isEmpty())
		{
			undoStack.push(premises.deepCopy());
			premises = redoStack.pop();
			root = premises.getBranches().iterator().next();
			editLine = null;
			resetAllComponents();
			moveComponents();
			repaint();
		}
	}
	
	/**
	 * Deletes all components saved and recreates them for the current tree.
	 */
	private void resetAllComponents()
	{
		deleteAllButtons();
		
		addBranchMap = new HashMap<Branch, JButton>();
		addLineMap = new HashMap<Branch, JButton>();
		branchMap = new HashMap<Branch, JButton>();
		terminateMap = new HashMap<Branch, JButton>();
		lineMap = new HashMap<JTextField, BranchLine>();
		reverseLineMap = new HashMap<BranchLine, JTextField>();
		
		addComponentsRecursively(premises);
	}
	
	/**
	 * Deletes all of the components associated with the tree.
	 */
	private void deleteAllButtons()
	{
		removeComponentsInMap(addBranchMap);
		removeComponentsInMap(addLineMap);
		removeComponentsInMap(branchMap);
		removeComponentsInMap(terminateMap);
		removeComponentsInMap(reverseLineMap);
	}
	
	/**
	 * Removes all components in the value set of a map from this panel.
	 * @param componentMap The map of objects to components to remove.
	 */
	private void removeComponentsInMap(Map<? extends Object, ? extends JComponent> componentMap)
	{
		for (JComponent comp : componentMap.values())
		{
			remove(comp);
		}
	}
	
	/**
	 * Creates all associated component for a given branch and its ancestors
	 * @param b The branch to recursively create components for
	 */
	private void addComponentsRecursively(Branch b)
	{
		makeButtonsForBranch(b);
		for (int i = 0; i < b.numLines(); i++)
		{
			BranchLine line = b.getLine(i);
			makeTextFieldForLine(line, b, line instanceof BranchTerminator);
		}
		for (Branch child : b.getBranches())
		{
			addComponentsRecursively(child);
		}
	}
	
	/**
	 * Makes a new BranchLine that is a premise, and sets its statement to s
	 * @param s The premise that is added
	 */	
	public void addPremise(Statement s)
	{
		recordState();
		BranchLine newLine = addLine(premises);
		newLine.setIsPremise(true);
		if (s != null)
		{
			newLine.setStatement(s);
			reverseLineMap.get(newLine).setText(s.toString());
			moveComponents();
		}
	}
	
	/**
	 * Makes a new BranchLine that is a premise, with no statement
	 */	
	public void addPremise()
	{
		addPremise(null);
	}
	
	/**
	 * Checks that the line is decomposed properly
	 * @param l The BranchLine that is being checked
	 * @return null if decomposed properly, an error message otherwise
	 */
	private String checkLine(BranchLine l)
	{
		return l.verifyDecomposition();
	}

	/**
	 * Checks all the lines in a branch to see if they are decomposed properly
	 * @param b Branch being checked
	 * @return null if decomposed properly, an error message otherwise
	 */
	private String checkBranch(Branch b)
	{
		for (int i = 0; i < b.numLines(); i++)
		{
			BranchLine curLine = b.getLine(i);
			String ret = checkLine(curLine);
			if (ret != null)
				return ret;
		}
		for (Branch curBranch : b.getBranches())
		{
			String ret = checkBranch(curBranch);
			if (ret != null)
				return ret;
		}
		return null;
	}
	
	/**
	 * Checks that the tree is complete (by checking that no branches are open
	 * and that all of the branches are closed).
	 * @return 0 if all branches close with no open branches, 1 if all branches terminate but at least one is marked open, -1 otherwise
	 */
	public int checkCompletion()
	{
		boolean isOpen = checkForOpenBranch(root);
		boolean allClosed = checkForAllClosed(root);
		if ( !isOpen && allClosed)
			return 0;
		else if( !allClosed && !isOpen )
			return -1;
		else
			return 1;
	}
	
	/**
	 * Recursively checks if b or any of its children are open
	 * @param b the Branch that is being checked
	 * @return true if b or any of its children are open
	 */
	private boolean checkForOpenBranch(Branch b)
	{
		if (b.isOpen())
			return true;
		for (Branch child : b.getBranches())
		{
			if (checkForOpenBranch(child))
				return true;
		}
		return false;
	}
	
	/**
	 * Recursively checks if b and all if its children are closed
	 * @param b the Branch being checked for closed
	 * @return false if b or one of its children is not closed, true otherwise
	 */
	private boolean checkForAllClosed(Branch b)
	{
		if (b.getBranches().size() == 0 && !b.isClosed())
			return false;
		for (Branch child : b.getBranches())
		{
			if (!checkForAllClosed(child))
				return false;
		}
		return true;
	}
	
	/**
	 * Recursively checks if the branch b has a valid open terminator
	 * @param b the branch being checked for a valid open branch
	 * @return true if b has a valid open terminator, false otherwise
	 */
	private boolean verifyOpenTerminator(Branch b)
	{
		// If there are no open branches anywhere return false
		if (!checkForOpenBranch(b)) {
			return false;
		}
		
		if (b.getBranches().size() == 0 && b.isOpen()) {
			return b.verifyTerminations();
		}
		
		if (b.getBranches().size() == 0 && !b.isOpen())
			return false;
		
		for (Branch child : b.getBranches()) {
			if (checkForOpenBranch(child)) {
				return verifyOpenTerminator(child);
			}
		}
		
		return false;
		
	}
	
	/**
	 * Recursively checks if b and all if its children are have verified Terminators
	 * @param b the Branch being checked for verified terminators
	 * @return false if b or one of its children has a terminator that is not verified, 
	 * 	true otherwise
	 */
	private boolean verifyTerminators(Branch b)
	{
		boolean hasOpen = checkForOpenBranch(b);
		
		if (hasOpen) { //If there are open branches in the current level or below
			
			// Check that the open branch is valid
			boolean validOpen = verifyOpenTerminator(b);
			if (validOpen) {
				return true;
			}
			else {
				return false;
			}
		}
		else { //No open branches in tree, proceed as normal
			if (b.getBranches().size() == 0 && !b.verifyTerminations()) {
				return false;
			}
			
			for (Branch child: b.getBranches()) {
				if (!verifyTerminators(child)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Walks through the tree and returns a set of the bottom branches that contain an open terminator
	 * @param b Branch to start searching at
	 * @param open Empty set of Branches (for recursion)
	 * @return The set of all branches with an open terminator
	 */
	private Set<Branch> findOpenBranches(Branch b, Set<Branch> open) {
		boolean hasOpen = checkForOpenBranch(b);
		if (hasOpen) {
			
			if (b.getBranches().size() > 0) {
				// Has children
				for (Branch child : b.getBranches()) {
					
					if (checkForOpenBranch(child)) { //Recurse into children with open branches
						return findOpenBranches(child, open);
					}
				}
			} else {
				// No children
				open.add(b);
				return open;
			}
		} else {
			// No open branch
			open.add(b);
			return open;
		}
		return open;
	}
	
	
	
	
	private String verifyPremisesOpenBranch(Branch b) {
		String error = "";
		int linec = b.numLines();
		Set<Branch> openBranches = new HashSet<Branch>();
		openBranches = findOpenBranches(root, openBranches);
		
		HashMap<BranchLine, Boolean> premiseMap = new HashMap<BranchLine, Boolean>();
		
		// Initialize map of all premises to false
		for (int i=0; i<linec; ++i) {
			if (b.getLine(i).getStatement() instanceof Decomposable && !(b.getLine(i).getStatement() instanceof Negation)) {
				premiseMap.put(b.getLine(i), false);
			}
		}
		
		// Loop through each premise and check if it's decomposition is a parent of the open termination
		for (int i=0; i<linec; ++i) {
			
			BranchLine line = b.getLine(i);
			
			Set<Branch> targetBranches = line.getSelectedBranches();
			Set<BranchLine> targetLines = line.getSelectedLines();
			
			// For cases such as conjunctions where the decomposition results in new lines add the parent to be checked
			if (line.getStatement() instanceof Conjunction) {
				for (BranchLine t : targetLines) {
					targetBranches.add(t.getParent());
				}
			}
			
			// Loop through each of the selected branches where the premise is decomposed 
			for (Branch t : targetBranches) {
				
				// Only look at cases where the target has an open terminator below it
				if (checkForOpenBranch(t)) {
				
					// Check if the selected branch is a parent of the open branch
					for (Branch openBranch : openBranches) {
						if (openBranch.isChildOf(t)) {
							if (line.getStatement() instanceof Decomposable && !(line.getStatement() instanceof Negation)) {
								// Mark that this premise is decomposed 
								premiseMap.put(line, true);
							}
						}
					}
				}
			}
		}
		for (BranchLine l : premiseMap.keySet()) {
			if (premiseMap.get(l) == false) {
				error += "Premise " + l.getStatement() + " is not decomposed in the open branch.\n";
			}
		}
		return error;
	}

	/**
	 * Checks for any unexpected constants in a branch 
	 * @param b Branch to be checked
	 * @return Empty string if ok, error message if problem
	 */
	public String checkBranchConstants(Branch b) {
		
		String ret = "";
		
		if (b.numLines() > 0) {
			
			for (int i = 0; i < b.numLines(); ++i) {
				BranchLine line = b.getLine(i);
				
				String vRet = line.verifyOpenDecomposition();
				if (vRet != null) {
					ret += vRet;
				}
			}
			
//			Set<String> preConsts = new LinkedHashSet<String>();
//			Set<String> thisConsts = new LinkedHashSet<String>();
//			System.out.println(b.getConstants().size());
//			
//			preConsts.addAll(b.getConstantsBefore(b.getLine(0)));
//			thisConsts.addAll(b.getConstantsThis());
//			System.out.println("Before: " + String.join(", ", b.getConstantsBefore(b.getLine(0))) + "\n");
//			System.out.println("This: " + thisConsts.toString() + "\n");
//			thisConsts.removeAll(preConsts);
//			
//			if (preConsts.size() > 0) {
//				for (String c : thisConsts) {
//					ret += "Unexpected constant " + c + " in tree.\n";
//				}
//			}
		}
		
		for (Branch child: b.getBranches()) {
			ret += checkBranchConstants(child);
		}
		return ret;
	}

	
	/**
	 * Runs all of the "check" methods on the whole tree.
	 * @return null if tree is correct and complete, error message otherwise
	 */
	public String check()
	{
		String returnVal = "";
		
		int completionVal = checkCompletion();
		System.out.println("completetionVal: " + completionVal);
		boolean verifyEndings = verifyTerminators(root);
		
		if (completionVal == 0) //No open branches
			if(verifyEndings)
				return null;
			else
				return "There are Branch Terminators that are not referencing correct lines.\n";
		
		else if(completionVal == 1) //At least one open branch
			if(verifyEndings) {
				
				String checkRet = verifyPremisesOpenBranch(premises);
				if (checkRet != null)
					returnVal = returnVal + checkRet;
				
				String varRet = checkBranchConstants(root);
				if (!varRet.equals(""))
					returnVal = returnVal + varRet;
					
				
				
//				String branchVal = checkBranch(root);
//				if (branchVal != null)
//					returnVal = returnVal + "root!! " + branchVal;
				
				if(returnVal.equals(""))
					return null;
				else
					return returnVal;
			}
			else
				return "Not all premises are decomposed on open branch OR Invalid usage of open branch.";
		
		else if(completionVal == -1)
			returnVal = returnVal + "Not all branches are closed and no branch has been marked as open!\n";
		
		String checkRet = checkBranch(premises);
		if (checkRet != null)
			returnVal = returnVal + checkRet;
		
		String branchVal = checkBranch(root);
		if (branchVal != null)
			returnVal = returnVal + branchVal;
		
		if(returnVal.equals(""))
			return null;
		else
			return returnVal;
	}
	
	/**
	 * Checks the selected line.
	 * @return null if the line is fine, and an error message otherwise
	 */
	public String checkSelectedLine()
	{
		if (editLine != null)
			return checkLine(editLine);
		return "No statement is currently selected!";
	}
	
	/**
	 * 
	 * @param parent
	 * @return
	 */
	public Branch addBranch(Branch parent)
	{
		return addBranch(parent, true);
	}
	
	/**
	 * Makes a new Branch and adds it to the parent branch. Adds the first line
	 * to the branch if addFirstLine is true.
	 * @param parent the root branch for this branch
	 * @param addFirstLine if true, the method will add a line to the new branch
	 * @return the new branch that was added
	 */
	public Branch addBranch(Branch parent, boolean addFirstLine)
	{
		recordState();
		Branch newBranch = new Branch(parent);
		newBranch.setFontMetrics(getFontMetrics(getFont()));
		makeButtonsForBranch(newBranch);
		if (parent != null)
			parent.addBranch(newBranch);
		if (addFirstLine)
		{
			addLine(newBranch);
			if (parent == null)
				newBranch.getLine(0).setIsPremise(true);
		}
		moveComponents();
		repaint();
		return newBranch;
	}
	
	/**
	 * Makes all the buttons for the branch
	 * @param b Branch that will get the buttons
	 */
	private void makeButtonsForBranch(Branch b)
	{
		final Branch myBranch = b;
		JButton branchButton = new JButton("Add Branch");
		JButton lineButton = new JButton("Add Line");
		JButton terminateButton = new JButton("Terminate");
		JButton decompButton = new JButton();
		decompButton.setOpaque(false);
		decompButton.setContentAreaFilled(false);
		decompButton.setBorderPainted(false);
		decompButton.setFocusable(false);
		branchButton.setMargin(new Insets(1, 1, 1, 1));
		lineButton.setMargin(new Insets(1, 1, 1, 1));
		terminateButton.setMargin(new Insets(1, 1, 1, 1));
		branchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if (myBranch.numLines() == 0) return;
				addBranch(myBranch);
			}
		});
		lineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addLine(myBranch);
			}
		});
		terminateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addTerminator(myBranch);
			}
		});
		decompButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (selectedBranches != null &&
				  (SwingUtilities.isRightMouseButton(e) || e.isControlDown()))
				{
					if (selectedBranches.contains(myBranch))
					{
						selectedBranches.remove(myBranch);
						myBranch.setDecomposedFrom(null);
					}
					else if (myBranch.getDecomposedFrom() == null)
					{
						selectedBranches.add(myBranch);
						myBranch.setDecomposedFrom(editLine);
					}
					TreePanel.this.repaint();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		add(branchButton);
		add(lineButton);
		add(terminateButton);
		add(decompButton);
		addBranchMap.put(b, branchButton);
		addLineMap.put(b, lineButton);
		branchMap.put(b, decompButton);
		terminateMap.put(b, terminateButton);
	}
	
	/**
	 * Checks if Branch b is selected.
	 * @param b the Branch that is being checked for selection
	 * @return true if b is selected, false otherwise
	 */
	private boolean isSelected(BranchLine b)
	{
		if (selectedLines != null)
			return selectedLines.contains(b);
		return false;
	}
	

	private void toggleSelected(BranchLine b, Set<BranchLine> curSelected)
	{
		if (curSelected.contains(b))
		{
			curSelected.remove(b);
			reverseLineMap.get(b).setBackground(BranchLine.DEFAULT_COLOR);
			if (!(editLine instanceof BranchTerminator))
				b.setDecomposedFrom(null);
		}
		else
		{
			curSelected.add(b);
			reverseLineMap.get(b).setBackground(BranchLine.SELECTED_COLOR);
			if (!(editLine instanceof BranchTerminator))
				b.setDecomposedFrom(editLine);
		}
	}
	
	private void toggleSelected(BranchLine b)
	{
		toggleSelected(b, selectedLines);
	}
	
	private void moveBranch(Branch b, Point origin)
	{
		int verticalOffset = 0;
		int maxLineWidth = b.getWidestLine();
		int maxWidth = b.getWidestChild();
		for (int i = 0; i < b.numLines(); i++)
		{
			BranchLine curLine = b.getLine(i);
			JTextField curField = reverseLineMap.get(curLine);
//			if (curField == null)
//			{
//				System.out.println(curLine.toString());
//				System.exit(-1);
//			}
			if (isSelected(curLine))
				curField.setBackground(BranchLine.SELECTED_COLOR);
			else if (curLine == editLine)
				curField.setBackground(BranchLine.EDIT_COLOR);
			else
				curField.setBackground(BranchLine.DEFAULT_COLOR);
			curField.setBounds(	origin.x - maxLineWidth/2, origin.y + verticalOffset,
								maxLineWidth, b.getLineHeight());
			curField.repaint();
			verticalOffset += b.getLineHeight();
		}
		if (b != premises)
		{
			JButton lineButton = addLineMap.get(b);
			JButton addButton = addBranchMap.get(b);
			JButton branchButton = branchMap.get(b);
			JButton terminateButton = terminateMap.get(b);
			int horizontalOffset = (maxWidth + Branch.BRANCH_SEPARATION) * (b.getBranches().size()-1);
			horizontalOffset /= -2;
			if (!b.isClosed() && !b.isOpen())
			{
				lineButton.setBounds(origin.x - maxLineWidth/2, origin.y + verticalOffset,
						maxLineWidth, b.getLineHeight());
				verticalOffset += b.getLineHeight();
				addButton.setBounds(origin.x - maxLineWidth/2, origin.y + verticalOffset,
									maxLineWidth, b.getLineHeight());
				verticalOffset += b.getLineHeight();
				if (b.getBranches().size() == 0)
				{
					terminateButton.setBounds(origin.x - maxLineWidth/2, origin.y + verticalOffset,
						maxLineWidth, b.getLineHeight());
					terminateButton.setVisible(true);
					terminateButton.setEnabled(true);
					verticalOffset += b.getLineHeight();
				}
				else
				{
					terminateButton.setVisible(false);
					terminateButton.setEnabled(false);
				}
				terminateButton.repaint();
				branchButton.setBounds(	origin.x + horizontalOffset - maxWidth/2, origin.y + verticalOffset,
										-horizontalOffset * 2 + maxWidth, Branch.VERTICAL_GAP);
				addButton.setVisible(true);
				addButton.setEnabled(true);
				lineButton.setVisible(true);
				lineButton.setEnabled(true);
				branchButton.setEnabled(true);
			}
			else
			{
				addButton.setVisible(false);
				addButton.setEnabled(false);
				lineButton.setVisible(false);
				lineButton.setEnabled(false);
				branchButton.setEnabled(false);
				terminateButton.setVisible(false);
				terminateButton.setEnabled(false);
			}
			addButton.repaint();
			lineButton.repaint();
			branchButton.repaint();
			terminateButton.repaint();
			verticalOffset += Branch.VERTICAL_GAP;
			for (Branch curChild : b.getBranches())
			{
				moveBranch(curChild, new Point(origin.x + horizontalOffset, origin.y + verticalOffset));
				horizontalOffset += (maxWidth + Branch.BRANCH_SEPARATION);
			}
		}
	}
	
	public void moveComponents()
	{
		Point origin = new Point(center.x + getWidth()/2, center.y + getHeight()/2);
		if (premises != null)
		{
			moveBranch(premises, origin);
			origin.translate(0, premises.getLineHeight() * premises.numLines());
		}
		origin.translate(0, 20);
		if (root != null)
			moveBranch(root, origin);
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(800, 600);
	}
	
	/**
	 * Adds a BranchLine that is not a terminator
	 * @param b the Branch that the BranchLine is added to
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b)
	{
		return addLine(b, false);
	}
	
	/**
	 * Adds a BranchLine that may or may not be a terminator
	 * @param b the Branch that the BranchLine is added to 
	 * @param isTerminator is true if if this BranchLine is a terminator
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b, final boolean isTerminator)
	{
		return addLine(b, isTerminator, true);
	}
	
	/**
	 * Adds a new branch line, which can be a terminator.
	 * @param b the Branch to which the line is being added
	 * @param isTerminator true if the line is a terminator
	 * @param isClose
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b, final boolean isTerminator, final boolean isClose)
	{
		recordState();
		final BranchLine newLine;
		if (isTerminator)
		{
			newLine = new BranchTerminator(b);
			if (!isClose)
				((BranchTerminator)newLine).switchIsClose();
			b.addTerminator((BranchTerminator)newLine);
		}
		else
			newLine = b.addStatement(null);
		makeTextFieldForLine(newLine, b, isTerminator);
		moveComponents();
		return newLine;
	}

  public void addLineBefore()
  {
    if (editLine != null)
    {
      System.out.println("Adding line before "+editLine);
      for (int i = 0; i < editLine.getParent().numLines(); i++)
      {
        if (editLine.getParent().getLine(i) == editLine)
        {
          final BranchLine newline;
          newline = editLine.getParent().addStatement(null, i);
          makeTextFieldForLine(newline, editLine.getParent(), false);
          moveComponents();
          return;
        }
      }
    }
  }

  public void addLineAfter()
  {
    if (editLine != null && !(editLine instanceof BranchTerminator))
    {
      System.out.println("Adding line after "+editLine);
      for (int i = 0; i < editLine.getParent().numLines(); i++)
      {
        if (editLine.getParent().getLine(i) == editLine)
        {
          final BranchLine newline;
          newline = editLine.getParent().addStatement(null, i+1);
          makeTextFieldForLine(newline, editLine.getParent(), false);
          moveComponents();
          return;
        }
      }
    }
  }
	
	private void makeTextFieldForLine(final BranchLine line, final Branch b, final boolean isTerminator)
	{
		final JTextField newField = new JTextField("");
		newField.setUI(new BasicTextFieldUI() {
			@Override
			public void paintBackground(Graphics g)
			{
				super.paintBackground(g);
				Color old = g.getColor();
				g.setColor(getBackground());
				g.fillRect(getX(), getY(), getWidth(), getHeight());
				g.setColor(old);
			}
		});
		if (line.getStatement() != null)
			newField.setText(line.getStatement().toString());
		if (isTerminator)
		{
			newField.setText(line.toString());
			newField.setForeground(new Color(0.7f, 0.0f, 0.0f));
		}
		if (b == premises)
			line.setIsPremise(true);
		newField.setEditable(false);
		newField.setFocusable(false);
		newField.setHorizontalAlignment(JTextField.CENTER);
		newField.setFont(this.getFont().deriveFont(size));
		((AbstractDocument) newField.getDocument()).setDocumentFilter(new DocumentFilter() {
			public void insertString(	DocumentFilter.FilterBypass fb,
										int offset, String string, AttributeSet attr)
												throws BadLocationException
			{
				if (string.equals("$"))
					super.insertString(fb, offset, "\u2192", attr);
				else if (string.equals("%"))
					super.insertString(fb, offset, "\u2194", attr);
				else if (string.equals("@"))
					super.insertString(fb, offset, "\u2200", attr);
				else if (string.equals("/"))
					super.insertString(fb, offset, "\u2203", attr);
				else if (string.equals("|"))
					super.insertString(fb, offset, "\u2228", attr);
				else if (string.equals("&"))
					super.insertString(fb, offset, "\u2227", attr);
				else if (string.equals("~"))
					super.insertString(fb, offset, "\u00AC", attr);
				else if (string.equals("!"))
					super.insertString(fb, offset, "\u00AC", attr);
				else
					super.insertString(fb, offset, string, attr);
			}
			public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
					throws BadLocationException
			{
				super.remove(fb, offset, length);
			}
			public void replace(	DocumentFilter.FilterBypass fb,
									int offset, int length, String text, AttributeSet attrs)
											throws BadLocationException
			{
				if (text.equals("$"))
					super.replace(fb, offset, length, "\u2192", attrs);
				else if (text.equals("%"))
					super.replace(fb, offset, length, "\u2194", attrs);
				else if (text.equals("@"))
					super.replace(fb, offset, length, "\u2200", attrs);
				else if (text.equals("/"))
					super.replace(fb, offset, length, "\u2203", attrs);
				else if (text.equals("|"))
					super.replace(fb, offset, length, "\u2228", attrs);
				else if (text.equals("&"))
					super.replace(fb, offset, length, "\u2227", attrs);
				else if (text.equals("~"))
					super.replace(fb, offset, length, "\u00AC", attrs);
				else if (text.equals("!"))
					super.replace(fb, offset, length, "\u00AC", attrs);
				else
					super.replace(fb, offset, length, text, attrs);
				
			}
		});
		newField.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && !e.isControlDown())
				{
					if (editLine != null)
					{
						reverseLineMap.get(editLine).setEditable(false);
						reverseLineMap.get(editLine).setFocusable(false);
					}
					if (!isTerminator)
					{
						newField.setEditable(true);
						newField.setFocusable(true);
					}
					newField.requestFocus();
					editLine = lineMap.get(newField);
					selectedLines = lineMap.get(newField).getSelectedLines();
					selectedBranches = lineMap.get(newField).getSelectedBranches();
					moveComponents();
					repaint();
				}
				else if (SwingUtilities.isRightMouseButton(e) || e.isControlDown())
				{
					BranchLine curLine = lineMap.get(newField);
					if (!isTerminator)
					{
						if (editLine != curLine && editLine != null &&
								(editLine == curLine.getDecomposedFrom() || curLine.getDecomposedFrom() == null || editLine instanceof BranchTerminator))
							toggleSelected(curLine);
					}
					else
					{
						((BranchTerminator)lineMap.get(newField)).switchIsClose();
						newField.setText(lineMap.get(newField).toString());
					}
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		newField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				TreePanel.this.dispatchEvent(e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				TreePanel.this.dispatchEvent(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
        line.typing = true;
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
          line.typing = false;
					TreePanel.this.requestFocus();
				}
				TreePanel.this.dispatchEvent(e);
        line.currentTyping = newField.getText();
        moveComponents();
			}
		});
		// Parse the statement when focus is lost
		newField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
        line.typing = false;
				Statement newStatement = ExpressionParser.parseExpression(newField.getText());
				if (newStatement != null)
				{
					if (newField.getParent() != null) // Ensures that the state isn't recorded twice when deleting a branch
						recordState();
					line.setStatement(newStatement);
					b.calculateWidestLine();
					newField.setText(newStatement.toString());
				}
				else
				{
					if (!newField.getText().equals(""))
					{
						if (line.getStatement() != null)
							newField.setText(line.toString());
						else
							newField.setText("");
						JOptionPane.showMessageDialog(	null, "Error: Invalid logical statement",
													"Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						if (newField.getParent() != null) // Ensures that the state isn't recorded twice when deleting a branch
							recordState();
						line.setStatement(null);
					}
				}
				moveComponents();
			}
			
			@Override
			public void focusGained(FocusEvent e) {}
		});
		lineMap.put(newField, line);
		reverseLineMap.put(line, newField);
		add(newField);
		newField.setEditable(false);
	}
	
	/**
	 * Adds a statement to a Branch
	 * @param b the Branch to which the Statement is added
	 * @param s the Statement added to the Branch
	 * @return the BranchLine (which contains Statement s) added to Branch b
	 */
	public BranchLine addStatement(Branch b, Statement s)
	{
		BranchLine newLine = addLine(b);
		newLine.setStatement(s);
		reverseLineMap.get(newLine).setText(s.toString());
		moveComponents();
		return newLine;
	}
	
	// temporary
	/**
	 * Adds a statement to the root of the tree
	 * @param s the Statement added to the tree
	 */
	public void addStatement(Statement s)
	{
		addStatement(root, s);
	}
	
	/**
	 * Adds a closed BranchTerminator to a branch
	 * @param b the Branch to which the BranchTerminator is added
	 * @return the BranchTerminator line that was added
	 */
	public BranchTerminator addTerminator(Branch b)
	{
		return (BranchTerminator) addLine(b, true, true);
	}
	
	/**
	 * Adds an open BranchTerminator to a branch
	 * @param b the Branch to which the BranchTerminator is added
	 * @return the BranchTerminator line that was added
	 */
	public BranchTerminator addOpenTerminator(Branch b)
	{
		return (BranchTerminator) addLine(b, true, false);
	}
	
	public void drawBranching(Branch b, Graphics2D g)
	{
		if (selectedBranches != null && selectedBranches.contains(b))
			g.setColor(BranchLine.SELECTED_COLOR);
		else
			g.setColor(BranchLine.DEFAULT_COLOR);
		JButton addButton = addBranchMap.get(b);
		if (addButton != null)
		{
			int midX = addButton.getX() + addButton.getWidth()/2;
			int topY = (int) addButton.getBounds().getMaxY();
			if (b.getBranches().size() > 1)
			{
				int midY = topY + Branch.VERTICAL_GAP/2;
				int bottomY = topY + Branch.VERTICAL_GAP;
				int leftX = (b.getWidestChild() + Branch.BRANCH_SEPARATION) * (b.getBranches().size()-1);
				leftX /= 2;
				leftX = midX - leftX;
				int rightX = leftX + (b.getWidestChild() + Branch.BRANCH_SEPARATION) * (b.getBranches().size() - 1);
				g.drawLine(midX, topY, midX, midY);
				g.drawLine(leftX, midY, rightX, midY);
				int curX = leftX;
				for (Branch curBranch : b.getBranches())
				{
					g.drawLine(curX, midY, curX, bottomY);
					curX += (b.getWidestChild() + Branch.BRANCH_SEPARATION);
					if (curBranch.getBranches().size() > 0)
						drawBranching(curBranch, g);
				}
			}
			else
				g.drawLine(midX, topY, midX, topY + Branch.VERTICAL_GAP);
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setClip(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(1.0f,1.0f,1.0f));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		g2d.setStroke(new BasicStroke(4.0f));
		
		drawStringAt(g2d,
		  new Point(center.x + getWidth()/2, center.y + getHeight()/2),
		  "Premises");
		
		drawStringAt(g2d,
		  new Point(center.x + getWidth()/2,
		    center.y + getHeight()/2 +
		    premises.numLines() * premises.getLineHeight() +
		    Branch.VERTICAL_GAP
		  ),
		  "Decomposition");
		
		if (root.getBranches().size() > 0)
			drawBranching(root, g2d);
	}
	
	private void drawStringAt(Graphics2D g2d, Point p, String toDraw)
	{
		FontMetrics fm = g2d.getFontMetrics();
		
		int centerX = p.x;
		int bottomY = p.y;
		
		int textX = centerX - fm.stringWidth(toDraw) / 2;
		int textY = bottomY - fm.getDescent() - fm.getLeading();
		
		g2d.drawString(toDraw, textX, textY);
	}
	
	/**
	 * Gets the root of the tree
	 * @return the Branch that is root of the tree
	 */
	public Branch getRootBranch()
	{
		return root;
	}
	
	/**
	 * Sets the root of the tree
	 * @param newRoot the Branch that is to be the new root of the tree
	 */
	public void setRoot(Branch newRoot)
	{
		root = newRoot;
		root.setFontMetrics(this.getFontMetrics(this.getFont()));
		root.getWidth();
	}
	
	/**
	 * Removes a line from the tree
	 * @param removedLine The line to remove
	 */
	private void removeLine(BranchLine removedLine)
	{
		recordState();
		BranchLine decomposedFrom = removedLine.getDecomposedFrom();
		if (decomposedFrom != null)
		{
			toggleSelected(removedLine, decomposedFrom.getSelectedLines());
		}
		if (!(removedLine instanceof BranchTerminator))
			for (BranchLine curLine : removedLine.getSelectedLines())
				curLine.setDecomposedFrom(null);
		int removeIndex = -1;
		for (int i = 0; i < removedLine.getParent().numLines(); i++)
		{
			if (removedLine.getParent().getLine(i) == removedLine)
			{
				removeIndex = i;
				break;
			}
		}
		removedLine.getParent().removeLine(removeIndex);
		JTextField removedField = reverseLineMap.get(removedLine);
		this.remove(removedField);
		lineMap.remove(removedField);
		reverseLineMap.remove(removedLine);
	}
	
	/**
	 * Unselects the currently selected line, modifying the context as such
	 */
	private void deselectCurrentLine()
	{
		editLine = null;
		selectedBranches = null;
		selectedLines = null;
	}
	
	/**
	 * Deletes the currently selected line
	 */
	public void deleteCurrentLine()
	{
		if (editLine == null && !editLine.isPremise())
			return;
		removeLine(editLine);
		deselectCurrentLine();
		moveComponents();
		repaint();
	}
	
	/**
	 * Removes a branch from the tree, deleting all references and removing its children
	 * @param b The branch to be removed
	 */
	private void deleteBranch(Branch b)
	{
		recordState();
		for (Branch curChild : b.getBranches())
			deleteBranch(curChild);
		for (int i = 0; i < b.numLines(); i++)
		{
			removeLine(b.getLine(i));
		}
		remove(addBranchMap.get(b));
		addBranchMap.remove(b);
		remove(addLineMap.get(b));
		addLineMap.remove(b);
		remove(branchMap.get(b));
		branchMap.remove(b);
		remove(terminateMap.get(b));
		terminateMap.remove(b);
		b.getRoot().removeBranch(b);
		if (b.getRoot().getBranches().size() == 0 && b.getRoot().getDecomposedFrom() != null)
			// This was the last child
		{
			b.getRoot().getDecomposedFrom().getSelectedBranches().remove(b.getRoot());
		}
	}
	
	/**
	 * Deletes the currently selected branch
	 * @return : False if the current branch is the root branch, true otherwise
	 */
	public boolean deleteCurrentBranch()
	{
		Branch selectedBranch = editLine.getParent();
		if (selectedBranch == root || selectedBranch == premises)
		{
			JOptionPane.showMessageDialog(null,
					"Cannot delete root and premise branches!",
					"Delete branch error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		deleteBranch(selectedBranch);
		deselectCurrentLine();
		moveComponents();
		repaint();
		return true;
	}
	
	public void deleteFirstPremise()
	{
		removeLine(premises.getLine(0));
	}
	
}
