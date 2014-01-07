/**
 * 
 */
package team139.actions;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 *
 */
public class Spawner {
	
	private final RobotController rc;

	/**
	 * 
	 * @param rc
	 */
	public Spawner(RobotController rc) {
		this.rc = rc;
	}

	/**
	 * TODO catch the exception here, or ensure that it won't get thrown.
	 * @param direction in which to spawn a Soldier.
	 * @return successfully spawned
	 * @throws GameActionException 
	 */
	public boolean spawn(Direction dir) throws GameActionException {
		// TODO use a yield() here?
		// TODO not sure the logic is correct.
		if (rc.isActive()) {
			rc.spawn(dir);
			return true;
		}
		return false;
	}
	
	

}
