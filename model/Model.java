/**
 * 
 */
package team139.model;

import team139.utils.Util;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import battlecode.common.Team;

/**
 * This is how the Controller learns anything about its environment. Here, we
 * are able to cache information that may be requested multiple times. Methods
 * here always return objective observations of the environment.
 * TODO add generic caching ability.
 */
public class Model {
	
	// TODO make this static?
	private final RobotController rc;
	public final Team opponent;
	public MapLocation enemyHQLocation;

	
	// Cache variables.
	// =======================================================================
//	public NearbyEnemies nearbyEnemies;
	public NearbyEnemyInfos nearbyEnemyInfos;
	public NearestEnemyLocation nearestEnemyLocation;
	public MissionAssignmentCache myMissionAssignment;

	public Model(RobotController rc) {
		this.rc = rc;
		this.opponent = rc.getTeam().opponent();
//		this.nearbyEnemies = new NearbyEnemies(this);
		this.myMissionAssignment = new MissionAssignmentCache(this);
		this.nearbyEnemyInfos = new NearbyEnemyInfos(this);
		this.nearestEnemyLocation = new NearestEnemyLocation(this);
		this.enemyHQLocation = rc.senseEnemyHQLocation();
	}
	
	public final RobotController rc() {
		return rc;
	}
	
	public void invalidate() {
//		nearbyEnemies.invalidate();
		myMissionAssignment.invalidate();
		nearbyEnemyInfos.invalidate();
		nearestEnemyLocation.invalidate();
	}
	
	/**
	 * Convert map x, y coordinate to a linear index.
	 * @param loc
	 * @return
	 */
	public final int toLinear(final MapLocation loc) {
		return loc.y * rc.getMapWidth() + loc.x;
	}
	
	/**
	 * Convert map linear index to x, y coordinate.
	 */
	public final MapLocation fromLinear(int loc) {
		int y = loc / rc.getMapWidth();
		return new MapLocation(y - rc.getMapWidth(), y);
	}
	
	/**
	 * TODO cache getLocation()?
	 * @return
	 */
	public Direction directionToEnemyHQ() {
		return rc.getLocation().directionTo(rc.senseEnemyHQLocation());
	}
	
	/**
	 * TODO prevent throwing; make sure we can sense; can ALL objects sense
	 * their adjacent squares?
	 * 
	 * @param dir
	 * @return true if can spawn in the adjacent tile in direction dir
	 * @throws GameActionException 
	 */
	public boolean canSpawnInDirection(final Direction dir)
			throws GameActionException {
		// Location loc = rc.getLocation().add(dir)
		// if rc.canSenseSquare(loc) { ...
		return rc.senseObjectAtLocation(rc.getLocation().add(dir)) == null;
	}
	
	/**
	 * Returns whether a soldier can move in the direction (accounts for occupied or too close to HQ)
	 */
	public boolean canMoveInDirection(Direction dir){
		if (!rc.canMove(dir)) return false;
		
		MapLocation stepLoc = rc.getLocation().add(dir);
		if (stepLoc.distanceSquaredTo(enemyHQLocation) <= RobotType.HQ.attackRadiusMaxSquared) return false;
		
		return true;
	}

	/**
	 * @param loc
	 * @return Manhattan distance between this robot's location and loc
	 */
	public int myManDistanceTo(final MapLocation loc) {
		return Util.manhattanDistance(rc.getLocation(), loc);
	}

	/**
	 * 
	 * @return
	 * @throws GameActionException 
	 */
	public boolean existsNearbyEnemies() throws GameActionException {
		//return nearbyEnemies.get().length > 0;
		return nearbyEnemyInfos.get().length > 0;
	}

//	/**
//	 * TODO make into cache variable
//	 * TODO what if there are no nearby enemies?
//	 * @return
//	 * @throws GameActionException 
//	 */
//	public MapLocation firstNearbyEnemyLocation() throws GameActionException {
//		return rc.senseRobotInfo(nearbyEnemies.get()[0]).location;
//	}

	/**
	 * TODO make into cache variable.
	 * @return
	 */
	public Direction directionTowardsEnemyHQ() {
		return rc.getLocation().directionTo(rc.senseEnemyHQLocation());
	}
}
