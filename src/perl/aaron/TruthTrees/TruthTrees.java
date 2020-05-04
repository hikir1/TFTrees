package perl.aaron.TruthTrees;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import perl.aaron.TruthTrees.graphics.TreePanel;
import perl.aaron.TruthTrees.util.FileFormatException;
import perl.aaron.TruthTrees.util.NoneResult;
import perl.aaron.TruthTrees.util.UserError;

public class TruthTrees {

	private static int instances = 0;

	private static final String VERSION = "1.3";

	// keeps track of all instances
	private static void close() {
		instances--;
		if (instances == 0) {
			System.exit(0);
		}
	}
	
	private static void showMessage(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	private static void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	// Starts new window
	private static void createNewInstance() {
		final JFrame frame = new JFrame("Truth Tree");
		frame.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu viewMenu = new JMenu("View");
		JMenu treeMenu = new JMenu("Tree");
		JMenu helpMenu = new JMenu("Help");
		final TreePanel treePanel = new TreePanel();

		frame.getContentPane().add(treePanel, BorderLayout.CENTER);
		frame.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(treeMenu);
		menuBar.add(helpMenu);

		/////////////////////////////
		// Check Tree Button
		/////////////////////////////

		JMenuItem checkButton = new JMenuItem("Check Tree");

		treeMenu.add(checkButton);
		checkButton.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_DOWN_MASK));
		checkButton.addActionListener( event -> {
			String msg;
			try {
				treePanel.check();
				msg = "The tree is correct!";
			}
			catch(UserError userError) {
				msg = "The tree is invalid!\n" + userError.getMessage();
			}
			showMessage(msg, "Check Tree");
		});

		////////////////////////////
		// Verify Line Button
		////////////////////////////

		JMenuItem checkLineButton = new JMenuItem("Verify Current Line");

		treeMenu.add(checkLineButton);
		checkLineButton.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
		checkLineButton.addActionListener(event -> {
			String msg;
			try {
				treePanel.checkSelectedLine();
				msg = "This line is correctly decomposed!";
			}
			catch(UserError userError) {
				msg = userError.getMessage();
			}
			showMessage(msg, "Verify Current Line");
		});

		///////////////////////////////
		// New Button
		/////////////////////////////

		treeMenu.addSeparator();

		JMenuItem newButton = new JMenuItem("New");

		fileMenu.add(newButton);
		newButton.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		newButton.addActionListener(event -> createNewInstance()); 

		///////////////////////////////
		// Save Button
		///////////////////////////////

		JMenuItem saveButton = new JMenuItem("Save");

		fileMenu.add(saveButton);
		saveButton.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveButton.addActionListener(event -> FileManager.saveFile(treePanel));

		////////////////////////////////
		// Save As Button
		////////////////////////////////

		JMenuItem saveAsButton = new JMenuItem("Save As");

		fileMenu.add(saveAsButton);
		saveAsButton.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		saveAsButton.addActionListener(event -> FileManager.saveAsFile(treePanel));

		/////////////////////////////////
		// Undo Button
		/////////////////////////////////

		JMenuItem undoButton = new JMenuItem("Undo");

		editMenu.add(undoButton);
		undoButton.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		undoButton.addActionListener(event -> treePanel.undoState());

		///////////////////////////////////
		// Redo Button
		///////////////////////////////////

		JMenuItem redoButton = new JMenuItem("Redo");

		editMenu.add(redoButton);
		redoButton.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
		redoButton.addActionListener(event -> treePanel.redoState());

		///////////////////////////////////
		// Open Button
		///////////////////////////////////

		JMenuItem loadButton = new JMenuItem("Open");

		fileMenu.add(loadButton);
		loadButton.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
		loadButton.addActionListener(event -> {
			try {
				FileManager.loadFromFile(treePanel);
				treePanel.moveComponents();
			}
			catch(NoneResult r) {}
			catch(FileFormatException ex) {
				showError("There's a problem with the format of that file: " + ex.getMessage());
			}
			catch(IOException ex) {
				showError("Failed to open the file: " + ex.getMessage());
			}
			catch(ParserConfigurationException | SAXException ex) {
				showError("Failed to parse the file: " + ex.getMessage());
			}
		});
		
		/////////////////////////////////////
		// Split Button
		/////////////////////////////////////
		
		JMenuItem splitButton = new JMenuItem("Split on Selected Line");
		treeMenu.add(splitButton);
		splitButton.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
		splitButton.addActionListener(event -> {
			try {
				treePanel.split();
			}
			catch(UserError userError) {
				showError(userError.getMessage());
			}
		});
		
		/////////////////////////////////////
		// Tick Button
		/////////////////////////////////////
		
		JMenuItem tickButton = new JMenuItem("Mark Decomposition");
		treeMenu.add(tickButton);
		tickButton.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_DOWN_MASK));
		tickButton.addActionListener(event -> {
			try {
				treePanel.mark();
			}
			catch(UserError er) {
				showError(er.getMessage());
			}
		});
		
		////////////////////////////////////
		// Zoom In Button
		////////////////////////////////////
		
		JMenuItem zoomInButton = new JMenuItem("Zoom in");
		viewMenu.add(zoomInButton);
		zoomInButton.setAccelerator(KeyStroke.getKeyStroke('=', InputEvent.CTRL_DOWN_MASK));
		zoomInButton.addActionListener(event -> treePanel.zoomIn());
		
		//////////////////////////////////////
		// Zoom Out Button
		//////////////////////////////////////
		
		JMenuItem zoomOutButton = new JMenuItem("Zoom out");
		viewMenu.add(zoomOutButton);
		zoomOutButton.setAccelerator(KeyStroke.getKeyStroke('-', InputEvent.CTRL_DOWN_MASK));
		zoomOutButton.addActionListener(event -> treePanel.zoomOut());
		
		//////////////////////////////////////
		// Line After Button
		//////////////////////////////////////

		JMenuItem addLineAfterButton = new JMenuItem("Add Line After");

		treeMenu.add(addLineAfterButton);
		addLineAfterButton.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
		addLineAfterButton.addActionListener(event -> {
			try {
				treePanel.addLineAfter();
			}
			catch(UserError er) {
				showError(er.getMessage());
			}
		});
		
		/////////////////////////////////////
		// Line Before Button
		/////////////////////////////////////

		JMenuItem addLineBeforeButton = new JMenuItem("Add Line Before");

		treeMenu.add(addLineBeforeButton);
		addLineBeforeButton.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
		addLineBeforeButton.addActionListener(event -> {
			try {
				treePanel.addLineBefore();
			}
			catch(UserError er) {
				showError(er.getMessage());
			}
		});
		
		/////////////////////////////////////
		// Delete Button
		/////////////////////////////////////

		JMenuItem deleteButton = new JMenuItem("Delete Selected Line");

		treeMenu.add(deleteButton);
		deleteButton.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
		deleteButton.addActionListener(event -> treePanel.deleteCurrentLine());
		
		//////////////////////////////////////
		// Delete Branch Button
		//////////////////////////////////////

		JMenuItem deleteBranchButton = new JMenuItem("Delete Current Branch");

		treeMenu.add(deleteBranchButton);
		deleteBranchButton.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		deleteBranchButton.addActionListener(event -> {
			int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to delete the current branch?");
			if (dialogResult == JOptionPane.YES_OPTION) {
				try {
					treePanel.deleteCurrentBranch();
				}
				catch(UserError er) {
					showError(er.getMessage());
				}
			}
				
		});
		
		/////////////////////////////////////
		// Premise Button
		/////////////////////////////////////

		JMenuItem premiseButton = new JMenuItem("Add Premise");

		treeMenu.add(premiseButton);
		premiseButton.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
		premiseButton.addActionListener(event -> treePanel.addPremise());
		
		/////////////////////////////////////
		// Symbols Button
		/////////////////////////////////////

		JMenuItem symbolsButton = new JMenuItem("Symbols");
		symbolsButton.addActionListener(event -> showMessage(

				"\u2228 : |\n" +
				"\u2227 : &\n" +
				"\u2192 : $\n" +
				"\u2194 : %\n" +
				"\u00AC : ~,!\n" +
				"\u2200 : @\n" +
				"\u2203 : /",

				"Symbols"));
		helpMenu.add(symbolsButton);
		
		//////////////////////////
		// About Button
		//////////////////////////

		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.addActionListener(event -> showMessage(
				String.format(

					"TFTrees : Copyright Aaron Perl 2016\n"+
					"Version %s\n"+
					"Repository : https://github.com/Bram-Hub/TFTrees"
					,
				VERSION),
				"About TFTrees"));

		helpMenu.add(aboutButton);
		
		//////////////////////////
		// Usage Button
		//////////////////////////

		JMenuItem usageButton = new JMenuItem("Usage");
		usageButton.addActionListener(event ->
				JOptionPane.showMessageDialog(null,
				new JLabel(String.format(

						"<html>" +
							"<body>" +
								"<p style=\"width: 500px;\">" +
									"How to Use Truth Trees %s <br><br>" +
									"You can Select a cell by left-clicking it, and it will turn green.<br><br>"+
									"Symbols to be used can be found under Help. Symbols will show up in their proper"+
									"logical form once you click outside the cell. At that time, the cell will also"+
									"resize if needed to show the whole statement.<br><br>"+
									"To add further premises, use CTRL-P, or use the menu item under Tree.<br><br>"+
									"For the application of the rules, there are the following options:<br>"+
								"<ul style=\"padding-left:20px; width: 500px;\">"+
									"<li>Add line."+
										"This is for the decomposition of statements that do not branch,"+
										"e.g. ~~P. Fill in the resulting statement in the newly created cell, and then"+
										"Select the cell where the statement came from (so it is green), and then"+
										"right-click on the statement(s) that were the result of decomposing the"+
										"selected statement (they will turn light blue)"+
									"<li>Add branch."+
										"You only add one branch at a time. The program does not assume that branching"+
										"will also result in exactly two branches, so that it can handle general"+
										"disjunctions with more than 2 disjuncts (each becomes its own branch). So,"+
										"create as many branches as needed, and again fill in the appropriate statements"+
										"in the cells. Then Select the statement that lead to the branching, and right-click"+
										"the resulting statements … and ALSO right-click the branch structure itself."+
									"<li>Terminate."+
										"This is to indicate that a branch is closed (X)"+
										"or finished and open (O). Right-click to switch"+
										"between X and O. If you select X, you will need"+
										"to say where the X comes from by rightclicking"+
										"those cells (which will turn blue)"
						, VERSION)),
				"About TFTrees",
				JOptionPane.PLAIN_MESSAGE)
		);

		helpMenu.add(usageButton);
		
		////////////////////////
		// Frame
		////////////////////////
		
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				close();
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				treePanel.moveComponents();
			}
		});

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		treePanel.moveComponents();

		instances++;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			showError("Error setting system look and feel: " + e.getMessage());
			System.exit(1);
		}

		createNewInstance();
	}

}
