package perl.aaron.TruthTrees.logic.fo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.negation.fo.NegUniversalQuantifier;
import perl.aaron.TruthTrees.util.NoneResult;
import perl.aaron.TruthTrees.util.UserError;

public class UniversalQuantifier extends A_Quantifier {
	
	public static final String TYPE_NAME = "Universal Quantifier";
	public static final String SYMBOL = "\u2200";

	public UniversalQuantifier(Variable v, I_Statement s, Map<Constant, List<I_Statement>> statementsWithConstant) {
		super(TYPE_NAME, SYMBOL, v, s, statementsWithConstant);
	}

	@Override
	public NegUniversalQuantifier negated() {
		return new NegUniversalQuantifier(variable, statement, statementsWithConstant);
	}
	
	@Override
	public void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		assert branches != null;
		if (branches.size() != 1)
			throw new UserError("Expected one branch, but there are " + branches.size() + ".");
		final Set<Constant> branchConstants = new HashSet<>(statementsWithConstant.keySet());
		// its ok if new constants are introduced, but all old constants must be used
		for (final I_Statement statement: branches.get(0)) {
			try {
				final Constant bounded = testEqualsWithConstant(statement);
				branchConstants.remove(bounded);
			}
			catch (NoneResult r) { /* skip: variable does not occur in statement */ }
		}
		if (!branchConstants.isEmpty())
			throw new UserError("There must be one decomposed statement per constant.\n"
					+ "Unused constants: " + branchConstants);
	}
}
