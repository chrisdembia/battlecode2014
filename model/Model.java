/**
 * 
 */
package team139.model;

import team139.utils.Util;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.TerrainTile;

/**
 * This is how the Controller learns anything about its environment. Here, we
 * are able to cache information that may be requested multiple times. Methods
 * here always return objective observations of the environment.
 * TODO add generic caching ability.
 */
public class Model {
	
	// TODO make this static?
	private final RobotController rc;
	
	public static TerrainTile[][] terrainMap;
	public NearbyEnemies nearbyEnemies;

	public Model(RobotController rc) {
		this.rc = rc;
		this.nearbyEnemies = new NearbyEnemies(this);
	}
	
	public final RobotController rc() {
		return rc;
	}
	
	/**
	 * TODO cache getLocation()?
	 * @return
	 */
	public Direction directionToEnemyHQ() {
		return rc.getLocation().directionTo(rc.senseEnemyHQLocation());
	}
	
	/**
	 * TODO prevent throwing; make sure we can sense; can ALL objects sense their adjacent squares?
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
	 * @param loc
	 * @return Manhattan distance between this robot's location and loc
	 */
	public int myManDistanceTo(final MapLocation loc) {
		return Util.manhattanDistance(rc.getLocation(), loc);
	}

	/**
	 * 
	 * @return
	 */
	public boolean existsNearbyEnemies() {
		return nearbyEnemies.get().length > 0;
	}

	/**
	 * TODO make into cache variable
	 * TODO what if there are no nearby enemies?
	 * @return
	 * @throws GameActionException 
	 */
	public MapLocation firstNearbyEnemyLocation() throws GameActionException {
		return rc.senseRobotInfo(nearbyEnemies.get()[0]).location;
	}

	/**
	 * TODO make into cache variable.
	 * @return
	 */
	public Direction directionTowardsEnemyHQ() {
		return rc.getLocation().directionTo(rc.senseEnemyHQLocation());
	}

	/**
	 * This calls invalidate() on all cached variables in the model.
	 * This should be called, for example, at the end of a turn (or
	 * at the beginning of a turn).
	 */
	public void invalidate() {
		nearbyEnemies.invalidate();
	}
}
