/**
 * 
 */
package team139.model;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * This is how the Controller learns anything about its environment. Here, we
 * are able to cache information that may be requested multiple times. Methods
 * here always return objective observations of the environment.
 * TODO add generic caching ability.
 */
public class Model {
	
	// TODO make this static?
	private final RobotController rc;

	public Model(RobotController rc) {
		this.rc = rc;
	}
	
	/**
	 * TODO cache getLocation()?
	 * @return
	 */
	public Direction directionToEnemyHQ() {
		return rc.getLocation().directionTo(rc.senseEnemyHQLocation());
	}
	
	/**
	 * TODO prevent throwing; make sure we can sense; can ALL objects sense their adjacent squares?
	 * @param loc
	 * @return
	 * @throws GameActionException 
	 */
	public boolean canSpawnInDirection(Direction dir)
			throws GameActionException {
		// Location loc = rc.getLocation().add(dir)
		// if rc.canSenseSquare(loc) { ...
		return rc.senseObjectAtLocation(rc.getLocation().add(dir)) == null;
	}
}
