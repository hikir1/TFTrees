package perl.aaron.TruthTrees;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import perl.aaron.TruthTrees.graphics.TreePanel;

public class TruthTrees {

	private static int instances = 0;

	public static final String version = "1.3";
	public static final String errorLogDir = "logs/";
	public static final String errorFrameName = "Truth Trees Error";
	public static final String errorMessageErrorLogFile = "Error writing to log file";
	public static final String errorMessageSystemLookAndFeel = "Error setting system look and feel";

	public static void close() {
		instances--;
		if (instances == 0) {
			System.exit(0);
		}
	}
	public static void createNewInstance() {
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
		System.out.println(frame.getContentPane().getComponent(0) == treePanel);
		frame.setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(treeMenu);
		menuBar.add(helpMenu);

		// frame.getContentPane().add(zoompanel,BorderLayout.EAST);
		
		JMenuItem checkButton = new JMenuItem("Check Tree");
		
		treeMenu.add(checkButton);
		checkButton.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_MASK));
		checkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ret = ((TreePanel)frame.getContentPane().getComponent(0)).check();
				if (ret == null)
					JOptionPane.showMessageDialog(null, "The tree is correct!");				
				else
					JOptionPane.showMessageDialog(null, "The tree is invalid!\n"+ret);
			}
		});

    JMenuItem checkFolderButton = new JMenuItem("Check Tree Folder");

    treeMenu.add(checkFolderButton);
    checkFolderButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ArrayList<File> files = FileManager.listFolderFilesStart((TreePanel)frame.getContentPane().getComponent(0));
        if (files != null)
        {
          for (int i = 0; i < files.size(); i ++)
          {
            System.out.println(files.get(i));
            TreePanel newPanel = FileManager.loadFromFile(files.get(i));
            String ret = newPanel.check();
            if (ret == null)
              JOptionPane.showMessageDialog(null, "The tree in file "+files.get(i).getPath()+" is correct!");	
            else
              JOptionPane.showMessageDialog(null, "The tree in file "+files.get(i).getPath()+" is invalid!\n"+ret);
          }
        }
      }
    });

		JMenuItem checkLineButton = new JMenuItem("Verify Current Line");
		
		treeMenu.add(checkLineButton);
		checkLineButton.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_MASK));
		checkLineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ret = ((TreePanel)frame.getContentPane().getComponent(0)).checkSelectedLine();
				if (ret == null)
					JOptionPane.showMessageDialog(null, "This line is correctly decomposed!");				
				else
					JOptionPane.showMessageDialog(null, ret);
			}
		});

		treeMenu.addSeparator();
		
		JMenuItem newButton = new JMenuItem("New");
		
		fileMenu.add(newButton);
		newButton.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));
		newButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileManager.newFile();
				
			}
		});

		JMenuItem saveButton = new JMenuItem("Save");
		
		fileMenu.add(saveButton);
		saveButton.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileManager.saveFile((TreePanel)frame.getContentPane().getComponent(0));
				
			}
		});
		
		JMenuItem saveAsButton = new JMenuItem("Save As");
		
		fileMenu.add(saveAsButton);
		saveAsButton.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		saveAsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileManager.saveAsFile((TreePanel)frame.getContentPane().getComponent(0));
				
			}
		});

		JMenuItem undoButton = new JMenuItem("Undo");
		
		editMenu.add(undoButton);
		undoButton.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_MASK));
		undoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).undoState();
			}
		});
		
		JMenuItem redoButton = new JMenuItem("Redo");
		
		editMenu.add(redoButton);
		redoButton.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_MASK));
		redoButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).redoState();
			}
		});
		
		JMenuItem loadButton = new JMenuItem("Open");
		
		fileMenu.add(loadButton);
		loadButton.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePanel newPanel = FileManager.loadFile((TreePanel)frame.getContentPane().getComponent(0));
				if (newPanel != null)
				{
					frame.remove(frame.getContentPane().getComponent(0));
					frame.getContentPane().add(newPanel, BorderLayout.CENTER);
					frame.pack();
					newPanel.moveComponents();
					System.out.println(frame.getContentPane().getComponent(0) == newPanel);
				}
			}
		});
		JMenuItem splitButton = new JMenuItem("Split Selected Line");
		treeMenu.add(splitButton);
		splitButton.setAccelerator(KeyStroke.getKeyStroke('W', InputEvent.CTRL_MASK));
		splitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ret = ((TreePanel)frame.getContentPane().getComponent(0)).split();
				if (ret != null)
					JOptionPane.showMessageDialog(null, "ret");				
				// else
				// 	JOptionPane.showMessageDialog(null, ret);
			}
		});
		JMenuItem tickButton = new JMenuItem("Mark Decomposition");
		treeMenu.add(tickButton);
		tickButton.setAccelerator(KeyStroke.getKeyStroke('M', InputEvent.CTRL_MASK));
		tickButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ret = ((TreePanel)frame.getContentPane().getComponent(0)).mark();
				if (ret != null)
					JOptionPane.showMessageDialog(null, "ret");				
				// else
				// 	JOptionPane.showMessageDialog(null, ret);
			}
		});
		JMenuItem zoomInButton = new JMenuItem("Zoom in");
		viewMenu.add(zoomInButton);
		zoomInButton.setAccelerator(KeyStroke.getKeyStroke('=', InputEvent.CTRL_MASK));
		zoomInButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).zoomIn();
			}
		});
		JMenuItem zoomOutButton = new JMenuItem("Zoom out");
		viewMenu.add(zoomOutButton);
		zoomOutButton.setAccelerator(KeyStroke.getKeyStroke('-', InputEvent.CTRL_MASK));
		zoomOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).zoomOut();
			}
    });

		JMenuItem addLineAfterButton = new JMenuItem("Add Line After");
		
		treeMenu.add(addLineAfterButton);
		addLineAfterButton.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK));
		addLineAfterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).addLineAfter();
			}
    });

		JMenuItem addLineBeforeButton = new JMenuItem("Add Line Before");
		
		treeMenu.add(addLineBeforeButton);
		addLineBeforeButton.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_MASK));
		addLineBeforeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).addLineBefore();
			}
    });
		
		JMenuItem deleteButton = new JMenuItem("Delete Selected Line");
		
		treeMenu.add(deleteButton);
		deleteButton.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK));
//		deleteButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).deleteCurrentLine();
			}
		});
		
		JMenuItem deleteBranchButton = new JMenuItem("Delete Current Branch");
		
		treeMenu.add(deleteBranchButton);
		deleteBranchButton.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		deleteBranchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to delete the current branch?");
				if (dialogResult == JOptionPane.YES_OPTION)
					((TreePanel)frame.getContentPane().getComponent(0)).deleteCurrentBranch();
			}
		});
		
		JMenuItem premiseButton = new JMenuItem("Add Premise");
		
		treeMenu.add(premiseButton);
		premiseButton.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK));
//		deleteButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		premiseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((TreePanel)frame.getContentPane().getComponent(0)).addPremise();
			}
		});
		
		JMenuItem symbolsButton = new JMenuItem("Symbols");
		symbolsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
				new JLabel("<html><body>" +
				  "\u2228 : |<br>" +
			      "\u2227 : &<br>" +
			      "\u2192 : $<br>" +
			      "\u2194 : %<br>" +
			      "\u00AC : ~,!<br>" +
			      "\u2200 : @<br>" +
			      "\u2203 : /" +
			    "</html></body>", JLabel.CENTER),
				"Logical Symbols",
				JOptionPane.PLAIN_MESSAGE);
			}
		});
		helpMenu.add(symbolsButton);
		
		JMenuItem aboutButton = new JMenuItem("About");
		aboutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
				  new JLabel("<html><body>" +
				    "TFTrees : Copyright Aaron Perl 2016<br>"+
				    "Version " + version + "<br>" +
				    "Repository : https://github.com/AaronPerl/TFTrees" +
				  "</body></html>"),
				  "About TFTrees",
				  JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		helpMenu.add(aboutButton);

		JMenuItem usageButton = new JMenuItem("Usage");
		usageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
				  new JLabel("<html><body><p style=\"width: 500px;\">"+
            "How to Use Truth Trees " + version + "<br><br>"+
            "You can Select a cell by left-clicking it, and it will turn green.<br><br>"+
            "Symbols to be used can be found under Help. Symbols will show up in their proper logical form once you click outside the cell. At that time, the cell will also resize if needed to show the whole statement.<br><br>" +
            "To add further premises, use CTRL-P, or use the menu item under Tree.<br><br>"+
            "For the application of the rules, there are the following options:<br></p>"+
            "<ul style=\"padding-left:20px; width: 500px;\">"+
            "<li>Add line. This is for the decomposition of statements that do not branch, e.g. ~~P. Fill in the resulting statement in the newly created cell, and then Select the cell where the statement came from (so it is green), and then right-click on the statement(s) that were the result of decomposing the selected statement (they will turn light blue)</li>"+
            "<li>Add branch. You only add one branch at a time. The program does not assume that branching will also result in exactly two branches, so that it can handle general disjunctions with more than 2 disjuncts (each becomes its own branch). So, create as many branches as needed, and again fill in the appropriate statements in the cells. Then Select the statement that lead to the branching, and right-click the resulting statements … and ALSO right-click the branch structure itself.</li>"+
            "<li>Terminate. This is to indicate that a branch is closed (X) or finished and open (O). Right-click to switch between X and O. If you select X, you will need to say where the X comes from by rightclicking those cells (which will turn blue)</li></ul>"+
            "<p style=\"width: 500px;\">There is also an option in the Tree menu to check all TruthTree files in a folder. To use, place all files into a folder labeled \"grading\" in the same location as the application. Subfolders are allowed. Non TruthTree files will be ignored.</p></body></html>"),
				  "About TFTrees",
				  JOptionPane.PLAIN_MESSAGE);
			}
		});

    helpMenu.add(usageButton);
		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				System.out.println(instances);
					close();
			}
	});

	frame.addComponentListener(new ComponentAdapter() {
    public void componentResized(ComponentEvent componentEvent) {
        treePanel.moveComponents();
    }
	});
		// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		treePanel.moveComponents();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		instances++;
}

	public static void popupException(Exception e, String errorMessage)
	{
		// get error string
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String errorString = sw.toString().trim();
		
		// trim error to 5 lines to prevent large popup dialogs
		int newlinePos = errorString.indexOf("\n");
		int newlines = 1;
		while (newlinePos != -1 && newlines < 5)
		{
			newlinePos = errorString.indexOf("\n",newlinePos + 1);
			newlines++;
		}
		
		// get number of error lines trimmed
		int newlinesRemaining = 0;
		int newlinePosNext = newlinePos;
		while (newlinePosNext != -1)
		{
			newlinesRemaining++;
			newlinePosNext = errorString.indexOf("\n",newlinePosNext + 1);
		}
		
		// string to append to error displaying number of lines trimmed
		String extraError = "";
		if (newlinesRemaining > 1)
			extraError = "\nand " + Integer.toString(newlinesRemaining) + " more...";
		
		JOptionPane.showMessageDialog(null, errorMessage+"\n"+errorString.substring(0,newlinePos)+extraError,
											TruthTrees.errorFrameName,
											JOptionPane.ERROR_MESSAGE);
	}
	
	public static void logException(Exception e, String errorMessage)
	{
		// file name based on timestamp
		DateFormat format = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
		String dateString = format.format(new Date());
		File errorFile = new File(errorLogDir + dateString + ".txt");
		try
		{
			PrintWriter errorpw = new PrintWriter(errorFile);
			errorpw.println(errorMessage);
			e.printStackTrace(errorpw);
			errorpw.close();
		}
		catch (FileNotFoundException fileError)
		{
			popupException(fileError, errorMessageErrorLogFile);
		}
	}
	
	public static void logExceptionPopup(Exception e, String errorMessage)
	{
		logException(e, errorMessage);
		popupException(e, errorMessage);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			logExceptionPopup(e, errorMessageSystemLookAndFeel);
			System.exit(1);
		}

		createNewInstance();

	}

}
