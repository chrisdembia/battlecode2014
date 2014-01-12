/**
 * 
 */
package team139.states;

import java.util.Random;

import team139.controller.Controller;
import team139.controller.Soldier;
import team139.model.Model;
import team139.utils.Util;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

/**
 * The Soldier logic employed by the examplefuncsplayer.
 * This State should only be used by Soldiers.
 */
public class SoldierExample extends State {

	// TODO why did I make this static and the others final?
	protected static final Random rand = new Random();
	private final Soldier soldier;
	
	/**
	 * 
	 * @param name
	 * @param con
	 */
	public SoldierExample(String name, Controller con) {
		super(name, con);
		assert(con instanceof Soldier);
		this.soldier = (Soldier) con;
	}

	/* (non-Javadoc)
	 * @see team139.states.State#run()
	 */
	@Override
	public void run() throws GameActionException {
		if (con.rc().isActive()) {
			int action = (con.rc().getRobot().getID() * rand.nextInt(101) + 50) % 101;

			if (action < 1 && model.myManDistanceTo(rc.senseHQLocation()) > 2) {
				rc.construct(RobotType.PASTR);

			} else if (action < 30) {
				if (model.existsNearbyEnemies()) {
					// Model should do the caching of the most nearby enemy.
					soldier.attacker.attack(model.firstNearbyEnemyLocation());
				}

			} else if (action < 80) {
				soldier.mover.move(Util.DIRECTIONS[rand.nextInt(8)]);

			} else {
				soldier.mover.sneak(model.directionTowardsEnemyHQ());
			}
		}

	}

}
