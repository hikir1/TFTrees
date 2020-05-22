package perl.aaron.TruthTrees.logic.fo;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.AStatement;
import perl.aaron.TruthTrees.logic.ComplexSymbolString;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.SymbolString;
import perl.aaron.TruthTrees.util.MutOption;
import perl.aaron.TruthTrees.util.NoneResult;
import perl.aaron.TruthTrees.util.UserError;

public abstract class AQuantifier extends AStatement implements Quantifier {

	protected final Variable variable;
	protected final Statement statement;
	protected final Map<Constant,List<Statement>> statementsWithConstant;
	
	public AQuantifier(
			final String typeName, 
			final String symbol, 
			final Variable v,
			final Statement s,
			final Map<Constant,List<Statement>> statementsWithConstant)
	{
		super(typeName, symbol);
		assert v != null;
		assert s != null;
		assert statementsWithConstant != null;
		variable = v;
		statement = s;
		// must be same set as given so that updates are detected
		this.statementsWithConstant = Collections.unmodifiableMap(statementsWithConstant);
	}
	
	@Override
	public Variable getVariable() {
		return variable;
	}
	
	@Override
	public Statement getStatement() {
		return statement;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		var quant = (AQuantifier) other;
		return variable.equals(quant.variable) && statement.equals(quant.statement);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ variable.hashCode() ^ statement.hashCode();
	}
	
	protected Constant testEqualsWithConstant(final Statement s) throws UserError, NoneResult {
		assert s != null;
		final Deque<SymbolString> thisStack = new LinkedList<>(), otherStack = new LinkedList<>();
		thisStack.push(this);
		otherStack.push(s);
		MutOption<Constant> boundedConstant = new MutOption<>();
		SymbolString thisSymString, otherSymString;
		while (!thisStack.isEmpty()) {
			assert thisStack.size() == otherStack.size() : "These stacks should always be the same size here.";
			thisSymString = thisStack.pop();
			otherSymString = otherStack.pop();
			if (thisSymString.equals(variable)) {
				if (!(otherSymString instanceof Constant))
					throw new UserError("Illegal replacement for " + variable + ": " + otherSymString);
				try {
					if (!boundedConstant.unwrap().equals(otherSymString))
						throw new UserError("Incompatible replacments for " + variable + ": " 
								+ boundedConstant.unwrap() + " and " + otherSymString);
				}
				catch (NoneResult r) {
					boundedConstant.set((Constant) otherSymString);
				}
			}
			// variable shadowing
			else if (thisSymString instanceof AQuantifier) {
				if (((AQuantifier)thisSymString).variable.equals(variable)
						&& !thisSymString.equals(otherSymString))
					throw new UserError(thisSymString + " does not match " + otherSymString);
			}
			else if (!thisSymString.getClass().equals(otherSymString.getClass())
					|| !thisSymString.getSymbol().equals(otherSymString.getSymbol()))
				throw new UserError(thisSymString + " does not match " + otherSymString);
			else if (thisSymString instanceof ComplexSymbolString) {
				assert otherSymString instanceof ComplexSymbolString : "These should both always be ComplexSymbolStrings at this point";
				((ComplexSymbolString)thisSymString).getSymbolStrings().forEach(thisStack::push);
				((ComplexSymbolString)otherSymString).getSymbolStrings().forEach(otherStack::push);
				if (thisStack.size() != otherStack.size())
					throw new UserError(thisSymString + " does not match " + otherSymString);
			}
		}
		assert otherStack.isEmpty() : "If one stack is empty, the other should be too";
		// throws nonresult if no constant needed (empty binding)
		return boundedConstant.unwrap();
	}
}
