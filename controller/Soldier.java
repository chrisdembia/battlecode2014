/**
 * 
 */
package team139.controller;

import java.util.Random;

import team139.actions.Attacker;
import team139.actions.Mover;
import team139.utils.Util;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

/**
 *
 */
public class Soldier extends Controller {

	private final Attacker attacker;
	private final Mover mover;

	/**
	 * @param rc
	 */
	public Soldier(RobotController rc) {
		super(rc);
		this.attacker = new Attacker(rc);
		this.mover = new Mover(rc);
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() throws GameActionException {
		int action = (rc.getRobot().getID() * rand.nextInt(101) + 50) % 101;
		
		if (action < 1 && model.myManDistanceTo(rc.senseHQLocation()) > 2) {
			rc.construct(RobotType.PASTR);
			
		} else if (action < 30) {
			if (model.existsNearbyEnemies()) {
				// Model should do the caching of the most nearby enemy.
				attacker.attack(model.firstNearbyEnemyLocation());
			}
		
		} else if (action < 80) {
			mover.move(Util.DIRECTIONS[rand.nextInt(8)]);
			
		} else {
			mover.sneak(model.directionTowardsEnemyHQ());
		}

	}

}
