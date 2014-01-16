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
		NoiseTowerBuilder,
		PASTRBuilder,
		HerderDefender,
		ExamplePlayer,
	}
	
	private Mission mission;
	
	// AttackNearestPASTR useful variables
	MapLocation pastrLocation = null;
	RobotInfo pastrInfo;
	
	MapLocation prevLocation = null;

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
					if (model.existsNearbyEnemies() && attacker.attack(model.nearestEnemyLocation.get())) {					
						//yes: attack enemy soldiers in range, or attack PASTR if no enemies
						break;
					}
					else{
					// Go to Enemy HQ
						/// CRUDE PATHFINDING:
						Direction stepDirection = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
						while (!rc.canMove(stepDirection) || rc.getLocation().add(stepDirection).equals(prevLocation)){
							stepDirection = stepDirection.rotateRight();
						}
						
						if (mover.move(stepDirection)){
							prevLocation = rc.getLocation();
						}
					}
					break;
					
				}
			}
			System.out.println(pastrLocation.toString());
			if (rc.canAttackSquare(pastrLocation)){
				// If yes, 
				//Does it have more than PASTR_ATTACK_THRESH hp?
				Robot nearestPastr = (Robot)rc.senseObjectAtLocation(pastrLocation);
				if (nearestPastr == null) {pastrLocation = null; break; } //Pastr is dead, start over
				pastrInfo = rc.senseRobotInfo(nearestPastr);
				if (pastrInfo.health > PASTR_ATTACK_THRESH && model.existsNearbyEnemies()){					
					//yes: attack enemy soldiers in range, or attack PASTR if no enemies
					attacker.attack(model.nearestEnemyLocation.get());
				}
				else {
					//no: attack it
					attacker.attack(pastrLocation);
//					while (!rc.isActive()){
//						//TODO: Check if soldiers are close by
//						rc.yield();
//					}
//					rc.attackSquare(pastrLocation);
				}
			
			}
			else {
				// If no
				// Kill nearby enemies
				if (model.existsNearbyEnemies() && attacker.attack(model.nearestEnemyLocation.get())) {					
					//yes: attack enemy soldiers in range, or attack PASTR if no enemies
					break;
				}
				else{
				// Go to nearest PASTR
					/// CRUDE PATHFINDING:
					//MapLocation nextStep;
					Direction stepDirection = rc.getLocation().directionTo(pastrLocation);
					//nextStep = rc.getLocation().add(stepDirection);
					while (!rc.canMove(stepDirection)  || rc.getLocation().add(stepDirection).equals(prevLocation)){
						stepDirection = stepDirection.rotateRight();
						//nextStep = rc.getLocation().add(stepDirection);
					}
					if (mover.move(stepDirection)){
						prevLocation = rc.getLocation();
					}
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
		case NoiseTowerBuilder:
			rc.construct(RobotType.NOISETOWER);
			while(true) yield();
		case PASTRBuilder:
			rc.construct(RobotType.PASTR);
			while(true) yield();
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
	}

	private void determineMission() throws GameActionException {
		if (model.myMissionAssignment.get() == null)
			mission = Mission.Sentry;
		else
			mission = model.myMissionAssignment.get();
	}

}
