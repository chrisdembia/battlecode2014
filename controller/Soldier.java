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
	
	//Pathfinding variables
	MapLocation prevLocation = null;
	private boolean avoidingObstacle = false;
	private int initialDistFromDest = 0;
	private Direction currentDirection; 
	
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
						goToLocation(model.enemyHQLocation);
//						Direction stepDirection = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
//						while (!model.canMoveInDirection(stepDirection) || rc.getLocation().add(stepDirection).equals(prevLocation)){
//							stepDirection = stepDirection.rotateRight();
//						}
//						
//						if (mover.move(stepDirection)){
//							prevLocation = rc.getLocation();
//						}
					}
					break;
					
				}
			}
			//System.out.println(pastrLocation.toString());
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
					goToLocation(pastrLocation);
					
//					Direction stepDirection = rc.getLocation().directionTo(pastrLocation);
//					//nextStep = rc.getLocation().add(stepDirection);
//					while (!model.canMoveInDirection(stepDirection)  || rc.getLocation().add(stepDirection).equals(prevLocation)){
//						stepDirection = stepDirection.rotateRight();
//						//nextStep = rc.getLocation().add(stepDirection);
//					}
//					if (mover.move(stepDirection)){
//						prevLocation = rc.getLocation();
//					}
					
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
		double minDist = Integer.MAX_VALUE;
		MapLocation returnLoc = allLocations[0];
		for (MapLocation loc : allLocations){
			if (rc.getLocation().distanceSquaredTo(loc) < minDist)
				returnLoc = loc;
		}
		return returnLoc;
	}
	

	
	private boolean goToLocation(MapLocation dest) throws GameActionException{

		if (rc.getLocation().equals(dest)) return true;
		if (!rc.isActive()) return false;
		yield();
		rc.setIndicatorString(2, "");
		if (currentDirection == null) currentDirection = rc.getLocation().directionTo(dest);
		
		if (!avoidingObstacle){ 
			currentDirection  = rc.getLocation().directionTo(dest);
			if (!model.canMoveInDirection(currentDirection)){
				avoidingObstacle = true;
				initialDistFromDest = rc.getLocation().distanceSquaredTo(dest);
				rc.setIndicatorString(2, "Found an obstacle, setting avoidingObstacle");
			}
		}

		if (avoidingObstacle){  //Not an else so that it can happen when entering avoidingObstacle mode
			if (rc.getLocation().distanceSquaredTo(dest) < initialDistFromDest){
				avoidingObstacle = false;
				currentDirection = rc.getLocation().directionTo(dest);
			} 
			rc.setIndicatorString(2, "Checking if I can move");
			while (!model.canMoveInDirection(currentDirection)){
				currentDirection = currentDirection.rotateRight();
			}
			//Make sure I'm following the wall on my left side
			Direction leftDir = currentDirection.rotateLeft().rotateLeft();
			if (model.canMoveInDirection(leftDir)){
				currentDirection = leftDir;
			}
			rc.setIndicatorString(2, "Found good direction: "+currentDirection.toString());
		}
		
		rc.setIndicatorString(0, "Moving in direction "+currentDirection.toString());
		rc.setIndicatorString(1, "avoidingObstacle: "+(avoidingObstacle ? "true" : "false"));
		mover.move(currentDirection);
		return false;
	}

	private void determineMission() throws GameActionException {
		if (model.myMissionAssignment.get() == null)
			mission = Mission.Sentry;
		else
			mission = model.myMissionAssignment.get();
	}

}
