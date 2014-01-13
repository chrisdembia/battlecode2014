package team139.model;

import battlecode.common.GameActionException;
import battlecode.common.Robot;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team139.utils.CacheVariable;

public class NearbyEnemyInfos extends CacheVariable<RobotInfo[]> {

	private int sensorRadius;

	public NearbyEnemyInfos(Model m) {
		super(m);
		sensorRadius = (int) Math.sqrt(m.rc().getType().sensorRadiusSquared);
	}
	
	@Override
	protected RobotInfo[] calculate() throws GameActionException {
		Robot[] allRobots = m.rc().senseNearbyGameObjects(Robot.class,
				sensorRadius,
				m.rc().getTeam().opponent());
		RobotInfo[] infos = new RobotInfo[allRobots.length];
		int numAttackableEnemies = 0;
		for (int i=0; i<allRobots.length; i++){
			RobotInfo info = m.rc().senseRobotInfo(allRobots[i]);
			if (info.type == RobotType.NOISETOWER || info.type==RobotType.SOLDIER){
				infos[i] = info;
				numAttackableEnemies++;
			}
			else{
				infos[i] = null;
			}
		}
		int j = 0;
		RobotInfo[] attackableInfos = new RobotInfo[numAttackableEnemies];
		for (int i=0; i<allRobots.length; i++){
			if (infos[i] != null){
				attackableInfos[j++] = infos[i];
			}
		}
		
		return attackableInfos;
	}

}
