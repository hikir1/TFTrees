package perl.aaron.TruthTrees.util;

public class Option {
	public Object unwrap() throws NoneResult {
		throw new NoneResult();
	}
	
	public static final class Some<T> extends Option {
		private final T val;
		
		public Some(final T val) {
			this.val = val;
		}
		
		@Override
		public T unwrap() {
			return val;
		}
	}
	
	public static final class None extends Option {}
}
