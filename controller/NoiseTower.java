/**
 * 
 */
package team139.controller;

import team139.controller.Soldier.Mission;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.TerrainTile;

/**
 *
 */
public class NoiseTower extends Controller {

	private MapLocation pastrLocation;
	private int maxAttackRadius = (int)Math.sqrt(rc.getType().attackRadiusMaxSquared);
	private int minAttackRadius = 6;
	private final int LIGHT_ATTACK_THRESH = 6;
	private double x;
	private double y;
	private boolean exit;

	private MapLocation target;	

	//TODO: Test out the puller strategy (instead of spiralling)
	//TODO: Advanced puller: use pathfinding to push large groups of cows directly to pastr through map
	//TODO: Avoid pushing cows into enemy PASTRS (don't shoot if PASTR is in affected range?)

	public static enum Mission {
		Spiraller,  //Spiral shots in towards pastr
		Puller,		//Pull in straight lines towards pastr
		AdvancedPuller,  //Pull in cows using pathfinding through map
		Disrupter	//Shoot at enemy pastrs to prevent them from gaining cows
	}

	private Mission mission;

	/**
	 * @param rc
	 */
	public NoiseTower(RobotController rc) {
		super(rc);
		mission = Mission.Spiraller;
		pastrLocation = rc.getLocation().add(Direction.EAST);
	}

	/* (non-Javadoc)
	 * @see team139.controller.Controller#takeOneTurn()
	 */
	@Override
	public void takeOneTurn() throws GameActionException {

		switch (mission){

		case Spiraller:
			exit = false;
			while (!exit){
				if (!rc.isActive()) return;
				double rInit = Math.min(maxAttackRadius, Math.min(rc.getMapWidth(), rc.getMapHeight()));
				double dR = -.06;
				double dTheta = .18; 
				double theta = -dTheta;			
				boolean offMap = false;
				double r=rInit;
				//Loop through decreasing outer circles
				while (r >= minAttackRadius || theta > dTheta){
					//determine change in angle for this circle
					//double thetaDot = ((double)stepSize)/((double)r);
					r += dR;
					theta += dTheta;
					if (theta > 2*Math.PI){
						theta -= 2*Math.PI;
					}
					//dTheta += ddTheta;

					//Loop through a single circle
					while (!rc.isActive()) this.yield();
					rc.setIndicatorString(1, "Ready to fire");

					//update to new target
					x = pastrLocation.x + r*Math.cos(theta);
					y = pastrLocation.y + r*Math.sin(theta);
					target = new MapLocation((int)Math.floor(x), (int)Math.floor(y));
					rc.setIndicatorString(2, "Target Selected: "+target.toString());

					// If we just went off the map, shoot one at the edge
					if (rc.senseTerrainTile(target) == TerrainTile.OFF_MAP){
						if (offMap){
							continue;
						}
						else{
							offMap = true;
							target = new MapLocation(Math.min(rc.getMapWidth()-1,Math.max(0,(int)Math.round(x))),
									Math.min(rc.getMapHeight()-1,Math.max(0,(int)Math.round(y))));
						}
					}
					else{
						offMap = false;
					}

//					rc.setIndicatorString(2, "Checking Square");
					if (rc.canAttackSquare(target)){
						rc.setIndicatorString(0, "Attacking");
						//Attack! (with noise)
						if (r > LIGHT_ATTACK_THRESH){
							rc.attackSquare(target);
						}
						else{
							rc.attackSquareLight(target);
						}

						//yield!
						this.yield();
					}
					else {
						rc.setIndicatorString(0, "Couldnt attack there");
					}
				} //Radius for loop
			} //while true	
			break;

		case Puller:
			exit = false;
			//while (!exit){
				//Tunable parameters:
				double initialAngle = 45; //degrees TODO: convert this to rads outside here to save computation
				double rotationAngle = 45/2; //degrees TODO: convert this to rads outside here to save computation
				double pullDist = 2;
				
				//computed parameters
				double theta = initialAngle*Math.PI/180;
				double dTheta = rotationAngle*Math.PI/180;
				//double r = maxAttackRadius;
				
				while (!exit){
					
					
					for (double r=maxAttackRadius; r>=minAttackRadius-2; r -= pullDist){
						while (!rc.isActive()) yield();
						x = pastrLocation.x + r*Math.cos(theta);
						y = pastrLocation.y + r*Math.sin(theta);
						target = new MapLocation((int)Math.floor(x),(int)Math.floor(y));
						
						if (rc.senseTerrainTile(target) != TerrainTile.OFF_MAP && rc.canAttackSquare(target)){
							rc.attackSquare(target);
							this.yield();
						}
					}
					
					theta += dTheta;
					if (theta > 2*Math.PI){
						theta -= 2*Math.PI;
					}
					
				}
								
			//}
			break;
			
		default:
			break;
		}
	} //takeOneTurn method

} //NoiseTower class
