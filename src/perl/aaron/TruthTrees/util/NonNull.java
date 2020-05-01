package perl.aaron.TruthTrees.util;

public final class NonNull<T> {
	private T val;
	public NonNull(T val) {
		set(val);
	}
	public void set(T val) {
		assert val != null : "NonNull cannot be null!";
		this.val = val;
	}
	public T get() {
		return val;
	}
	public boolean equals(NonNull<T> other) {
		return val.equals(other.val);
	}
	@Override
	public boolean equals(Object val) {
		return this.val.equals(val);
	}
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
	@Override
	public String toString() {
		return val.toString();
	}
}
