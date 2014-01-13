/**
 * 
 */
package team139.controller;

import team139.model.Model;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * We have a controller for each RobotType, and possibly for various
 * specializations of RobotType's. The Controller is where the AI logic lives.
 * The Controller uses information from a Model to decide what it should do,
 * then calls Action's.
 * 
 * The Controller is also a StateManager: it manages a queue of states.
 *
 */
public abstract class Controller extends StateManager {
	
	protected final RobotController rc;
	protected final Model model;
	protected final int END_TURN_BYTECODE_USE = 0;

	public Controller(RobotController rc) {
		super();
		this.rc = rc;
		this.model = new Model(rc);
	}
	
	public final RobotController rc() {
		return rc;
	}
	
	public final Model model() {
		return model;
	}
	
	public final void run() {
		while (true) {
			try {
				beginTurn();
				decideState();
				
				while (activeTasks() != 0) {
					getActiveTask().addDependencies();
					getActiveTask().do();
				}
				/*
					while (activeState().complete()) {
						getActiveState.run();
					}
					getActiveState().resolveDependencies();
					getActiveState().run()
				}
				if (activeStates() > 0) getActiveState().run();
				yield();
				*/
			}
			catch (Exception e) {
				// TODO how to handle exceptions?
				e.printStackTrace();
			}
		}
	}

	private void beginTurn() {
		// TODO Auto-generated method stub
		
	}
	
	
	private void endTurn() {
		// TODO Auto-generated method stub
		
		// IMPORTANT: Make sure the END_TURN_BYTECODE_USE variable 
		//			  reflects the bytecode used in this method
		
	}
	
	private void yield() {
		
		rc.yield();
		// TODO model.invalidate().
	}
	
	/**
	 * This is the Controller's chance to directly affect what states
	 * should be in the queue.
	 * @throws GameActionException 
	 */
	protected abstract void decideState() throws GameActionException;

	public String toString() {
		return rc.getType() + " Controller with ID " + rc.getRobot().getID();
	}
}
