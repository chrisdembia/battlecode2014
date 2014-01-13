/**
 * 
 */
package team139.utils;

import battlecode.common.Direction;
import battlecode.common.MapLocation;

/**
 *
 */
public class Util {
	
	public static final Direction[] DIRECTIONS = {
		Direction.NORTH, Direction.NORTH_EAST,
		Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH,
		Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};

	/**
	 * @param loc0
	 * @param loc1
	 * @return abs(dx) + abs(dy)
	 */
	public static final int manhattanDistance(final MapLocation loc0,
			final MapLocation loc1) {
		final int dx = loc0.x - loc1.y;
		final int dy = loc0.y - loc1.y;
		return (dx > 0 ? dx : -dx) + (dy > 0 ? dy : -dy);
	}

}
