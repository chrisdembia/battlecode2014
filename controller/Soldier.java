/**
 * 
 */
package team139.controller;

import team139.actions.Attacker;
import team139.actions.Mover;
import team139.states.SoldierExample;
import team139.utils.Util;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 *
 */
public class Soldier extends Controller {

	public final Attacker attacker;
	public final Mover mover;

	/**
	 * @param rc
	 */
	public Soldier(RobotController rc) {
		super(rc);
		this.attacker = new Attacker(rc);
		this.mover = new Mover(rc);
		addState(new SoldierExample("soldier_ex", this));
	}

	@Override
	protected void decideState() throws GameActionException {
		setActiveState("soldier_ex");
		/*
		
		addState(new GoTo("goto_enemy_hq", rc.senseEnemyHQLocation()));
		setActiveState("goto_enemy_hq");
		addState(new AttackNearbyEnemies("a1"));
		
		addState(new AttackLocation)
		*/
		
		pushState(AttackNearestPASTR());
		
		/*
		if (killnearbyenemies() {return;}
		if (gotonearestpasture())) {return;}
		if (attacknearestpasture() ){return}
		if (gotoenemyhq()) {return} 
		
		pushState(GoToEnemyHQ());
		pushState()
		
		goto nearest pasture
		attack nearest pasture
		
		pushState(new DestroyNearestPASTR());
		
		if nearbyEnemies() setActiveState(attacknearbyenemies())
		*/
		
	}
	
	
	
	

}
