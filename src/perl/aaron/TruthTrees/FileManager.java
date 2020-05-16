package perl.aaron.TruthTrees;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import perl.aaron.TruthTrees.graphics.TreePanel;
import perl.aaron.TruthTrees.logic.AStatement;
import perl.aaron.TruthTrees.util.FileFormatException;
import perl.aaron.TruthTrees.util.NoneResult;

public class FileManager {
	private static final String EXTENSION = "tft";
	private static File SAVEDIR;


	public static ArrayList<File> listFolderFilesStart(TreePanel parent)
	{
		// codes needs to be updated to allow choosing a directory with JFileChooser
		// look at http://www.rgagnon.com/javadetails/java-0370.html
		File dir = new File("grading");
		return listFolderFiles(dir);
	}

	public static ArrayList<File> listFolderFiles(File dir)
	{
		File[] filesAndFolders = dir.listFiles();
		ArrayList<File> files = new ArrayList<File>();

		if (filesAndFolders != null)
		{
			for (int i = 0; i < filesAndFolders.length; i++)
			{
				if (filesAndFolders[i].isDirectory())
					files.addAll(listFolderFiles(filesAndFolders[i]));
				else
					files.add(filesAndFolders[i]);
			}
		}
		return files;
	}

	public static void loadFromFile(TreePanel parent)
			throws NoneResult,
				FileFormatException,
				ParserConfigurationException,
				SAXException,
				IOException
	{
		final JFileChooser fileChooser = new JFileChooser(SAVEDIR);
		if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			SAVEDIR = new File(file.getPath());
			System.out.println(file.getName());
			loadFromFile(parent, file);
		}
		throw new NoneResult();
	}

	public static void saveFile(TreePanel parent)
	{
		if (SAVEDIR == null)
			saveAsFile(parent);
		else
			saveToFile(parent.getRootBranch(), SAVEDIR, parent);
	}

	public static void saveAsFile(TreePanel parent)
	{
		final JFileChooser fileChooser = new JFileChooser(SAVEDIR);
		FileNameExtensionFilter tftFilter = new FileNameExtensionFilter(
				EXTENSION + " files(*." + EXTENSION + ")",
				EXTENSION);

		fileChooser.addChoosableFileFilter(tftFilter);
		fileChooser.setFileFilter(tftFilter);

		if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			if (fileChooser.getFileFilter() == tftFilter && !file.getName().endsWith("." + EXTENSION))
				file = new File(file.getAbsolutePath() + "." + EXTENSION);
			SAVEDIR = new File(file.getPath());
			System.out.println(file.getName());
			saveToFile(parent.getRootBranch(), file, parent);
		}
	}

	private static void processBranchLine(Branch curBranch, Node node, TreePanel panel,
			ArrayList<Set<Integer>> lineDecompositions, ArrayList<Set<Integer>> branchDecompositions,
			ArrayList<BranchLine> lines, ArrayList<Branch> branches)
	{
		var curLineDecompositions = new LinkedHashSet<Integer>();
		var curBranchDecompositions = new LinkedHashSet<Integer>();
		Element curElement = (Element) node;
		BranchLine newLine;
		if (node.getNodeName().equals("Terminator"))
		{
			String isClose = curElement.getAttribute("close");
			if (isClose.isBlank() || isClose.equals("true"))
			{
				newLine = panel.addTerminator(curBranch);
			}
			else
			{
				newLine = panel.addOpenTerminator(curBranch);
			}
		}
		else
		{
			String content = curElement.getAttribute("content");
			AStatement newStatement = ExpressionParser.parseExpression(content);
			newLine = panel.addStatement(curBranch, newStatement);
		}
		lines.add(newLine);
		NodeList decompositions = node.getChildNodes();
		for (int j = 0; j < decompositions.getLength(); j++)
		{
			Node curDecomp = decompositions.item(j);
			if (curDecomp.getNodeName().equals("Decomposition"))
			{
				Element decompElement = (Element) curDecomp;
				String branchIndexString = decompElement.getAttribute("branchIndex");
				if (!branchIndexString.isBlank())
				{
					System.out.println("Branch index:" + branchIndexString);
					int branchIndex = Integer.parseInt(branchIndexString);
					curBranchDecompositions.add(branchIndex);
				}
				String lineIndexString = decompElement.getAttribute("lineIndex");
				if (!lineIndexString.isBlank())
				{
					System.out.println("Line index:" + lineIndexString);
					int lineIndex = Integer.parseInt(lineIndexString);
					curLineDecompositions.add(lineIndex);
				}
			}
		}
		lineDecompositions.add(curLineDecompositions);
		branchDecompositions.add(curBranchDecompositions);
	}

	private static void processNode(Branch curBranch, Node curNode, TreePanel panel,
			ArrayList<Set<Integer>> lineDecompositions, ArrayList<Set<Integer>> branchDecompositions,
			ArrayList<BranchLine> lines, ArrayList<Branch> branches)
	{
		NodeList children = curNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node curChild = children.item(i);
			if (curChild.getNodeName().equals("Branch"))
			{
				Branch newBranch = panel.addBranch(curBranch, false);
				branches.add(newBranch);
				processNode(newBranch, curChild, panel, lineDecompositions, branchDecompositions, lines, branches);
			}
			else if (curChild.getNodeName().equals("BranchLine") || curChild.getNodeName().equals("Terminator"))
			{
				processBranchLine(curBranch, curChild, panel, lineDecompositions, branchDecompositions, lines, branches);
			}
		}
	}

	public static void loadFromFile(TreePanel treePanel, File file)
			throws FileFormatException,
				ParserConfigurationException,
				SAXException,
				IOException
	{
		treePanel.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(file);
		doc.getDocumentElement().normalize();
		Branch rootBranch = treePanel.getRootBranch();
		Branch premiseBranch = rootBranch.getRoot();
		Node rootElement = doc.getDocumentElement();
		NodeList rootList = rootElement.getChildNodes();
		ArrayList<Set<Integer>> lineDecompositions = new ArrayList<Set<Integer>>();
		ArrayList<Set<Integer>> branchDecompositions = new ArrayList<Set<Integer>>();
		ArrayList<BranchLine> lines = new ArrayList<BranchLine>();
		ArrayList<Branch> branches = new ArrayList<Branch>();
		branches.add(rootBranch);
		boolean foundRoot = false;
		for (int i = 0; i < rootList.getLength(); i++)
		{
			Node curNode = rootList.item(i);
			if (curNode.getNodeName().equals("BranchLine"))
			{
				processBranchLine(premiseBranch, curNode, treePanel, lineDecompositions, branchDecompositions, lines, branches);
			}
			else if(curNode.getNodeName().equals("Branch"))
			{
				if (foundRoot)
					throw new FileFormatException("Two root nodes");
				foundRoot = true;
				processNode(rootBranch,curNode,treePanel,
						lineDecompositions, branchDecompositions,
						lines, branches);
			}
		}
		for (int i = 0; i < lines.size(); i++)
		{
			Set<Integer> curLineIndices = lineDecompositions.get(i);
			Set<Integer> curBranchIndices = branchDecompositions.get(i);
			BranchLine curLine = lines.get(i);
			Set<BranchLine> selectedLines = curLine.getSelectedLines();
			Set<Branch> selectedBranches = curLine.getSelectedBranches();
			for (int lineIndex : curLineIndices)
			{
				BranchLine curDecomp = lines.get(lineIndex);
				selectedLines.add(curDecomp);
				if (!(curLine instanceof BranchTerminator))
					curDecomp.setDecomposedFrom(curLine);
			}
			for (int branchIndex : curBranchIndices)
			{
				Branch curDecomp = branches.get(branchIndex);
				selectedBranches.add(curDecomp);
			}
		}
		treePanel.deleteFirstPremise();
		treePanel.moveComponents();
	}

	private static void saveBranch(Branch curBranch, Document doc, Element parent,
			LinkedHashMap<Branch, Integer> branchIndexMap, LinkedHashMap<BranchLine, Integer> lineIndexMap)
	{
		for (int i = 0; i < curBranch.numLines(); i++)
		{
			BranchLine curLine = curBranch.getLine(i);
			Element curLineElement;
			if (curLine instanceof BranchTerminator)
			{
				curLineElement = doc.createElement("Terminator");
				curLineElement.setAttribute("close",
						Boolean.toString(((BranchTerminator)curLine).isClose()));
			}
			else
			{
				curLineElement = doc.createElement("BranchLine");
				curLineElement.setAttribute("content", curLine.toString());
			}
			curLineElement.setAttribute("index", Integer.toString(lineIndexMap.get(curLine)));
			for (Branch curDecomp : curLine.getSelectedBranches())
			{
				Element curDecompElement = doc.createElement("Decomposition");
				curDecompElement.setAttribute("branchIndex", Integer.toString(branchIndexMap.get(curDecomp) - 1)); // offset by 1 to skip premise branch
				curLineElement.appendChild(curDecompElement);
			}
			for (BranchLine curDecomp : curLine.getSelectedLines())
			{
				Element curDecompElement = doc.createElement("Decomposition");
				curDecompElement.setAttribute("lineIndex", Integer.toString(lineIndexMap.get(curDecomp)));
				curLineElement.appendChild(curDecompElement);
			}
			parent.appendChild(curLineElement);
		}
		for (Branch curChild : curBranch.getBranches())
		{
			Element curBranchElement = doc.createElement("Branch");
			curBranchElement.setAttribute("index", Integer.toString(branchIndexMap.get(curBranch)));
			parent.appendChild(curBranchElement);
			saveBranch(curChild, doc, curBranchElement, branchIndexMap, lineIndexMap);
		}
	}

	private static void createIndexMaps(Branch root, Map<Branch,Integer> branchIndexMap, Map<BranchLine,Integer> lineIndexMap)
	{
		branchIndexMap.put(root, branchIndexMap.size());
		for (int i = 0; i < root.numLines(); i++)
		{
			BranchLine curLine = root.getLine(i);
			lineIndexMap.put(curLine, lineIndexMap.size());
		}
		for (Branch curBranch : root.getBranches())
		{
			createIndexMaps(curBranch, branchIndexMap, lineIndexMap);
		}
	}

	private static void saveToFile(Branch root, File file, TreePanel parent)
	{
		try {
			LinkedHashMap<Branch, Integer> branchIndexMap = new LinkedHashMap<Branch, Integer>();
			LinkedHashMap<BranchLine, Integer> lineIndexMap = new LinkedHashMap<BranchLine, Integer>();
			createIndexMaps(root.getRoot(), branchIndexMap, lineIndexMap);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element rootElement = doc.createElement("Tree");
			doc.appendChild(rootElement);
			saveBranch(root.getRoot(), doc, rootElement, branchIndexMap, lineIndexMap);

			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer transformer = tfactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);


		} catch (ParserConfigurationException | TransformerException e) {
			JOptionPane.showMessageDialog(parent, "Error: Could not save file!");
		}

	}

}
