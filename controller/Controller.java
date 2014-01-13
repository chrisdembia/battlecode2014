/**
 * 
 */
package team139.controller;

import java.util.Random;

import team139.model.Model;
import battlecode.common.GameActionException;
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
	protected final Model model;
	protected final int END_TURN_BYTECODE_USE = 0;
	// TODO why did I make this static and the others final?
	protected static Random rand;

	public Controller(RobotController rc) {
		this.rc = rc;
		this.model = new Model(rc);
		rand = new Random();
	}
	
	public final void run() {
		while (true) {
			try {
				beginTurn();
				takeOneTurn();
				// TODO endTurn();
				yield();
			}
			catch (Exception e) {
				// TODO how to handle exceptions?
				e.printStackTrace();
				rc.breakpoint();
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
	
	protected void yield() {
		rc.yield();
		model.invalidate();
	}
	
	// TODO prevent this method from being able to throw exceptions.
	public abstract void takeOneTurn() throws GameActionException;

	public String toString() {
		return rc.getType() + " Controller with ID " + rc.getRobot().getID();
	}
}
