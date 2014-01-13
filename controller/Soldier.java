/**
 * 
 */
package team139.controller;

import java.util.Random;

import team139.actions.Attacker;
import team139.actions.Mover;
import team139.model.MissionAssignment;
import team139.utils.Util;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.TerrainTile;

/**
 *
 */
public class Soldier extends Controller {

	private final Attacker attacker;
	private final Mover mover;
	
	
	private final int PASTR_ATTACK_THRESH = 50; //If attacking a pastr with this health or less, disregard enemy soldiers
	
	public static enum Mission {
		Sentry,
		AttackNearestPASTR,
		PASTRBuilder,
		HerderDefender,
		ExamplePlayer,
	}
	
	private Mission mission;
	
	// AttackNearestPASTR useful variables
	MapLocation pastrLocation = null;
	RobotInfo pastrInfo;

	private int id;

	/**
	 * @param rc
	 */
	public Soldier(RobotController rc) {
		super(rc);
		this.attacker = new Attacker(rc);
		this.mover = new Mover(rc);
		this.mission = Mission.Sentry;
		this.id = rc.getRobot().getID();
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() throws GameActionException {
		
		determineMission();

		switch (mission) {
		case Sentry:
			break;
		case AttackNearestPASTR:
			// Check if you are in range of an enemy PASTR
			if (pastrLocation == null){
				pastrLocation = getNearestPastrLocation();
				if (pastrLocation == null){
					//TODO: This should really tell the soldier to go to the enemy base and wait for 
					// enemies to be spawned
					mission = Mission.ExamplePlayer;
					break;
				}
			}
			System.out.println(pastrLocation.toString());
			if (rc.canAttackSquare(pastrLocation)){
				// If yes, 
				//Does it have more than PASTR_ATTACK_THRESH hp?
				Robot nearestPastr = (Robot)rc.senseObjectAtLocation(pastrLocation);
				if (nearestPastr == null) break; //TODO: the pastr is already dead when we get here;
				pastrInfo = rc.senseRobotInfo(nearestPastr);
				if (pastrInfo.health > PASTR_ATTACK_THRESH && enemySoldiersInRange()){					
					//yes: attack enemy soldiers in range, or attack PASTR if no enemies
					Robot enemySoldier = getEnemySoldierInRange();
					while (!rc.isActive()){
						rc.yield();
					}
					rc.attackSquare(rc.senseLocationOf(enemySoldier));
				}
				else {
					//no: attack it
					while (!rc.isActive()){
						//TODO: Check if soldiers are close by
						rc.yield();
					}
					rc.attackSquare(pastrLocation);
				}
			
			}
			else {
				// If no
				// Kill nearby enemies
				if (model.existsNearbyEnemies() && attacker.attack(model.nearestEnemyLocation.get())){					
					//yes: attack enemy soldiers in range, or attack PASTR if no enemies
					break;
				}
				else{
				// Go to nearest PASTR
					/// CRUDE PATHFINDING:
					//MapLocation nextStep;
					Direction stepDirection = rc.getLocation().directionTo(pastrLocation);
					//nextStep = rc.getLocation().add(stepDirection);
					while (!rc.canMove(stepDirection)){
						stepDirection = stepDirection.rotateRight();
						//nextStep = rc.getLocation().add(stepDirection);
					}
					mover.move(stepDirection);
//					while (!rc.isActive()){
//						rc.yield();
//					}
//					rc.move(stepDirection);
					//pathfinder.goTo(pastrLocation);
				}
			}
					
			break;
		case ExamplePlayer:
			if (getNearestPastrLocation() != null){
				mission = Mission.AttackNearestPASTR;
				break;
			}
			
			if (rc.isActive()) {
				int action = (rc.getRobot().getID() * rand.nextInt(101) + 50) % 101;

				if (action < 1 &&
						model.myManDistanceTo(rc.senseHQLocation()) > 2) {
					rc.construct(RobotType.PASTR);

				} else if (action < 30) {
					if (model.existsNearbyEnemies()) {
						// Model should do the caching of the most nearby enemy.
						attacker.attack(model.nearestEnemyLocation.get());
					}

				} else if (action < 80) {
					mover.move(Util.DIRECTIONS[rand.nextInt(8)]);

				} else {
					mover.sneak(model.directionTowardsEnemyHQ());
				}
			}
			break;
		case HerderDefender:
			break;
		case PASTRBuilder:
			break;
		default:
			break;
		}

	}

	private MapLocation getNearestPastrLocation() throws GameActionException {
		MapLocation[] allLocations = this.rc.sensePastrLocations(this.model.opponent);
		if (allLocations.length == 0) return null;
		double minDist = 9999999;
		MapLocation returnLoc = allLocations[0];
		for (MapLocation loc : allLocations){
			if (rc.getLocation().distanceSquaredTo(loc) < minDist)
				returnLoc = loc;
		}
		return returnLoc;

	private void determineMission() {
		mission = new MissionAssignment(rc, rc.getRobot().getID()).mission;
	}

}
