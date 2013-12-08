package gridworld;

public class QCell {
	public double reward;
	public double utility;
	public boolean wall;
	public boolean goal;
	public Action action;
	public double[] qValues;
	public int[] freqValues;
	public int row;
	public int col;
	public boolean found;
	
	public static enum Action {
		LEFT, UP, RIGHT, DOWN
	}
	
	public QCell() {
		this.reward = 0;
		this.utility = 0;
		this.wall = false;
		this.goal = false;
		this.qValues = new double[Action.values().length];
		this.freqValues = new int[Action.values().length];
		this.found = false;
		for (int i = 0; i < qValues.length; i++) {
			qValues[i] = 0;
			freqValues[i] = 0;
		}
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
