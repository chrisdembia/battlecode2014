/**
 * 
 */
package team139.model;

import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;

/**
 *
 */
public abstract class Message {

	/**
	 * Returns null instead of 0 if the channel is unset.
	 * @param rc
	 * @param channel
	 * @return
	 * @throws GameActionException 
	 */
	public static Integer readBroadcast(RobotController rc, int channel)
			throws GameActionException {
		if (channel > GameConstants.BROADCAST_MAX_CHANNELS) {
			System.out.println("channel > max channels.");
		}
		int message = rc.readBroadcast(channel);
		if (message == 0) return null;
		return message - 1;
	}
	
	/**
	 * Use this to send messages to ensure that our custom
	 * `readBroadcast` can be used.
	 * @param rc
	 * @param channel
	 * @param message
	 * @throws GameActionException
	 */
	public static void broadcast(RobotController rc,
			int channel, int message) throws GameActionException {
		if (channel > GameConstants.BROADCAST_MAX_CHANNELS) {
			System.out.println("channel > max channels.");
		}
		rc.broadcast(channel, message + 1);
	}

}
