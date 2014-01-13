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
public class MissionAssignment {
	
	public Mission mission;
	
	public MissionAssignment(RobotController rc, int ID) {
		if (ID > GameConstants.BROADCAST_MAX_CHANNELS) this.mission = null;
		try {
			this.mission = Mission.values()[rc.readBroadcast(ID)];
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void broadcast(RobotController rc, int ID, Mission mission)
			throws GameActionException {
		rc.broadcast(ID, mission.ordinal());
	}
}
