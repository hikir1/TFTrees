package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.fo.Constant;
import perl.aaron.TruthTrees.logic.fo.ExistentialQuantifier;
import perl.aaron.TruthTrees.logic.fo.UniversalQuantifier;
import perl.aaron.TruthTrees.logic.fo.Variable;

public class NegUniversalQuantifier extends A_NegQuantifier {

	public NegUniversalQuantifier(Variable v, I_Statement s, Map<Constant, List<I_Statement>> statementsWithConstant) {
		super(
				UniversalQuantifier.TYPE_NAME, 
				UniversalQuantifier.SYMBOL,
				v, s, statementsWithConstant,
				List.of(new ExistentialQuantifier(v, s, statementsWithConstant)));
	}

	@Override
	public I_Statement getInner() {
		return new UniversalQuantifier(variable, statement, statementsWithConstant);
	}

}