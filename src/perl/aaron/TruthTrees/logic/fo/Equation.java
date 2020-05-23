package perl.aaron.TruthTrees.logic.fo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import perl.aaron.TruthTrees.logic.Disjunction;
import perl.aaron.TruthTrees.logic.I_Statement;
import perl.aaron.TruthTrees.logic.negation.fo.Inequation;
import perl.aaron.TruthTrees.util.UserError;

public class Equation extends A_EquationType {
	public static final String TYPE_NAME = "Equality";
	public static final String SYMBOL = "=";
	
	private final Set<A_LogicObject> uniqueArguments;
	
	public Equation(List<A_LogicObject> arguments, Map<A_LogicObject,Set<I_PredicateType>> atomicPredicates) {
		super(TYPE_NAME, SYMBOL, arguments, atomicPredicates);
		assert arguments != null;
		uniqueArguments = new HashSet<A_LogicObject>(arguments);
	}
	
	@Override
	public Equation withArgs(List<A_LogicObject> arguments) {
		return new Equation(arguments, atomicPredicates);
	}
	
	
	// redundancy does not effect equivalency of equations
	// ex: a = a = b is the same as a = b
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Equation))
			return false;
		return uniqueArguments.equals(((Equation)other).uniqueArguments);
	}
	
	@Override
	public int hashCode() {
		return symbol.hashCode() ^ uniqueArguments.hashCode();
	}
	
	@Override
	protected void subVerifyDecomposition(List<List<I_Statement>> branches) throws UserError {
		Set<A_LogicObject> arguments = Set.copyOf(this.arguments);
		Set<I_PredicateType> currentPredicates, modelPredicates = new HashSet<>();
		for (A_LogicObject equArg: arguments) {
			currentPredicates = atomicPredicates.get(equArg);
			for (I_PredicateType predicate: currentPredicates) {
				for (List<A_LogicObject> argList: replaceArgs(predicate.getArguments(), equArg, arguments)) {
					I_PredicateType option = predicate.withArgs(argList);
					if (option instanceof Equation)
						// Prevent infinite recursive decomposition by skipping Equality if already exists
						// option.arguments could be empty if tautology
						if (((Equation)option).uniqueArguments.size() == 1 || atomicPredicates.get(argList.get(0)).contains(option))
							continue;
					modelPredicates.add(option);
				}
			}
		}
		verifySerialDecomposition(branches, List.copyOf(modelPredicates));
	}

	@Override
	public I_Statement negated() {
		assert arguments.size() >= 2;
		List<I_Statement> ineqs = new ArrayList<>(arguments.size() * (arguments.size() - 1) / 2);
		for (int i = 0; i < arguments.size(); i++)
			for (int j = i + 1; j < arguments.size(); j++)
				ineqs.add(new Inequation(arguments.get(i), arguments.get(j), atomicPredicates));
		return new Disjunction(ineqs);
	}
	
	private static Set<List<A_LogicObject>> replaceArgs(final List<A_LogicObject> arguments, final A_LogicObject current, final Set<A_LogicObject> replacements) {
		assert arguments != null;
		assert current != null;
		assert replacements != null;
		if (arguments.isEmpty())
			return Set.of();
		Set<List<A_LogicObject>> argCombos = new HashSet<>();
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
	
	private static Set<A_LogicObject> getOptions(final A_LogicObject innerObject, final A_LogicObject current, final Set<A_LogicObject> replacements) {
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
				.map(args -> (A_LogicObject)new FunctionSymbol(func.getSymbol(), args))
				.collect(Collectors.toUnmodifiableSet());
	}
	
}
