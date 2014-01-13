//package team139.model;
//
//import battlecode.common.Robot;
//import team139.utils.CacheVariable;
//
//public class NearbyEnemies extends CacheVariable<Robot[]> {
//
//	int sensorRadius;
//	public NearbyEnemies(Model m) {
//		super(m);
//		sensorRadius = (int) Math.sqrt(m.rc().getType().sensorRadiusSquared);
//	}
//
//	@Override
//	protected Robot[] calculate() {
//		return m.rc().senseNearbyGameObjects(Robot.class,
//				sensorRadius,
//				m.rc().getTeam().opponent());
//	}
//
//}
