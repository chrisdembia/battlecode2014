package team139.model;

import battlecode.common.GameActionException;
import team139.controller.Soldier.Mission;
import team139.utils.CacheVariable;

public class MissionAssignmentCache extends CacheVariable<Mission> {

	public MissionAssignmentCache(Model m) {
		super(m);
	}

	@Override
	protected Mission calculate() throws GameActionException {
		return new MissionAssignment(m.rc(), m.rc().getRobot().getID()).mission;
	}

}
