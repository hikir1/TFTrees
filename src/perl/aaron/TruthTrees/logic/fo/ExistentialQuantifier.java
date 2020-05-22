package perl.aaron.TruthTrees.logic.fo;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.negation.fo.NegExistentialQuantifier;
import perl.aaron.TruthTrees.util.NoneResult;
import perl.aaron.TruthTrees.util.UserError;

public class ExistentialQuantifier extends AQuantifier {
	public static final String TYPE_NAME = "Existential Quantifier";
	public static final String SYMBOL = "\u2203";

	public ExistentialQuantifier(Variable v, Statement s, Map<Constant,List<Statement>> statementsWithConstant) {
		super(TYPE_NAME, SYMBOL, v, s, statementsWithConstant);
	}
	
	@Override
	public NegExistentialQuantifier negated() {
		return new NegExistentialQuantifier(variable, statement, statementsWithConstant);
	}
	
	@Override
	public void subVerifyDecomposition(List<List<Statement>> branches) throws UserError {
		assert branches != null;
		if (branches.size() != 1)
			throw new UserError("Expected one branch, but there are " + branches.size() + ".");
		if (branches.get(0).size() != 1)
			throw new UserError("Expected one decomposed statement, but there are " + branches.get(0).size() + ".");
		Statement decomposed = branches.get(0).get(0);
		try {
			Constant bounded = testEqualsWithConstant(decomposed);
			if (statementsWithConstant.get(bounded).size() != 1)
				throw new UserError(bounded + " already used. Use a fresh constant instead.");
		}
		catch(NoneResult r) {/* skip: variable does not occur in statement */}
	}

}
