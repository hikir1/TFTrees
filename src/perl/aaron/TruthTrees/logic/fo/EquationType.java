package perl.aaron.TruthTrees.logic.fo;

import java.util.stream.Collectors;

public interface EquationType extends PredicateType {
	@Override
	default String symString() {
		return getArguments().stream().map(a -> a.toString()).collect(Collectors.joining(" " + getSymbol() + " "));
	}
}
