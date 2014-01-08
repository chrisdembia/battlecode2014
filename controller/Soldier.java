/**
 * 
 */
package team139.controller;

import java.util.Random;

import team139.utils.Util;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

/**
 *
 */
public class Soldier extends Controller {
	
	// TODO this could be final as well.
	protected static Direction[] directions;

	/**
	 * @param rc
	 */
	public Soldier(RobotController rc) {
		super(rc);

		// TODO delete this when we don't need it.
		
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() {
		int action = (rc.getRobot().getID() * rand.nextInt(101) + 50) % 101;
		
		if (action < 1 && model.myManDistanceTo(rc.senseHQLocation()) > 2) {
			rc.construct(RobotType.PASTR);
			
		} else if (action < 30) {
			if (model.existsNearbyEnemies()) {
				// Model should do the caching of the most nearby enemy.
				attacker.attackFirstNearbyNeighbor();
			}
		
		} else if (action < 80) {
			mover.move(Util.DIRECTIONS[rand.nextInt(8)]);
			
		} else {
			mover.sneakTowards(rc.senseEnemyHQLocation());
		}

	}

}
