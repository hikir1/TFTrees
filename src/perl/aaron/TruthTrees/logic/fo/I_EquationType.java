package perl.aaron.TruthTrees.logic.fo;

import java.util.stream.Collectors;

public interface I_EquationType extends I_PredicateType {
	@Override
	default String symString() {
		return getArguments().stream().map(a -> a.symString()).collect(Collectors.joining(" " + getSymbol() + " "));
	}
}
