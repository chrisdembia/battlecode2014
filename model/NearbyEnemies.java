package team139.model;

import battlecode.common.Robot;
import team139.utils.CacheVariable;

public class NearbyEnemies extends CacheVariable<Robot[]> {

	public NearbyEnemies(Model m) {
		super(m);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Robot[] calculate() {
		return m.rc().senseNearbyGameObjects(Robot.class,
				10,
				m.rc().getTeam().opponent());
	}

}
