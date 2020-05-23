package perl.aaron.TruthTrees.logic.negation.fo;

import java.util.List;
import java.util.Map;

import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.fo.A_Quantifier;
import perl.aaron.TruthTrees.logic.fo.Constant;
import perl.aaron.TruthTrees.logic.fo.Variable;
import perl.aaron.TruthTrees.logic.negation.I_Negation;
import perl.aaron.TruthTrees.util.UserError;

public abstract class A_NegQuantifier extends A_Quantifier implements I_Negation {
	
	private final List<I_Statement> decomposition;
	
	public A_NegQuantifier(
			String typeName,
			String symbol,
			Variable v,
			I_Statement s,
			Map<Constant,List<I_Statement>> statementsWithConstant,
			List<I_Statement> decomposition) {
		super(typeName, symbol, v, s, statementsWithConstant);
		assert decomposition != null;
		this.decomposition = decomposition;
	}

	@Override
	public String innerSymStringParen() {
		return super.symStringParen(); // TODO: this will break if Quantifier becomes interface
	} // TODO: ensure no class overrides symString or paren equivalent
	
	@Override
	public String symString() {
		return I_Negation.super.symString();
	}

	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		verifySerialDecomposition(branches, decomposition);
	}

}
