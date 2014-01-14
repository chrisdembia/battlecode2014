package team139.utils;

import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

/**
 * A function that accepts or declines particular types of robots.
 * 
 */
public abstract class RobotFilter {

  public abstract boolean accept(RobotInfo robot);
  
  /**
   * @return an array of items that match the filter.
   */
  public static RobotInfo[] filter(RobotInfo[] robots, RobotFilter filter) {
    int count = 0;
    for (int i=0; i<robots.length; i++) {
      count +=  filter.accept(robots[i]) ? 1 : 0;
    }
    RobotInfo[] matches = new RobotInfo[count];
    int m = 0;
    for (int i=0; i<robots.length; i++) {
      if (filter.accept(robots[i])) {
        matches[m++] = robots[i];
      }
    }
    return matches;
  }
  
  /**
   * Looks for robots of a particular type and team.
   * 
   * to match all types or all teams, pass in null for that argument.
   */
  public static class is extends RobotFilter {
    private final RobotType type;
    private final Team team;
    
    public is(RobotType type, Team team) { 
      this.team = team; 
      this.type = type;
    }
    
    public boolean accept(RobotInfo robot) { 
      return (type == null || robot.type == type) && (team == null || robot.team == team); 
    }
  }
  
  public static class isNot extends RobotFilter {
    private final RobotType type;
    private final Team team;
    
    public isNot(RobotType type, Team team) { 
      this.team = team; 
      this.type = type;
    }
    
    public boolean accept(RobotInfo robot) { 
      return (type == null || robot.type != type) && (team == null || robot.team != team); 
    }
  }
  
  /**
   * Looks for wounded robots.
   */
  public static class wounded extends RobotFilter {
    private final Team team;  // required
    private final double threshold; // required
    private RobotType type = null; // optional
    
    public wounded(Team team, double threshold) {
      this.team = team;
      this.threshold = threshold;
    }
    
    public wounded(Team team, double percent_wounded_thresh, RobotType type) {
      this.team = team;
      this.threshold = percent_wounded_thresh;
      this.type = type;
    }
    
    public boolean accept(RobotInfo robot) {
      return robot.team == team && robot.health <= threshold*robot.type.maxHealth && (type == null || robot.type == type);
    }
  }
  
  
  /**
   * Looks for units adjacent to the current position.
   */
  public static class adjacent extends RobotFilter {
    private final MapLocation loc;
    private final Team team;
    
    public adjacent(MapLocation loc, Team team) {
      this.loc = loc;
      this.team = team;
    }
    
    @Override
    public boolean accept(RobotInfo robot) {
      return robot.team == team && robot.location.isAdjacentTo(loc);
    }
  }
  
  
  public static class onOrAdjacent extends RobotFilter {
    private final MapLocation loc;
    private final Team team;
    
    public onOrAdjacent(MapLocation loc, Team team) {
      this.loc = loc;
      this.team = team;
    }
    
    @Override
    public boolean accept(RobotInfo robot) {
      return robot.team == team && (robot.location.isAdjacentTo(loc) || robot.location.equals(loc));
    }
  }
}