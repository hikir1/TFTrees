package perl.aaron.TruthTrees.logic;

public abstract class ASymbolString implements SymbolString {
	
	protected final String typeName;
	protected final String symbol;
	
	protected ASymbolString(String typeName, String symbol) {
		assert typeName != null;
		assert symbol != null;
		this.typeName = typeName;
		this.symbol = symbol;
	}
	
	@Override
	public final String getSymbol() {
		return symbol;
	}
	
	@Override
	public final String toString() {
		return typeName + symString();
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null || !getClass().equals(other.getClass()))
			return false;
		return symbol.equals(((ASymbolString)other).symbol);
	}
	
	@Override
	public int hashCode() {
		return typeName.hashCode() ^ symbol.hashCode();
	}
	
}
