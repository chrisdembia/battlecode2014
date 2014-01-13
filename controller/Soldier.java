/**
 * 
 */
package team139.controller;

import java.util.List;
import java.util.Random;

import team139.actions.Attacker;
import team139.actions.Mover;
import team139.model.MissionAssignment;
import team139.pathfinding.GridDStar;
import team139.pathfinding.IntCoord;
import team139.pathfinding.StaticMap;
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
	private GridDStar pathplanner;
	
	
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
		
		model.buildStaticMap();
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
//						Direction stepDirection = rc.getLocation().directionTo(rc.senseEnemyHQLocation());
//						while (!rc.canMove(stepDirection)){
//							stepDirection = stepDirection.rotateRight();
//						}
						MapLocation goal = model.enemyHQLocation.add(Direction.NORTH);
						pathplanner = new GridDStar(model.map,
								new IntCoord(rc.getLocation()),
								new IntCoord(goal));
						List<IntCoord> path = pathplanner.plan();
						for (int i=0; i<path.size(); i++){
							MapLocation nextStep = new MapLocation(path.get(i).getInts()[0], path.get(i).getInts()[1]);
							while (!rc.getLocation().equals(nextStep)){
								rc.setIndicatorString(2, nextStep.toString());
								mover.move(rc.getLocation().directionTo(nextStep));
								yield();
							}
						}
						
//						while (!rc.getLocation().equals(goal)){
//							pathplanner.updateStart(new IntCoord(rc.getLocation()));
//							List<IntCoord> path = pathplanner.plan();
//							System.out.println(path.toString());
//							if (path.isEmpty()) {
//								System.out.println("No path found.");
//							}
//							int[] nextStepCoords = path.get(2).getInts();
//							MapLocation nextStep = new MapLocation(nextStepCoords[0], nextStepCoords[1]);
//							rc.setIndicatorString(2, nextStep.toString());
//							mover.move(rc.getLocation().directionTo(nextStep));
//						}
					}
					break;
					
				}
			}
			System.out.println("I'm heading to a PASTR at: " + pastrLocation.toString());
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
//					Direction stepDirection = rc.getLocation().directionTo(pastrLocation);
//					//nextStep = rc.getLocation().add(stepDirection);
//					while (!rc.canMove(stepDirection)){
//						stepDirection = stepDirection.rotateRight();
//						//nextStep = rc.getLocation().add(stepDirection);
//					}
//					mover.move(stepDirection);
					
					
					pathplanner = new GridDStar(model.map, new IntCoord(rc.getLocation()), new IntCoord(model.enemyHQLocation.add(Direction.NORTH)));
					
					List<IntCoord> path = pathplanner.plan();
					for (int i=0; i<path.size(); i++){
						MapLocation nextStep = new MapLocation(path.get(i).getInts()[0], path.get(i).getInts()[1]);
						while (!rc.getLocation().equals(nextStep)){
							rc.setIndicatorString(2, nextStep.toString());
							mover.move(rc.getLocation().directionTo(nextStep));
							yield();
						}
					}
//					while (!rc.getLocation().equals(model.enemyHQLocation.add(Direction.NORTH))){
//						pathplanner.updateStart(new IntCoord(rc.getLocation()));
//						int[] nextStepCoords = pathplanner.plan().get(2).getInts();
//						MapLocation nextStep = new MapLocation(nextStepCoords[0], nextStepCoords[1]);
//						mover.move(rc.getLocation().directionTo(nextStep));
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
	}

	private void determineMission() throws GameActionException {
		if (model.myMissionAssignment.get() == null)
			mission = Mission.Sentry;
		else
			mission = model.myMissionAssignment.get();
	}

}
