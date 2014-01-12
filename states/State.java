/**
 * 
 */
package team139.states;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team139.controller.Controller;
import team139.controller.Soldier;
import team139.model.Model;

/**
 *
 */
public abstract class State {
	
	private final String name;
	protected final Controller con;
	protected final RobotController rc;
	protected final Model model;

	public State(String name, Controller con) {
		this.name = name;
		this.con = con;
		this.rc = con.rc();
		this.model = con.model();
	}
	
	public final String getName() {
		return name;
	}
	
	public abstract void run() throws GameActionException;

	/**
	 * What should the state do when another active state is pushed in front of it?
	 * 
	 */
	public void pause() { }
	
	/**
	 * What should the state do when it's resumed? This is not the same
	 * as the execution of the state.
	 */
	public void unpause() { }

	/**
	 * What should the state do when it's deactivated (removed
	 * from the stack of active states)?
	 */
	public void powerDown() { }

	/**
	 * The state has just been added to the active state stack. What should
	 * it do?
	 */
	public void wakeUp()  { }
	
}
