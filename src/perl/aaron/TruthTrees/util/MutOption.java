package perl.aaron.TruthTrees.util;

import java.util.function.Consumer;

public final class MutOption<T> {
	private T val;
	
	public MutOption() {
		setNone();
	}
	
	public MutOption(T val) {
		set(val);
	}
	
	public T unwrap() throws NoneResult {
		if (val == null)
			throw new NoneResult();
		return val;
	}
	
	public void if_some(Consumer<T> func) {
		if (val != null)
			func.accept(val);
	}
	
	public void set(T val) {
		assert val != null : "Cant set MutOption to null";
		this.val = val;
	}
	
	public void setNone() {
		val = null;
	}
	
	public boolean isNone() {
		return val == null;
	}
	
	@Override
	public String toString() {
		return "" + val;
	}
	
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
	
	public boolean equals(MutOption<T> other) {
		if (val != null && other.val != null)
			return val.equals(other.val);
		return val == other.val;
	}
	
	public boolean valEquals(T val) {
		return this.val != null && this.val.equals(val);
	}
}
