package perl.aaron.TruthTrees.logic.fo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.Disjunction;
import perl.aaron.TruthTrees.logic.SerialDecomposable;
import perl.aaron.TruthTrees.logic.Statement;
import perl.aaron.TruthTrees.logic.negation.fo.Inequality;

public class Equality extends AEquality implements SerialDecomposable {
	public static final String TYPE_NAME = "Equality";
	public static final String SYMBOL = "=";
	
	private final Set<LogicObject> uniqueArguments;
	
	public Equality(List<LogicObject> arguments, Map<LogicObject,Set<APredicate>> atomicPredicates) {
		super(TYPE_NAME, SYMBOL, arguments, atomicPredicates);
		assert arguments != null;
		uniqueArguments = new HashSet<LogicObject>(arguments);
	}
	
	@Override
	protected Equality newInstance(List<LogicObject> arguments) {
		return new Equality(arguments, atomicPredicates);
	}
	
	
	// redundancy does not effect equivalency of equations
	// ex: a = a = b is the same as a = b
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Equality))
			return false;
		return uniqueArguments.equals(((Equality)other).uniqueArguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ uniqueArguments.hashCode();
	}
	
	// TODO override replace and make exceptions for equations that already exist
	//	to prevent infinite loop
	
	@Override
	public List<Statement> getModelDecomposition() {
		Set<LogicObject> arguments = Set.copyOf(this.arguments);
		Set<APredicate> currentPredicates, modelPredicates = new HashSet<>();
		for (LogicObject equArg: arguments) {
			currentPredicates = atomicPredicates.get(equArg);
			for (APredicate predicate: currentPredicates) {
				for (List<LogicObject> argList: replaceArgs(predicate.arguments, equArg, arguments)) {
					APredicate option = predicate.newInstance(argList);
					if (option instanceof Equality)
						// Prevent infinite recursive decomposition by skipping Equality if already exists
						// option.arguments could be empty if tautology
						if (((Equality)option).uniqueArguments.size() == 1 || atomicPredicates.get(argList.get(0)).contains(option))
							continue;
					modelPredicates.add(option);
				}
			}
		}
		return new ArrayList<Statement>(modelPredicates);
	}

	@Override
	public Statement negated() {
		assert arguments.size() >= 2;
		List<Statement> ineqs = new ArrayList<>(arguments.size() * (arguments.size() - 1) / 2);
		for (int i = 0; i < arguments.size(); i++)
			for (int j = i + 1; j < arguments.size(); j++)
				ineqs.add(new Inequality(arguments.get(i), arguments.get(j), atomicPredicates));
		return new Disjunction(ineqs);
	}
	
	private static Set<List<LogicObject>> replaceArgs(final List<LogicObject> arguments, final LogicObject current, final Set<LogicObject> replacements) {
		assert arguments != null;
		assert current != null;
		assert replacements != null;
		if (arguments.isEmpty())
			return Set.of();
		Set<List<LogicObject>> argCombos = new HashSet<>();
		getOptions(arguments.get(0), current, replacements).forEach(opt -> argCombos.add(List.of(opt)));
		arguments.listIterator(1).forEachRemaining(arg -> {
			var prevArgCombos = Set.copyOf(argCombos);
			argCombos.clear();
			for (var option: getOptions(arg, current, replacements)) {
				var temp = prevArgCombos.stream().map(LinkedList::new).collect(Collectors.toUnmodifiableList());
				temp.forEach(optlist -> optlist.add(option));
				argCombos.addAll(temp);
			}
		});
		return argCombos;
	}
	
	private static Set<LogicObject> getOptions(final LogicObject innerObject, final LogicObject current, final Set<LogicObject> replacements) {
		assert innerObject != null;
		assert current != null;
		assert replacements != null;
		if (innerObject instanceof Variable)
			return Set.of(innerObject);
		if (innerObject.equals(current))
			return replacements;
		if(!(innerObject instanceof FunctionSymbol))
			return Set.of(innerObject);
		var func = (FunctionSymbol) innerObject;
		return replaceArgs(func.getArguments(), current, replacements).stream()
				.map(args -> (LogicObject)new FunctionSymbol(func.getSymbol(), args))
				.collect(Collectors.toUnmodifiableSet());
	}
}
