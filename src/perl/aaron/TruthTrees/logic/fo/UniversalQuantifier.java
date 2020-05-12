package perl.aaron.TruthTrees.logic.fo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.util.UserError;

public class UniversalQuantifier extends Quantifier {

	public UniversalQuantifier(Variable var, Statement statement) {
		super(var, statement);
	}

	@Override
	public String toString() {
		return "\u2200" + var.toString() + " " + statement.toStringParen();
	}
	
	@Override
	public String toStringParen() {
		return toString();
	}

	@Override
	public void verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) throws UserError {
		if (branches.size() != 1) // There should be only 1 branch
			throw new UserError("Quantifier decomposition should not produce branches");
		Set<String> instantiatedConstants = new LinkedHashSet<String>();
		try {
			for (Statement s : branches.get(0))
				instantiatedConstants.add(s.determineBinding(statement).getConstant().toString());
		}
		catch(UserError e) {
			throw new UserError("Invalid binding for " + this.var + ":\n" + e.getMessage());
		}
		instantiatedConstants.removeAll(constants);
		if (!instantiatedConstants.isEmpty())
			throw new UserError("Not all constants used. Constants remaining: " + instantiatedConstants);
	}

}
