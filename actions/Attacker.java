/**
 * 
 */
package team139.actions;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;

/**
 *
 */
public class Attacker {

	private final RobotController rc;

	public Attacker(RobotController rc) {
		this.rc = rc;
	}

	public final boolean attack(MapLocation loc) throws GameActionException {
		// TODO this is probably not a sufficient check to see if we can
		// attack.
		if (rc.isActive()) {
			rc.attackSquare(loc);
			return true;
		}
		return false;		
	}
	
}
