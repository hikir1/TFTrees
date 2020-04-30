package perl.aaron.TruthTrees.util;

public class UserError extends Exception {
	static final long serialVersionUID = 1;
	
	public UserError(String msg) {
		super(msg);
	}
}
