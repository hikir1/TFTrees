package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.fo.Constant;
import perl.aaron.TruthTrees.logic.fo.AQuantifier;
import perl.aaron.TruthTrees.logic.fo.Variable;
import perl.aaron.TruthTrees.logic.negation.Negation;

public abstract class ANegQuantifier extends AQuantifier implements Negation, SerialDecomposable {
	
	private final List<Statement> decomposition;
	
	public ANegQuantifier(
			String typeName,
			String symbol,
			Variable v,
			Statement s,
			Map<Constant,List<Statement>> statementsWithConstant,
			List<Statement> decomposition) {
		super(typeName, symbol, v, s, statementsWithConstant);
		assert decomposition != null;
		this.decomposition = decomposition;
	}

	@Override
	public String innerSymStringParen() {
		return super.symStringParen(); // TODO: this will break if Quantifier becomes interface
	} // TODO: ensure no class overrides symString or paren equivalent
	
	@Override
	public final List<Statement> getModelDecomposition() {
		return decomposition;
	}

}
