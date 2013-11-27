package gridworld;

import gridworld.Cell.Action;

public class MDP {
	public Cell[][] maze;
	
	public MDP(Cell[][] maze) {
		this.maze = maze;
	}
	
	public int valueIteration(double gamma) {
		int iter = 1;
		while(true) {
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
					if (maze[i][j].wall) {
						continue;
					}
					double max = Integer.MIN_VALUE;
					for (Action ac : Action.values()) {
						double sum = 0;
						if (ac == Action.LEFT) {
							sum += getUtility(i, j-1)*0.8; // LEFT
							sum += getUtility(i-1, j)*0.1; // UP
							sum += getUtility(i+1, j)*0.1; // DOWN
						}
						if (ac == Action.RIGHT) {
							sum += getUtility(i, j+1)*0.8; // RIGHT
							sum += getUtility(i-1, j)*0.1; // UP
							sum += getUtility(i+1, j)*0.1; // DOWN
						}
						if (ac == Action.UP) {
							sum += getUtility(i-1, j)*0.8; // UP
							sum += getUtility(i, j+1)*0.1; // RIGHT
							sum += getUtility(i, j-1)*0.1; // LEFT
						}
						if (ac == Action.DOWN) {
							sum += getUtility(i+1, j)*0.8; // DOWN
							sum += getUtility(i, j+1)*0.1; // RIGHT
							sum += getUtility(i, j-1)*0.1; // LEFT
						}
						if (sum > max) {
							max = sum;
							maze[i][j].action = ac;
						}
					}
					maze[i][j].newUtility = maze[i][j].reward + gamma*max;
				}
			}
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
					maze[i][j].utility = maze[i][j].newUtility;
				}
			}
			iter++;
			if (iter > 100) {
				break;
			}
		}
		return iter;
	}
	
	private double getUtility(int i, int j) {
		if (i < 0 || i > 5) {
			return 0;
		}
		if (j < 0 || j > 5) {
			return 0;
		}
		if (maze[i][j].wall) {
			return 0;
		}
		return maze[i][j].utility;
	}
	
	public void printMaze() {
		String niceOutput;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j].wall) {
					niceOutput = String.format("%1$8s", "%%%");
				} else {
					niceOutput = String.format("%1$8.3f", maze[i][j].utility);
				}
                System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void printPolicy() {
		String niceOutput;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j].wall) {
					niceOutput = String.format("%1$4s", "%");
				} else if (maze[i][j].goal) {
					niceOutput = String.format("%1$4.0f", maze[i][j].reward);
				} else {
					niceOutput = String.format("%1$4s", Cell.actionToString(maze[i][j].action));
				}
                System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
}
