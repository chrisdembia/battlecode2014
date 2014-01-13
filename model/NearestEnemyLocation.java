package team139.model;

import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotInfo;
import team139.utils.CacheVariable;

public class NearestEnemyLocation extends CacheVariable<MapLocation> {

	public NearestEnemyLocation(Model m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected MapLocation calculate() {
		int minDist = Integer.MAX_VALUE;
		MapLocation closestLoc = m.nearbyEnemyInfos.get()[0].location;
		for (RobotInfo info : m.nearbyEnemyInfos.get()){
			if (m.rc().getLocation().distanceSquaredTo(info.location) < minDist)
				closestLoc = info.location;
		}
		return closestLoc;
	}

}
