/**
 * 
 */
package team139;

import battlecode.common.RobotController;

import team139.controller.HQ;
import team139.controller.NoiseTower;
import team139.controller.PASTR;
import team139.controller.Soldier;

public class RobotPlayer {
	
	/**
	 * @param rc
	 */
	public static void run(RobotController rc) {
		
		// TODO should this be in a while loop? should we catch exceptions?
		switch (rc.getType()) {
		case HQ:
			new HQ(rc).run();
			break;
		case NOISETOWER:
			new NoiseTower(rc).run();
			break;
		case PASTR:
			new PASTR(rc).run();
			break;
		case SOLDIER:
			// TODO Allow for specialization of Soldiers, using some logic.
			new Soldier(rc).run();
			break;
		}
	}
}