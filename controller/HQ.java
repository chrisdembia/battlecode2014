/**
 * 
 */
package team139.controller;

import team139.actions.Spawner;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;

/**
 *
 */
public class HQ extends Controller {

	// TODO can these be static (instead)? if so, should they be?
	public final Spawner spawner;

	/**
	 * @param rc
	 */
	public HQ(RobotController rc) {
		super(rc);
		this.spawner = new Spawner(rc);
	}
	
	@Override
	protected void decideState() throws GameActionException {
		
		// TODO wrong place; create a State.
		if (rc.senseRobotCount() < GameConstants.MAX_ROBOTS) {
			Direction dir = model.directionToEnemyHQ();
			if (model.canSpawnInDirection(dir)) { 
				spawner.spawn(dir);
			}
		}		
	}
}
