package perl.aaron.TruthTrees.util;

public class Option<T> {
	public T unwrap() throws NoneResult {
		throw new NoneResult();
	}
	
	public static final class Some<T> extends Option<T> {
		private final T val;
		
		public Some(final T val) {
			this.val = val;
		}
		
		@Override
		public T unwrap() {
			return val;
		}
	}
	
	public static final class None<T> extends Option<T> {}
}
