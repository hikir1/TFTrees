package perl.aaron.TruthTrees.graphics;

import static perl.aaron.TruthTrees.Branch.MIN_WIDTH;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import perl.aaron.TruthTrees.Branch;
import perl.aaron.TruthTrees.BranchLine;
import perl.aaron.TruthTrees.BranchTerminator;
import perl.aaron.TruthTrees.ExpressionParser;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.util.History;
import perl.aaron.TruthTrees.util.MutOption;
import perl.aaron.TruthTrees.util.NonNull;
import perl.aaron.TruthTrees.util.NoneResult;
import perl.aaron.TruthTrees.util.UserError;

class Global {
	static String var;
	static Statement s1;
	static Statement s2;
}

class RadioPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private JRadioButton jRadioButton1;

	private JRadioButton jRadioButton2;

	private JButton jButton;

	private ButtonGroup buttonGroup;

	private JLabel label;

	private ArrayList<JRadioButton> buttons;

	// Constructor of Demo class.
	 RadioPanel(Set<String> variables) {
		// Setting layout as null of JFrame.
		this.setLayout(null);

		jButton = new JButton("Submit"); // Initialization of object of "ButtonGroup" class.
		buttonGroup = new ButtonGroup();
		label = new JLabel("Choose variable to split");

		int x = 200;
		int y = 200;
		buttons = new ArrayList<>();

		var itr = variables.iterator();
		while (itr.hasNext()) {
			JRadioButton button = new JRadioButton();
			button.setText(itr.next().toString());
			buttonGroup.add(button);
			buttons.add(button);
			this.add(button);
			button.setBounds(x, 30, y, 50);
			x += 100;
			y -= 40;
		}

		// Setting Bounds of "jButton".
		jButton.setBounds(125, 90, 80, 30);

		// Setting Bounds of JLabel "L2".
		label.setBounds(20, 30, 200, 50);

		this.add(jButton);

		// Adding JLabel "L2" on JFrame.
		this.add(label);

		// Adding Listener to JButton.
		jButton.addActionListener(new ActionListener() {
			// Anonymous class.

			public void actionPerformed(ActionEvent e) {
				// Override Method

				// Declaration of String class Objects.
				String qual = " ";
				for (int i = 0; i < buttons.size(); i++) {
					JRadioButton button = buttons.get(i);
					if (button.isSelected()) {
						qual = button.getText();
					}
				}
				if (qual.equals(" ")) {

					qual = "NO variable selected";
				}

				// MessageDialog to show information selected radion buttons.
				JOptionPane.showMessageDialog(RadioPanel.this, qual);
				Global.var = qual;
				RadioPanel.this.dispose();
			}
		});
	}
}

/**
 * An extension of JPanel for displaying and interacting with a sequence of
 * TreeLines
 * 
 * @author Aaron Perl, Sarah Mogielnicki
 *
 */
public class TreePanel extends JPanel {

	private static final long serialVersionUID = 1;
	private static final int UNDO_STACK_SIZE = 32;

	private final Point center = new Point(0, -50);
	private final Point prevCenter = new Point(center);
	private final Point clickPoint = new Point();
	private final MutOption<BranchLine> editLine = new MutOption<>();
	private final Map<Branch, JButton> addBranchMap = new HashMap<>();
	private final Map<Branch, JButton> addLineMap = new HashMap<>();
	private final Map<Branch, JButton> branchMap = new HashMap<>();
	private final Map<Branch, JButton> terminateMap = new HashMap<>();
	private final Map<JTextField, BranchLine> lineMap = new HashMap<>();
	private final Map<BranchLine, JTextField> reverseLineMap = new HashMap<>();
	private final MutOption<Set<BranchLine>> selectedLines = new MutOption<>();
	private final MutOption<Set<Branch>> selectedBranches = new MutOption<>();
	private final NonNull<Branch> premises = new NonNull<>(addBranchNoMove(null, false, null));
	private final NonNull<Branch> root = new NonNull<>(addBranchNoMove(premises.get(), false, null));
	private final History hist = new History(UNDO_STACK_SIZE);
	
	private int zoomLevel = 0;
	private int decompNumber = 1;
	private float size = 12;
	private double zoomMultiplicationFactor = 1.1;
	
	private enum Completion {
		ALL_CLOSED,
		ONE_OPEN,
		INVALID
	}
	
	private static final Map<String,String> SYMBOLS =
			Map.of(
				"$", "\u2192",
				"%", "\u2194",
				"@", "\u2200",
				"/", "\u2203",
				"|", "\u2228",
				"&", "\u2227",
				"~", "\u00AC",
				"!", "\u00AC"
			);
	
	public TreePanel() {
		super();
		setOpaque(false);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);
		
		addPremise();
		
		setFocusable(true);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				prevCenter.setLocation(center);
				clickPoint.setLocation(e.getPoint());
				requestFocus();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				center.setLocation(e.getPoint().x - clickPoint.x + prevCenter.x,
						e.getPoint().y - clickPoint.y + prevCenter.y);
				moveComponents();
				validate();
				repaint();
			}
		});

	}

	/**
	 * Undoes the previous state change.
	 */
	public void undoState() {
		hist.undo();
	}

	/**
	 * Performs the previously undone state change again
	 */
	public void redoState() {
		hist.redo();
	}
	
	public void clear() {
		center.setLocation(0, -50);
		prevCenter.setLocation(center);
		size = 12f;
		zoomLevel = 0;
		decompNumber = 1;
		editLine.setNone();
		selectedLines.setNone();
		selectedBranches.setNone();
		addBranchMap.clear();
		addLineMap.clear();
		branchMap.clear();
		terminateMap.clear();
		lineMap.clear();
		reverseLineMap.clear();
		this.setFont(this.getFont().deriveFont(size));
		premises.set(addBranch(null, true));
		hist.clear();

		root.set(addBranch(premises.get(), false));
	}

	/**
	 * Makes a new BranchLine that is a premise, with no statement
	 */
	public void addPremise() {
		addPremise(null);
	}

	/**
	 * Makes a new BranchLine that is a premise, and sets its statement to s
	 *
	 * @param s The premise that is added
	 */
	private void addPremise(final Statement s) { // TODO: worry about removing first premise?
		BranchLine newLine = addLine(premises.get());
		newLine.setIsPremise(true);
		if (s != null) {
			newLine.setStatement(s);
			reverseLineMap.get(newLine).setText(s.toString());
			moveComponents();
		}
	}
	
	/**
	 * Checks that the line is decomposed properly
	 * 
	 * @param l The BranchLine that is being checked
	 * @return null if decomposed properly, an error message otherwise
	 */
	private void checkLine(BranchLine l) throws UserError {
		l.verifyDecomposition();
	}

	/**
	 * Checks all the lines in a branch to see if they are decomposed properly
	 * 
	 * @param b Branch being checked
	 * @return null if decomposed properly, an error message otherwise
	 */
	private void checkBranch(Branch b) throws UserError {
		for (int i = 0; i < b.numLines(); i++)
			checkLine(b.getLine(i));

		for (Branch curBranch : b.getBranches())
			checkBranch(curBranch);

	}

	/**
	 * Checks that the tree is complete (by checking that no branches are open and
	 * that all of the branches are closed).
	 * 
	 * @return 0 if all branches close with no open branches, 1 if all branches
	 *         terminate but at least one is marked open, -1 otherwise
	 */
	private Completion checkCompletion() {
		boolean isOpen = checkForOpenBranch(root.get());
		boolean allClosed = checkForAllClosed(root.get());
		if (!isOpen && allClosed)
			return Completion.ALL_CLOSED;
		else if (!allClosed && !isOpen)
			return Completion.INVALID;
		else
			return Completion.ONE_OPEN;
	}

	/**
	 * Recursively checks if b or any of its children are open
	 * 
	 * @param b the Branch that is being checked
	 * @return true if b or any of its children are open
	 */
	public static boolean checkForOpenBranch(Branch b) {
		if (b.isOpen())
			return true;
		for (Branch child : b.getBranches()) {
			if (checkForOpenBranch(child))
				return true;
		}
		return false;
	}

	/**
	 * Recursively checks if b and all if its children are closed
	 * 
	 * @param b the Branch being checked for closed
	 * @return false if b or one of its children is not closed, true otherwise
	 */
	private boolean checkForAllClosed(Branch b) {
		if (b.getBranches().size() == 0 && !b.isClosed())
			return false;
		for (Branch child : b.getBranches()) {
			if (!checkForAllClosed(child))
				return false;
		}
		return true;
	}

	/**
	 * Recursively checks if the branch b has a valid open terminator
	 * 
	 * @param b the branch being checked for a valid open branch
	 * @return true if b has a valid open terminator, false otherwise
	 */
	private void verifyOpenTerminator(Branch b) throws UserError { //TODO: make open part of state
		// If there are no open branches anywhere return false

		if (b.getBranches().size() == 0 && b.isOpen()) {
			b.verifyTerminations();
			return;
		}

		for (Branch child : b.getBranches()) {
			if (checkForOpenBranch(child)) {
				verifyOpenTerminator(child);
				return;
			}
		}
		throw new UserError("There is no open branch");

	}

	/**
	 * Recursively checks if b and all if its children are have verified Terminators
	 * 
	 * @param b the Branch being checked for verified terminators
	 *         verified, true otherwise
	 */
	private void verifyTerminators(Branch b) throws UserError {

		if (checkForOpenBranch(b)) { // If there are open branches in the current level or below

			// Check that the open branch is valid
			verifyOpenTerminator(b);
			
		} else { // No open branches in tree, proceed as normal
			if (b.getBranches().size() == 0)
				b.verifyTerminations();

			for (Branch child : b.getBranches())
				verifyTerminators(child);

		}
	}
	
	private void openVerifyLines(Branch b) throws UserError {
		openVerifyLines(b, false);
	}

	// 'lax' indicates that lines need not be decomposed, but still check for validity if they are
	private void openVerifyLines(Branch b, boolean lax) throws UserError {
		for(BranchLine line: b.getLines())
			line.verifyDecompositionOpen(lax);

		for(Branch child: b.getBranches()) {
			boolean doLax = lax || !checkForOpenBranch(child);
			openVerifyLines(child, doLax);
		}

	}

	/**
	 * Runs all of the "check" methods on the whole tree.
	 * 
	 * @return null if tree is correct and complete, error message otherwise
	 */
	public void check() throws UserError {
		verifyTerminators(root.get());

		switch (checkCompletion()) {
		case ALL_CLOSED:
			checkBranch(premises.get());
			checkBranch(root.get());
			return;
		case ONE_OPEN:
			openVerifyLines(premises.get());
			openVerifyLines(root.get());
			return;
		default: // INVALID
			throw new UserError("Not all branches are closed and no branch has been marked as open!");
		}
	}

	/**
	 * Checks the selected line.
	 *
	 */
	public void checkSelectedLine() throws UserError {
		try {
			checkLine(editLine.unwrap());
		} catch (NoneResult e) {
			throw new UserError("No statement is currently selected!");
		}
	}

	/**
	 * 
	 * @param parent
	 */
	private void addBranch(Branch parent) {
		addBranch(parent, true);
	}

	/**
	 * Makes a new Branch and adds it to the parent branch. Adds the first line to
	 * the branch if addFirstLine is true.
	 * 
	 * @param parent       the root branch for this branch
	 * @param addFirstLine if true, the method will add a line to the new branch
	 * @return the new branch that was added
	 */
	public Branch addBranch(Branch parent, boolean addFirstLine) {
		return addBranch(parent, addFirstLine, null);
	}

	public Branch addBranch(final Branch parent, final boolean addFirstLine, final Statement s) {
		final Branch newBranch = addBranchNoMove(parent, addFirstLine, s);
		moveComponents();
		repaint();
		return newBranch;
	}
	
	public Branch addBranchNoMove(final Branch parent, final boolean addFirstLine, final Statement s) {
		final Branch newBranch = new Branch(parent);
		newBranch.setFontMetrics(getFontMetrics(getFont()));
		makeButtonsForBranch(newBranch);
		if (parent != null)
			parent.addBranch(newBranch);
		if (addFirstLine) {
			if(s != null) {
				addLine(newBranch, s);
				newBranch.getLine(0).setIsPremise(true);
			}
			else
				addLine(newBranch);
			if (parent == null)
				newBranch.getLine(0).setIsPremise(true);
		}
		return newBranch;
	}
	
	/**
	 * Makes all the buttons for the branch
	 * 
	 * @param b Branch that will get the buttons
	 */
	private void makeButtonsForBranch(Branch b) {
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
		branchButton.addActionListener(e -> addBranch(myBranch));
		lineButton.addActionListener(e -> addLine(myBranch));
		terminateButton.addActionListener(e -> addTerminator(myBranch));
		decompButton.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				selectedBranches.if_some(selectedBranches -> {
					if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
						try {
							if (selectedBranches.contains(myBranch)) {
								selectedBranches.remove(myBranch);
								myBranch.setDecomposedFrom(null);
							} else if (myBranch.getDecomposedFrom() == null) {
								myBranch.setDecomposedFrom(editLine.unwrap());
								selectedBranches.add(myBranch);
							}
							TreePanel.this.repaint();
						} catch(NoneResult r) {
							r.printStackTrace();
						}
					}
				});
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
	 * 
	 * @param b the Branch that is being checked for selection
	 * @return true if b is selected, false otherwise
	 */
	private boolean isSelected(BranchLine b) {
		try {
			return selectedLines.unwrap().contains(b);
		}
		catch(NoneResult r) {
			return false;
		}
	}

	private void toggleSelected(BranchLine b, Set<BranchLine> curSelected) {
		try {
			if (curSelected.contains(b)) {
				curSelected.remove(b);
				reverseLineMap.get(b).setBackground(BranchLine.DEFAULT_COLOR);
				if (!(editLine.unwrap() instanceof BranchTerminator))
					b.setDecomposedFrom(null);
			} else {
				curSelected.add(b);
				reverseLineMap.get(b).setBackground(BranchLine.SELECTED_COLOR);
				if (!(editLine.unwrap() instanceof BranchTerminator))
					b.setDecomposedFrom(editLine.unwrap());
			}
		} catch(NoneResult r) {
			r.printStackTrace();
		}
	}

	private void toggleSelected(BranchLine b) {
		try {
			toggleSelected(b, selectedLines.unwrap());
		}
		catch(NoneResult r) {
			r.printStackTrace();
		}
	}

	private void moveBranch(Branch b, Point origin) {
		int verticalOffset = 0;
		int maxLineWidth = b.getWidestLine();
		int maxWidth = b.getWidestChild();
		for (int i = 0; i < b.numLines(); i++) {
			BranchLine curLine = b.getLine(i);
			JTextField curField = reverseLineMap.get(curLine);
			if (isSelected(curLine))
				curField.setBackground(BranchLine.SELECTED_COLOR);
			else if (editLine.valEquals(curLine))
				curField.setBackground(BranchLine.EDIT_COLOR);
			else
				curField.setBackground(BranchLine.DEFAULT_COLOR);
			curField.setBounds(origin.x - maxLineWidth / 2, origin.y + verticalOffset, maxLineWidth, b.getLineHeight());
			curField.repaint();
			verticalOffset += b.getLineHeight();
			if (curLine.decompNum != -1){

				String tickMark = "\u221A" + generateSubscript(curLine.decompNum);
				Graphics2D g2d = (Graphics2D)this.getGraphics();
				Point p = curField.getLocation();
				p.setLocation((p.getX()+curField.getWidth()+10), (p.getY()+(curField.getHeight()/2)+7));
				drawStringAt(g2d, p, tickMark);
			}
		}
		if (b != premises.get()) {
			JButton lineButton = addLineMap.get(b);
			JButton addButton = addBranchMap.get(b);
			JButton branchButton = branchMap.get(b);
			JButton terminateButton = terminateMap.get(b);
			int horizontalOffset = (maxWidth + Branch.BRANCH_SEPARATION) * (b.getBranches().size() - 1);
			horizontalOffset /= -2;
			if (!b.isClosed() && !b.isOpen()) {
				lineButton.setBounds(origin.x - maxLineWidth / 2, origin.y + verticalOffset, maxLineWidth,
						b.getLineHeight());
				verticalOffset += b.getLineHeight();
				addButton.setBounds(origin.x - maxLineWidth / 2, origin.y + verticalOffset, maxLineWidth,
						b.getLineHeight());
				verticalOffset += b.getLineHeight();
				if (b.getBranches().size() == 0) {
					terminateButton.setBounds(origin.x - maxLineWidth / 2, origin.y + verticalOffset, maxLineWidth,
							b.getLineHeight());
					terminateButton.setVisible(true);
					terminateButton.setEnabled(true);
					verticalOffset += b.getLineHeight();
				} else {
					terminateButton.setVisible(false);
					terminateButton.setEnabled(false);
				}
				terminateButton.repaint();
				branchButton.setBounds(origin.x + horizontalOffset - maxWidth / 2, origin.y + verticalOffset,
						-horizontalOffset * 2 + maxWidth, Branch.VERTICAL_GAP);
				addButton.setVisible(true);
				addButton.setEnabled(true);
				lineButton.setVisible(true);
				lineButton.setEnabled(true);
				branchButton.setEnabled(true);
			} else {
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
			for (Branch curChild : b.getBranches()) {
				moveBranch(curChild, new Point(origin.x + horizontalOffset, origin.y + verticalOffset));
				horizontalOffset += (maxWidth + Branch.BRANCH_SEPARATION);
			}
		}
	}

	public void moveComponents() {
		Point origin = new Point(center.x + getWidth() / 2, center.y + getHeight() / 2);
		moveBranch(premises.get(), origin);
		origin.translate(0, premises.get().getLineHeight() * premises.get().numLines());
		origin.translate(0, 20);
		moveBranch(root.get(), origin);
	}

	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}

	/**
	 * Adds a BranchLine that is not a terminator
	 * 
	 * @param b the Branch that the BranchLine is added to
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b) {
		return addLine(b, false);
	}

	/**
	 * Adds a BranchLine that is not a terminator
	 * Specific to the split function where user does not add the line
	 * 
	 * @param b the Branch that the BranchLine is added to
	 * @param s the Statement that is added to the Branchline
	 *

	 */
	private void addLine(final Branch b, final Statement s) {
		addLine(b, false, true, true, s);
	}

	/**
	 * Adds a BranchLine that may or may not be a terminator
	 * 
	 * @param b            the Branch that the BranchLine is added to
	 * @param isTerminator is true if if this BranchLine is a terminator
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b, final boolean isTerminator) {
		return addLine(b, isTerminator, true);
	}

	/**
	 * Adds a new branch line, which can be a terminator.
	 * 
	 * @param b            the Branch to which the line is being added
	 * @param isTerminator true if the line is a terminator
	 * @param isClose
	 * @return the BranchLine that was added
	 */
	private BranchLine addLine(final Branch b, final boolean isTerminator, final boolean isClose) {

		return addLine(b, isTerminator, isClose, false, null);

	}

	/**
	 * Adds a new branch line, which can be a terminator.
	 * Used when user does not add the line
	 * 
	 * @param b            the Branch to which the line is being added
	 * @param isTerminator true if the line is a terminator
	 * @param isClose
	 * @param wasNotTyped check if user added the line or code did
	 * @param s the Statement added to the Branchline
	 * 
	 * @return the BranchLine that was added
	 */
	private BranchLine  addLine(final Branch b, final boolean isTerminator, final boolean isClose,
			final boolean wasNotTyped, final Statement s) {
		final BranchLine newLine;
		if (wasNotTyped) {
			newLine = b.addStatement(s);
		} else if (isTerminator) {
			newLine = new BranchTerminator(b);
			if (!isClose)
				((BranchTerminator) newLine).switchIsClose();
			b.addTerminator((BranchTerminator) newLine);
		} else
			newLine = b.addStatement(null);
		makeTextFieldForLine(newLine, b, isTerminator);
		moveComponents();
		return newLine;
	}
	
	private enum LinePlacement {
		BEFORE, AFTER
	}

	public void addLineBefore() throws UserError {
		addLine(LinePlacement.BEFORE);
	}
	
	public void addLineAfter() throws UserError {
		addLine(LinePlacement.AFTER);
	}
	
	private void addLine(final LinePlacement placement) throws UserError {
		try {
			final BranchLine editLine = this.editLine.unwrap();
			BranchLine newLine = null;
			for (int i = 0; i < editLine.getParent().numLines(); i++) {
				if (editLine.getParent().getLine(i) == editLine) {
					newLine = editLine.getParent().addStatement(null, placement == LinePlacement.AFTER? i + 1: i);
					break;
				}
			}
			assert newLine != null : "Failed to find editLine";
			makeTextFieldForLine(newLine, editLine.getParent(), false);
			moveComponents();
		}
		catch(NoneResult r) {
			throw new UserError("No line selected.");
		}		
	}
	
	private void makeTextFieldForLine(final BranchLine line, final Branch b, final boolean isTerminator) {
		final JTextField newField = new JTextField("");
		newField.setUI(new BasicTextFieldUI() {
			@Override
			public void paintBackground(Graphics g) {
				super.paintBackground(g);
				Color old = g.getColor();
				g.setColor(getBackground());
				g.fillRect(getX(), getY(), getWidth(), getHeight());
				g.setColor(old);
			}
		});
		if (line.getStatement() != null) {
			System.out.println("toString:" + line.getStatement().toString());
			newField.setText(line.getStatement().toString());
		}
		if (isTerminator) {
			newField.setText(line.toString());
			newField.setForeground(new Color(0.7f, 0.0f, 0.0f));
		}
		if (b == premises.get())
			line.setIsPremise(true);
		newField.setEditable(false);
		newField.setFocusable(false);
		newField.setHorizontalAlignment(JTextField.CENTER);
		newField.setFont(this.getFont().deriveFont(size));
		((AbstractDocument) newField.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				super.insertString(fb, offset, SYMBOLS.getOrDefault(string, string), attr);
			}

			public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				super.replace(fb, offset, length, SYMBOLS.getOrDefault(text, text), attrs);
			}
		});
		newField.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && !e.isControlDown()) {
					editLine.if_some(editLine -> {
						reverseLineMap.get(editLine).setEditable(false);
						reverseLineMap.get(editLine).setFocusable(false);
					});
					if (!isTerminator) {
						newField.setEditable(true);
						newField.setFocusable(true);
					}
					newField.requestFocus();
					editLine.set(lineMap.get(newField));
					selectedLines.set(lineMap.get(newField).getSelectedLines());
					selectedBranches.set(lineMap.get(newField).getSelectedBranches());
					moveComponents();
					repaint();
				} else if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
					BranchLine curLine = lineMap.get(newField);
					if (!isTerminator) {
						editLine.if_some(editLine -> {
							if (editLine != curLine && !(curLine instanceof BranchTerminator))
								toggleSelected(curLine);
						});
					} else {
						((BranchTerminator) lineMap.get(newField)).switchIsClose();
						newField.setText(lineMap.get(newField).toString());
					}
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
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
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
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
				if (newStatement != null) {

					line.setStatement(newStatement);
					b.calculateWidestLine();
					newField.setText(newStatement.toString());
				} else {
					if (!newField.getText().equals("")) {
						if (line.getStatement() != null)
							newField.setText(line.toString());
						else
							newField.setText("");
						JOptionPane.showMessageDialog(null, "Error: Invalid logical statement", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {

						line.setStatement(null);
					}
				}
				moveComponents();
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		lineMap.put(newField, line);
		reverseLineMap.put(line, newField);
		add(newField);
		newField.setEditable(false);
	}

	/**
	 * Adds a statement to a Branch
	 * 
	 * @param b the Branch to which the Statement is added
	 * @param s the Statement added to the Branch
	 * @return the BranchLine (which contains Statement s) added to Branch b
	 */
	public BranchLine addStatement(Branch b, Statement s) {
		BranchLine newLine = addLine(b);
		newLine.setStatement(s);
		reverseLineMap.get(newLine).setText(s.toString());
		moveComponents();
		return newLine;
	}

	// temporary
	/**
	 * Adds a statement to the root of the tree
	 * 
	 * @param s the Statement added to the tree
	 */
	/*
	public void addStatement(Statement s) {
		addStatement(root.get(), s);
	}
	*/

	/**
	 * Adds a closed BranchTerminator to a branch
	 * 
	 * @param b the Branch to which the BranchTerminator is added
	 * @return the BranchTerminator line that was added
	 */
	public BranchTerminator addTerminator(Branch b) {
		return (BranchTerminator) addLine(b, true, true);
	}

	/**
	 * Adds an open BranchTerminator to a branch
	 * 
	 * @param b the Branch to which the BranchTerminator is added
	 * @return the BranchTerminator line that was added
	 */
	public BranchTerminator addOpenTerminator(Branch b) {
		return (BranchTerminator) addLine(b, true, false);
	}

	private void drawBranching(Branch b, Graphics2D g) {
		g.setColor(BranchLine.DEFAULT_COLOR);
		selectedBranches.if_some(selectedBranches -> {
			if (selectedBranches.contains(b))
				g.setColor(BranchLine.SELECTED_COLOR);
		});
			
		JButton addButton = addBranchMap.get(b);
		if (addButton != null) {
			int midX = addButton.getX() + addButton.getWidth() / 2;
			int topY = (int) addButton.getBounds().getMaxY();
			if (b.getBranches().size() > 1) {
				int midY = topY + Branch.VERTICAL_GAP / 2;
				int bottomY = topY + Branch.VERTICAL_GAP;
				int leftX = (b.getWidestChild() + Branch.BRANCH_SEPARATION) * (b.getBranches().size() - 1);
				leftX /= 2;
				leftX = midX - leftX;
				int rightX = leftX + (b.getWidestChild() + Branch.BRANCH_SEPARATION) * (b.getBranches().size() - 1);
				g.drawLine(midX, topY, midX, midY);
				g.drawLine(leftX, midY, rightX, midY);
				int curX = leftX;
				for (Branch curBranch : b.getBranches()) {
					g.drawLine(curX, midY, curX, bottomY);
					curX += (b.getWidestChild() + Branch.BRANCH_SEPARATION);
					if (curBranch.getBranches().size() > 0)
						drawBranching(curBranch, g);
				}
			} else
				g.drawLine(midX, topY, midX, topY + Branch.VERTICAL_GAP);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setClip(0, 0, getWidth(), getHeight());
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(1.0f, 1.0f, 1.0f));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		g2d.setStroke(new BasicStroke(4.0f));
		drawStringAt(g2d, new Point(center.x + getWidth() / 2, center.y + getHeight() / 2), "Premises");

		drawStringAt(g2d, new Point(center.x + getWidth() / 2,
				center.y + getHeight() / 2 + premises.get().numLines() * premises.get().getLineHeight() + Branch.VERTICAL_GAP),
				"Decomposition");

		for (BranchLine l : reverseLineMap.keySet()){
			if (l.decompNum != -1){
				String tickMark = "\u221A" + generateSubscript(l.decompNum);
				JTextField field = reverseLineMap.get(l);
				Point p = field.getLocation();
				p.setLocation((p.getX()+field.getWidth()+10), (p.getY()+(field.getHeight()/2)+7));
				drawStringAt(g2d, p, tickMark);
			}
		}



		if (root.get().getBranches().size() > 0)
			drawBranching(root.get(), g2d);
	}

	private void drawStringAt(Graphics2D g2d, Point p, String toDraw) {
		FontMetrics fm = g2d.getFontMetrics();

		int centerX = p.x;
		int bottomY = p.y;

		int textX = centerX - fm.stringWidth(toDraw) / 2;
		int textY = bottomY - fm.getDescent() - fm.getLeading();

		g2d.drawString(toDraw, textX, textY);
	}

	/**
	 * Gets the root of the tree
	 * 
	 * @return the Branch that is root of the tree
	 */
	public Branch getRootBranch() {
		return root.get();
	}

	/**
	 * Removes a line from the tree
	 * 
	 * @param removedLine The line to remove
	 */
	private void removeLine(BranchLine removedLine) {
		BranchLine decomposedFrom = removedLine.getDecomposedFrom();
		if (decomposedFrom != null) {
			toggleSelected(removedLine, decomposedFrom.getSelectedLines());
		}
		if (!(removedLine instanceof BranchTerminator))
			for (BranchLine curLine : removedLine.getSelectedLines())
				curLine.setDecomposedFrom(null);
		int removeIndex = -1;
		for (int i = 0; i < removedLine.getParent().numLines(); i++) {
			if (removedLine.getParent().getLine(i) == removedLine) {
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
	private void deselectCurrentLine() {
		editLine.setNone();
		selectedBranches.setNone();
		selectedLines.setNone();
	}

	/**
	 * Deletes the currently selected line
	 */
	public void deleteCurrentLine() {
		editLine.if_some(editLine -> {
			removeLine(editLine);
			deselectCurrentLine();
			moveComponents();
			repaint();
		});
	}

	/**
	 * Removes a branch from the tree, deleting all references and removing its
	 * children
	 * 
	 * @param b The branch to be removed
	 */
	private void deleteBranch(Branch b) {
		for (Branch curChild : b.getBranches())
			deleteBranch(curChild);
		for (int i = 0; i < b.numLines(); i++) {
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
	 */
	public void deleteCurrentBranch() throws UserError {
		try {
			Branch selectedBranch = editLine.unwrap().getParent();
			if (selectedBranch == root.get() || selectedBranch == premises.get())
				throw new UserError("Cannot delete root and premise branches!");
			deleteBranch(selectedBranch);
			deselectCurrentLine();
			moveComponents();
			repaint();
		}
		catch(NoneResult r) {
			throw new UserError("None line selected.");
		}
	}

	public void deleteFirstPremise() {
		removeLine(premises.get().getLine(0));
	}

	public void zoomIn() {
		if (zoomLevel >= 3)
			return;
		zoomLevel++;
		double ratio = zoomMultiplicationFactor;
		Font oldF = getFont();
		Font newF = oldF.deriveFont((float) (oldF.getSize2D() * ratio));
		size = size * (float) ratio;
		setFont(newF);
		for (Branch branch : addBranchMap.keySet()) {
			int numLines = branch.numLines();
			branch.width *= ratio;
			MIN_WIDTH *= ratio;
			branch.addStatement(null);
			branch.removeLine(numLines);
			
		}
		for (JTextField text : lineMap.keySet()) {
			int width = (int)(text.getWidth() * ratio);
			int height = (int)(text.getHeight() * ratio);
			text.setSize(width,height);
			text.setFont(newF);
		}
		this.setFont(this.getFont().deriveFont(size));
		this.repaint();
		moveComponents();
	}

	public void zoomOut() {
		if (zoomLevel <= -3)
			return;
		zoomLevel--;
		double ratio = ( 1.0 / zoomMultiplicationFactor );
		Font oldF = getFont();
		Font newF = oldF.deriveFont( (float)( oldF.getSize2D() * ratio )  );
		size = size * (float)ratio;
		setFont( newF );      
		for (Branch branch : addBranchMap.keySet()) {
			int numLines = branch.numLines();
			branch.width *= ratio;
			MIN_WIDTH *= ratio;
			branch.addStatement(null);
			branch.removeLine(numLines);
			
		}
		for (JTextField text : lineMap.keySet()) {
			int width = (int)(text.getWidth() * ratio);
			int height = (int)(text.getHeight() * ratio);
			text.setSize(width,height);
			text.setFont(newF);
		}
		this.setFont(this.getFont().deriveFont(size));
		this.repaint();
		moveComponents();
	}

	// intermediate function
	public void split() throws UserError {
		try {
			split(editLine.unwrap());
		}
		catch (NoneResult r) {
			throw new UserError("No statement is currently selected!");
		}
	}

	/**
	 * Splits the selected Branchline
	 * 
	 * @param l The Branchline to be split
	 * 
	 * @return String that describes success
	 */
	private void split(final BranchLine l) {
		Set<String> vars = l.split();
		RadioPanel rp = new RadioPanel(vars);
		// Setting Bounds of JFrame. 
        rp.setBounds(100, 100, 220*vars.size(), 200); 
  
        // Setting Title of frame. 
        rp.setTitle("Split Window"); 

        // Setting Visible status of frame as true. 
		rp.setVisible(true); 
		rp.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent we) {
			}
		
			@Override
			public void windowActivated(WindowEvent we) {
			}

			@Override
			public void windowClosed(WindowEvent we) {
				String var = Global.var;
				Global.s1 = ExpressionParser.parseExpression(var);
				Global.s2 = ExpressionParser.parseExpression("\u00AC"+var);
				if (l.getParent() == premises.get()){
					TreePanel.this.addBranch(root.get(), true, Global.s1);
					TreePanel.this.addBranch(root.get(), true, Global.s2);
				}
				else{
					TreePanel.this.addBranch(l.getParent(),  true, Global.s1);
					TreePanel.this.addBranch(l.getParent(),  true, Global.s2);
				}
			}
		});

	}

	// intermediate function
	public void mark() throws UserError {
		try {
			mark(editLine.unwrap());
		}
		catch(NoneResult r) {
			throw new UserError("No statement is currently selected!");
		}
	}

	// helper function
	private String generateSubscript(int i) {
		StringBuilder sb = new StringBuilder();
		for (char ch : String.valueOf(i).toCharArray()) {
			sb.append((char) ('\u2080' + (ch - '0')));
		}
		return sb.toString();
	}

	/**
	 * Adds decomposition marks to the tree
	 * 
	 * @param l The Branchline to be marked
	 * 
	 * @return String that describes success
	 */
	private void mark(final BranchLine l) {
		String tickMark = "\u221A" + generateSubscript(decompNumber);
		l.decompNum = decompNumber;
		decompNumber++;
		Graphics2D g2d = (Graphics2D)this.getGraphics();
		JTextField field = reverseLineMap.get(l);
		Point p = field.getLocation();
		p.setLocation((p.getX()+field.getWidth()+10), (p.getY()+(field.getHeight()/2)+7));
		drawStringAt(g2d, p, tickMark);
	}
}
