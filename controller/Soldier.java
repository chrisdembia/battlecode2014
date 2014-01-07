/**
 * 
 */
package team139.controller;

import java.util.Random;

import battlecode.common.Direction;
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
		Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST,
				Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH,
				Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
		
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() {
		int action = (rc.getRobot().getID() * rand.nextInt(101) + 50) % 101;
		
		// TODO write manhattan distance function.
		if (action < 1 && model.myManDistanceTo(rc.senseHQLocation()) > 2) {
			rc.construct(RobotType.PASTR);
			
		} else if (action < 30) {
			if (model.existsNearbyEnemies()) {
				// Model should do the caching of the most nearby enemy.
				attacker.attackFirstNearbyNeighbor();
			}
		
		} else if (action < 80) {
			mover.move(directions[rand.nextInt(8)]);
			
		} else {
			mover.sneakTowards(rc.senseEnemyHQLocation());
		}

	}

}
