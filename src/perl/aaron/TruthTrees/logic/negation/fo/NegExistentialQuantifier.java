package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.Constant;
import perl.aaron.TruthTrees.logic.fo.ExistentialQuantifier;
import perl.aaron.TruthTrees.logic.fo.UniversalQuantifier;
import perl.aaron.TruthTrees.logic.fo.Variable;

public class NegExistentialQuantifier extends ANegQuantifier {



	public NegExistentialQuantifier(Variable v, Statement s, Map<Constant,List<Statement>> statementsWithConstant) {
		super(
				ExistentialQuantifier.TYPE_NAME, 
				ExistentialQuantifier.SYMBOL, 
				v, s, statementsWithConstant,
				List.of(new UniversalQuantifier(v, s.negated(), statementsWithConstant)));
	}

	@Override
	public Statement getInner() {
		return new ExistentialQuantifier(variable, statement, statementsWithConstant);
	}

}
