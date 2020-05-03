package perl.aaron.TruthTrees.util;

import java.util.ArrayDeque;
import java.util.Deque;

public class History {
	private final int capacity;
	private final Deque<Action> undoStack;
	private final Deque<Action> redoStack;
	
	public interface Action {
		public Action run();
	}
	
	public History(final int capacity) {
		this.capacity = capacity;
		undoStack = new ArrayDeque<>(capacity);
		redoStack = new ArrayDeque<>(capacity);
	}
	
	public void push(final Action act) {
		redoStack.clear();
		if(undoStack.size() == capacity)
			undoStack.removeLast();
		undoStack.push(act.run());
	}
	
	public boolean redo() {
		if (redoStack.isEmpty())
			return false;
		assert undoStack.size() != capacity : "Undo stack should not be full when redoStack is non-empty";
		undoStack.push(redoStack.pop().run());
		return true;
	}
	
	public boolean undo() {
		if (undoStack.isEmpty())
			return false;
		assert redoStack.size() != capacity : "Redo stack should never be full when undoStack is non-empty";
		redoStack.push(undoStack.pop().run());
		return true;
	}
	
	public void clear() {
		undoStack.clear();
		redoStack.clear();
	}

}
