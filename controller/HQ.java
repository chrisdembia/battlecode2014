/**
 * 
 */
package team139.controller;

import team139.actions.Spawner;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 *
 */
public class HQ extends Controller {

	// TODO can these be static (instead)? if so, should they be?
	private final Spawner spawner;

	/**
	 * @param rc
	 */
	public HQ(RobotController rc) {
		super(rc);
		this.spawner = new Spawner(rc);
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() throws GameActionException {
		if (rc.senseRobotCount() <= 25) {
			Direction dir = model.directionToEnemyHQ();
			if (model.canSpawnInDirection(dir)) { 
				spawner.spawn(dir);
			}
		}
	}
}
