package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.BranchLine;

public class AtomicStatement extends Statement implements Composable {
	private String _symbol;
	/**
	 * Creates an atomic statement with a given symbol
	 * @param symbol The character representing the statement
	 */
	public AtomicStatement(String symbol)
	{
		_symbol = symbol;
	}
	/**
	 * Returns the statement's symbol
	 * @return The character representing the statement
	 */
	public String getSymbol()
	{
		return _symbol;
	}
	public String toString()
	{
		return _symbol;
	}
	public String toStringParen()
	{
		return _symbol;
	}
	public boolean equals(Object other)
	{
		if (!(other instanceof AtomicStatement))
			return false;
		AtomicStatement otherAS = (AtomicStatement) other;
		return (otherAS.getSymbol().equals(_symbol));
	}
	public boolean equals(Statement other) {
		if (!(other instanceof AtomicStatement))
			return false;
		return ((AtomicStatement)other).getSymbol().equals(_symbol);
	}
	
	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) {
		
		
		
		
		return true;
		
	}
	
	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}
	@Override
	public Set<String> getConstants() {
		return Collections.emptySet();
	}
	@Override
	public Binding determineBinding(Statement unbound) {
		if (unbound.equals(this)) return Binding.EMPTY_BINDING;
		else return null;
	}
	@Override
	public String verifyComposition(Set<BranchLine> selectedBranchLines) {
		//System.out.println("Wtf");
		
		if (selectedBranchLines.size() == 2) {
			
			ArrayList<Statement> selectedStatements = new ArrayList<Statement>();
			Iterator<BranchLine> itr = selectedBranchLines.iterator();
			while (itr.hasNext()) {
				selectedStatements.add(itr.next().getStatement());
			}
			
			for (int i = 0; i < selectedStatements.size(); i++) {
				
				//System.out.println("no conditionals found yet...");
				
				
				if (selectedStatements.get(i) instanceof Conditional) {
					//System.out.println("conditionals found...");
					List<Statement> operands = ((Conditional)selectedStatements.get(i)).getOperands();
					
					// MODUS PONENS
					if (operands.get(1).toString().equals(toString()) && operands.get(0).toString().equals(selectedStatements.get(i%1).toString())) {
						//System.out.println("we good");
						return "composable";
					}
					
					// MODUS TOLLENS
					//Negation negFirstOperand = new Negation (operands.get(0));
					Negation negSecondOperand = new Negation (operands.get(1));
					System.out.println(operands.get(0).toString() + " " + toString());
					System.out.println(negSecondOperand.toString() + " " + selectedStatements.get(i%1).toString());
					
					
					if (operands.get(0).toString().equals(toString()) && negSecondOperand.toString().equals(selectedStatements.get(i%1).toString())) {
						//System.out.println("we good negations");
						return "composable";
					}
					
				}
				
				
				
			}
			
			return "Statement \"" + toString() + "\" cannot be composed like this";
			
		}
		
		
		if (selectedBranchLines.size() != 0) {
			return "Statement \"" + toString() + "\" cannot be composed like this";
			
		}
	
		
		
		
		return "X";

	}
	

	
}
