/**
 * 
 */
package team139.model;

import team139.controller.Soldier.Mission;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.RobotController;

/**
 *
 */
public class MissionAssignment extends Message {

	public Mission mission;

	/**
	 * Returns null if a message has not been sent.
	 * 
	 * @param rc
	 * @param ID
	 * @throws GameActionException
	 */
	public MissionAssignment(RobotController rc, int ID)
			throws GameActionException {
		Integer message = readBroadcast(rc, ID);
		if (message == null) {
			mission = null;
			return;
		}
		mission = Mission.values()[message];
	}

	public static void broadcast(RobotController rc, int ID, Mission mission)
			throws GameActionException {
		broadcast(rc, ID, mission.ordinal());
	}
}
