package gridworld;

public class Cell {
	public double reward;
	public double utility;
	public boolean wall;
	public int action;
	
	public static enum Action {
		LEFT, UP, RIGHT, DOWN
	}
	
	public Cell() {
		this.reward = 0;
		this.utility = 0;
		this.wall = false;
	}
	
	public Cell(double reward, double utility, boolean isWall) {
		this.reward = reward;
		this.utility = utility;
		this.wall = isWall;
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
