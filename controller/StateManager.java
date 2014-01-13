/**
 * 
 */
package team139.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import team139.states.State;
import team139.utils.Util;


/**
 *
 */
public abstract class StateManager {
	
	private Map<String, State> states;
	private Vector<State> stateStack;
	
	public StateManager() {
		this.states = new HashMap<String, State>();
		this.stateStack = new Vector<State>();
	}
	
	// Managing the extant states.
	// =======================================================================
	
	/**
	 * After being added, the state can now be set active, etc.
	 * @param state
	 * @return The same state that was passed in.
	 */
	public State addState(State state) {
		
		if (states.containsKey(state.getName())) {
			throw new Error(
					"State '" + state.getName() + "' is already managed."); 
		}
		
		states.put(state.getName(), state);
		return state;
	}
	
	/**
	 * Returns null if the state doesn't exist. If it's active, deactivate it.
	 * If it's in the active stack, remove it.
	 * @param state that is removed.
	 * @return
	 */
	public State removeState(String name) {
		
		// If it does not exist, return null.
		if (!states.containsKey(name)) return null;
		
		// Get the state to remove.
		State toRemove = states.get(name);
		
		// Remove the state from the map.
		states.remove(name);
		
		// If it's active, deactivate it.
		if (getActiveState().getName() == name) {
			deactivateCurrentActiveState();
		}
		
		// If it's in the active queue, remove it.
		stateStack.remove(toRemove);
		
		// Return the state that we just removed.
		return toRemove;
		
	}
	
	
	// Managing the state stack.
	// =======================================================================
	
	/**
	 * If the State is not already in the manager, we throw an exception.
	 * @param name of the state to make active.
	 */
	public void setActiveState(String name) {
		// If the state is not in the manager, throw exception.
		if (!states.containsKey(name)) {
			throw new Error("State '" + name + "' not in manager.");
		}
		
		State newActiveState = states.get(name);
		
		// If there is a currently active state:
		if (stateStack.size() != 0) {
			
			// If this state is already the active one, do nothing.
			if(stateStack.lastElement().getName() == name) return;
			
			// Pause the currently active state.
			stateStack.lastElement().pause();
			
			// If the state to be made active is already in the stack:
			// Note: necessarily, we must find the first index, which
			// indexOf() does.
			int indexOfNewActiveState = stateStack.indexOf(newActiveState);
			if (indexOfNewActiveState != -1) {
				
				// Unpause the state in the stack, because it's currently
				// paused.
				newActiveState.unpause();
				
				// Remove it from its current place.
				stateStack.remove(indexOfNewActiveState);
			}
		}
		
		// Put the state at the back of the stack.
		stateStack.add(newActiveState);	
		
		// Initialize the state.
		newActiveState.wakeUp();
		
	}
	
	pushTasks(tasksArray ) {
		for task in tasks array:
			pushTasks(task.dependencies();)
			tasks.add(task);
		task.
	}
	
	/**
	 * The state that is running.
	 * @return
	 */
	public final State getActiveState() {
		if (stateStack.size() == 0) {
			Util.debug_print("No active state.");
			return null;
		}
		return stateStack.lastElement();
	}
	
	public final int activeStates() {
		return stateStack.size();
	}
	
	/**
	 * Remove the currently active state from the stack of active states.
	 * Shut it down!! But it's still around in case you want it to be active
	 * again.
	 */
	public void deactivateCurrentActiveState() {
		// Give the state a chance to end itself pleasantly.
		stateStack.lastElement().powerDown();
		
		// Remove as active state.
		stateStack.remove(stateStack.lastElement());
		
		// Resume the next active state.
		if (stateStack.size() != 0) stateStack.lastElement().unpause();
	}

}
