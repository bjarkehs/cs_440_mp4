package gridworld;

public class Cell {
	public double reward;
	public double utility;
	public double newUtility;
	public boolean wall;
	public boolean goal;
	public Action action;
	
	public static enum Action {
		LEFT, UP, RIGHT, DOWN
	}
	
	public Cell() {
		this.reward = 0;
		this.utility = 0;
		this.newUtility = 0;
		this.wall = false;
		this.goal = false;
	}
	
	public static String actionToString(Action ac) {
		String result = null;
		if (ac == Action.LEFT) {
			result = "←";
		} else if (ac == Action.UP) {
			result = "↑";
		} else if (ac == Action.RIGHT) {
			result = "→";
		} else if (ac == Action.DOWN) {
			result = "↓";
		}
		return result;
	}
}
