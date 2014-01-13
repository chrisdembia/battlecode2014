/**
 * 
 */
package team139.controller;

import team139.actions.Attacker;
import team139.actions.Spawner;
import team139.model.MissionAssignment;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

/**
 *
 */
public class HQ extends Controller {

	// TODO can these be static (instead)? if so, should they be?
	private final Spawner spawner;
	private final Attacker attacker;
	
	private static enum Strategy {
		Dothraki,
		Murica,
		Suicide
	}
	
	private Strategy strategy;

	/**
	 * @param rc
	 */
	public HQ(RobotController rc) {
		super(rc);
		this.spawner = new Spawner(rc);
		this.attacker = new Attacker(rc);
		this.strategy = determineStrategy();
	}
	
	private Strategy determineStrategy() {
		return Strategy.Dothraki;
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() throws GameActionException {
		
		switch (strategy) {
		case Dothraki:
			if (rc.senseRobotCount() < GameConstants.MAX_ROBOTS) {
				Direction dir = model.directionToEnemyHQ();
				if (model.canSpawnInDirection(dir)) { 
					spawner.spawn(dir);
					MapLocation newlySpawned = rc.getLocation().add(dir);
					yield();
					if (rc.senseObjectAtLocation(newlySpawned) != null) {
						// TODO not the exact check we want but it should
						// suffice.
						MissionAssignment.broadcast(rc,
								rc.senseObjectAtLocation(newlySpawned).getID(),
								Soldier.Mission.ExamplePlayer);
					}
				}
			}
			
			/* TODO uncomment
			if (model.existsNearbyAttackableEnemies()) {
				attacker.attack(model.nearestEnemyLocation());
			}
			*/
			
			break;
		default:
			break;
		}
	}
}
