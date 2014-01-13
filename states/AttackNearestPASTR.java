/**
 * 
 */
package team139.states;

import team139.controller.Controller;
import battlecode.common.GameActionException;

/**
 * @author fitze
 *
 */
public class AttackNearestPASTR extends State {
	

	public AttackNearestPASTR(String name, Controller con) {
		super(name, con);
		// TODO Auto-generated constructor stub
	}
	
	public Task[] dependencies() {
		
		pushTask(GoTo(NearestPASTR());
		pushTask()
		
		Task dep[]
	}

	/* (non-Javadoc)
	 * @see team139.states.State#run()
	 */
	@Override
	public void do() throws GameActionException {
		// TODO Auto-generated method stub
		while (!dependency.complete()) {
			
		}
			
		loc = sense...
		rc.attack(loc);
			
			
		
		
	}
	
	public boolean complete() {
		numNearestEnemyPastrs() == 0;
	}

}
