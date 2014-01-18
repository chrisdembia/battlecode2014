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
import battlecode.common.Robot;
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
//		return Strategy.Dothraki;
		return Strategy.Murica;

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
				spawnAndAssignSoldier(dir, Soldier.Mission.AttackNearestPASTR);
			}
			
			/* TODO uncomment
			if (model.existsNearbyAttackableEnemies()) {
				attacker.attack(model.nearestEnemyLocation());
			}
			*/
			
			break;
		case Murica:
			rc.setIndicatorString(1, "Making NoiseTowerBuilder");
			while (!spawnAndAssignSoldier(Direction.NORTH, Soldier.Mission.NoiseTowerBuilder)) yield();
			yield();
			rc.setIndicatorString(1, "Making PASTRBuilder");
			while (!spawnAndAssignSoldier(Direction.NORTH_EAST, Soldier.Mission.PASTRBuilder)) yield();
			rc.setIndicatorString(1, "Defending");
			defendIndefinitely();
			
			break;
		default:
			break;
		}
	}
	
	private void defendIndefinitely() throws GameActionException {
		while (true){
			if (model.existsNearbyEnemies()){
				rc.setIndicatorString(1, "I see an enemy!");
				while (!rc.isActive()) {
					rc.setIndicatorString(2, "Waiting for an active round!");
					yield();
					if (!model.existsNearbyEnemies()) {
						rc.setIndicatorString(1, "No longer any enemies.4");
						continue;
					}
				}
				attacker.attack(model.nearestEnemyLocation.get());
				yield();
				continue;
			}
			else{
				rc.setIndicatorString(1, "No Nearby Enemies");
			}
			//Make sure PASTR exists
			if (rc.senseObjectAtLocation(rc.getLocation().add(Direction.NORTH_EAST)) == null){
				while (!spawnAndAssignSoldier(Direction.NORTH_EAST, Soldier.Mission.PASTRBuilder));
				continue;
			}
			//Make sure Noise Tower Exists
			if (rc.senseObjectAtLocation(rc.getLocation().add(Direction.NORTH)) == null){
				while (!spawnAndAssignSoldier(Direction.NORTH, Soldier.Mission.NoiseTowerBuilder));
				continue;
			}
			//Try to build more offensive Soldiers
			if (rc.senseRobotCount() < GameConstants.MAX_ROBOTS) {
				spawnAndAssignSoldier(Direction.SOUTH, Soldier.Mission.AttackNearestPASTR);
			}
			yield();
		}
		
	}

	private boolean spawnAndAssignSoldier(Direction dir, Soldier.Mission mission) throws GameActionException{
		if (model.canSpawnInDirection(dir)) { 
			if (!spawner.spawn(dir)) return false;
			MapLocation newlySpawned = rc.getLocation().add(dir);
			yield();
			Robot newBot = (Robot) rc.senseObjectAtLocation(newlySpawned);
			if (newBot != null) {
				MissionAssignment.broadcast(rc, newBot.getID(), mission);
			}
			return true;
		}
		return false;
	}
}
