package perl.aaron.TruthTrees.util;

import java.util.ArrayDeque;
import java.util.Deque;

public class History {
	private final int capacity;
	private final Deque<Action> undoStack;
	private final Deque<Action> redoStack;
	
	private static class Action {
		final Runnable undo;
		final Runnable redo;
		Action(final Runnable redo, final Runnable undo) {
			this.redo = redo;
			this.undo = undo;
		}
	}
	
	public History(final int capacity) {
		this.capacity = capacity;
		undoStack = new ArrayDeque<>(capacity);
		redoStack = new ArrayDeque<>(capacity);
	}
	
	public void push(final Runnable redo, final Runnable undo) {
		redoStack.clear();
		if(undoStack.size() == capacity)
			undoStack.removeLast();
		undoStack.push(new Action(redo, undo));
	}
	
	public boolean redo() {
		if (redoStack.isEmpty())
			return false;
		assert undoStack.size() != capacity : "Undo stack should not be full when redoStack is non-empty";
		final Action act = redoStack.pop();
		act.redo.run();
		undoStack.push(act);
		return true;
	}
	
	public boolean undo() {
		if (undoStack.isEmpty())
			return false;
		assert redoStack.size() != capacity : "Redo stack should never be full when undoStack is non-empty";
		final Action act = undoStack.pop();
		act.undo.run();
		redoStack.push(act);
		return true;
	}
	
	public void clear() {
		undoStack.clear();
		redoStack.clear();
	}

}
