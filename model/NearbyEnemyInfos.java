package team139.model;

import battlecode.common.GameActionException;
import battlecode.common.Robot;
import battlecode.common.RobotInfo;
import team139.utils.CacheVariable;

public class NearbyEnemyInfos extends CacheVariable<RobotInfo[]> {

	public NearbyEnemyInfos(Model m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected RobotInfo[] calculate() {
		RobotInfo[] infos = new RobotInfo[m.nearbyEnemies.get().length];
		for (int i=0; i<m.nearbyEnemies.get().length; i++){
			try {
				infos[i] = m.rc().senseRobotInfo(m.nearbyEnemies.get()[i]);
			} catch (GameActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				m.rc().breakpoint();
			}
		}
		return infos;
	}

}
