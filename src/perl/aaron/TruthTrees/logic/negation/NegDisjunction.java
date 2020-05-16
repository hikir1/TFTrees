package perl.aaron.TruthTrees.logic.negation;

import java.util.List;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.AComplexStatement;
import perl.aaron.TruthTrees.logic.Disjunction;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;

public class NegDisjunction extends AComplexStatement implements ComplexNegation, SerialDecomposable {
	public static final String TYPE_NAME = "Negated Disjunction";
	
	private final List<Statement> decomposition;
	
	public NegDisjunction(Statement a, Statement b) {
		this(List.of(a, b));
	}
	
	public NegDisjunction(List<Statement> statements) {
		super(TYPE_NAME, Disjunction.SYMBOL, List.copyOf(statements));
		assert statements != null;
		decomposition = statements.stream().map(Statement::negated).collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<Statement> getModelDecomposition() {
		return decomposition;
	}
	
	@Override
	public Statement getInner() {
		return new Disjunction(statements);
	}

}
