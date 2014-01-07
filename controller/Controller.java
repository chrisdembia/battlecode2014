/**
 * 
 */
package team139.controller;

import battlecode.common.RobotController;

/**
 * We have a controller for each RobotType, and possibly for various
 * specializations of RobotType's. The Controller is where the AI logic lives.
 * The Controller uses information from a Model to decide what it should do,
 * then calls Action's.
 *
 */
public abstract class Controller {
	
	protected final RobotController rc;

	public Controller(RobotController rc) {
		this.rc = rc;
	}
	
	public final void run() {
		while (true) {
			try {
				beginTurn();
				takeOneTurn();
				endTurn();
				
				rc.yield();
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
		
	}
	
	public abstract void takeOneTurn();

	public String toString() {
		return rc.getType() + " Controller with ID " + rc.getRobot().getID();
	}
}
