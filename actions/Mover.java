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
public class Mover {

	private final RobotController rc;

	/**
	 * 
	 * @param rc
	 */
	public Mover(RobotController rc) {
		this.rc = rc;
	}

	/**
	 * 
	 * @param dir
	 * @return
	 * @throws GameActionException
	 */
	public final boolean move(Direction dir) throws GameActionException {
		if (rc.isActive() && rc.canMove(dir)) {
			rc.move(dir);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param dir
	 * @return
	 * @throws GameActionException
	 */
	public final boolean sneak(Direction dir) throws GameActionException {
		if (rc.isActive() && rc.canMove(dir)) {
			rc.sneak(dir);
			return true;
		}
		return false;
	}
}
