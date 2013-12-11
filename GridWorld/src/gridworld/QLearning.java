package gridworld;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import gridworld.QCell.Action;

public class QLearning {
	public QCell[][] maze;
	public int steps;
	public double convFactor = 0.000001;
	public int Ne;
	public double Rplus;
	public QCell startCell;
	public QCell currentCell;
	public int alphaValue;
	public double[][] trueUtilities;
	public List<TrialData> trials;
	
	public QLearning(QCell[][] maze, QCell start, int ne, double rplus, int alphaValue, double[][] trueUtilities) {
		this.maze = maze;
		this.Ne = ne;
		this.Rplus = rplus;
		this.startCell = start;
		this.alphaValue = alphaValue;
		this.trueUtilities = trueUtilities;
		this.trials = new ArrayList<TrialData>();
	}
	
	public int runAlgorithm(double gamma) {
		currentCell = startCell;
		steps = 0;
		while(true) {
			if (currentCell.goal) {
				// Save trial info.
				if (!currentCell.found) {
        			for (int k = 0; k < 4; k++) {
        				currentCell.qValues[k] = currentCell.reward;
        			}
        			currentCell.found = true;
				}
				saveRMSE();
				currentCell = startCell;
				if (trials.size() % 100000 == 0) {
					System.out.println(trials.size());
				}
//				if (trials.size() > 000000) {
				if (hasConverged()) {
					break;
				}
			}
			steps++;
//			System.out.println("Current state: " + currentCell.row + "," + currentCell.col);
			Action a = getNextAction(currentCell);
			QCell nextCell = getState(currentCell, a);
			currentCell.qValues[a.ordinal()] = currentCell.qValues[a.ordinal()] + getAlpha() * (currentCell.reward + gamma * getMaxQ(nextCell) - currentCell.qValues[a.ordinal()]);
//			System.out.println(currentCell.qValues[a.ordinal()]);
			currentCell = nextCell;
//			System.out.println("Current State: i: " + currentCell.row + " j: " + currentCell.col);
		}
		return 1;
	}
	
	private boolean hasConverged() {
		int oldIndex = trials.size()-2;
		int newIndex = oldIndex+1;
		if (oldIndex < 0) {
			return false;
		}

		double sum = 0;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				double oldUtility = trials.get(oldIndex).utilities[i][j];
				double newUtility = trials.get(newIndex).utilities[i][j];
				if (newUtility == 0) {
					continue;
				}
				sum += Math.pow((oldUtility-newUtility),2);
			}
		}
//		System.out.println("Sum " + sum);
		double e = Math.sqrt(sum);
//		System.out.println(e);
		if (e < convFactor) {
			return true;
		} else {
			return false;
		}
	}
	
	private void saveRMSE() {
		int n = 0;
		double sum = 0;
		double[][] utilities = new double[maze.length][maze[0].length];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				utilities[i][j] = getMaxQ(maze[i][j]);
				sum += Math.pow((utilities[i][j]-trueUtilities[i][j]),2);
				n++;
			}
		}
		double rmse = Math.sqrt(sum/n);
		TrialData t = new TrialData(rmse, utilities);
		trials.add(t);
	}
	
	private double getAlpha() {
		return (double)alphaValue/(double)((alphaValue-1)+steps);
	}
	
	private Action getNextAction(QCell currentState) {
		double max = Double.NEGATIVE_INFINITY;
		Action maxAc = null;
		for (Action ac : Action.values()) {
//			if (currentState.row == 5 && currentState.col == 4) {
//				System.out.println("here");
//			}
			double tmp = effFunction(currentState.qValues[ac.ordinal()], currentState.freqValues[ac.ordinal()]);
//			System.out.println("Tmp: " + tmp + " ac: " + ac);
			if (tmp > max) {
				max = tmp;
				maxAc = ac;
			}
		}
		currentState.freqValues[maxAc.ordinal()]++;
		return maxAc;
	}
	
	private double effFunction(double u, int n) {
		if (n < this.Ne) {
			return this.Rplus;
		} else {
			return u;
		}
	}
	
	private double getMaxQ(QCell currentState) {
		double max = Double.NEGATIVE_INFINITY;
		for (Action ac : Action.values()) {
			double tmp = currentState.qValues[ac.ordinal()];
			if (tmp > max) {
				max = tmp;
				currentState.action = ac;
			}
		}
		
		return max;
	}
	
	private QCell getState(QCell currentState, Action ac) {
		double rand = Math.random();
		Action getAc = ac;
		if (rand <= 0.8) {
			// Do nothing.....
		} else if (rand <= 0.9) {
			if (ac == Action.DOWN) {
				getAc = Action.RIGHT;
			} else if (ac == Action.RIGHT) {
				getAc = Action.UP;
			} else if (ac == Action.UP) {
				getAc = Action.LEFT;
			} else if (ac == Action.LEFT) {
				getAc = Action.DOWN;
			}
		} else {
			if (ac == Action.DOWN) {
				getAc = Action.LEFT;
			} else if (ac == Action.RIGHT) {
				getAc = Action.DOWN;
			} else if (ac == Action.UP) {
				getAc = Action.RIGHT;
			} else if (ac == Action.LEFT) {
				getAc = Action.UP;
			}
		}
		return getStateFromAction(currentState, getAc);
	}
	
	private QCell getStateFromAction(QCell currentState, Action ac) {
		int endI = currentState.row;
		int endJ = currentState.col;
		if (ac == Action.LEFT) {
			endJ--;
		}
		if (ac == Action.RIGHT) {
			endJ++;
		}
		if (ac == Action.DOWN) {
			endI++;
		}
		if (ac == Action.UP) {
			endI--;
		}
		
		if (endI < 0 || endI > 5 || endJ < 0 || endJ > 5 || maze[endI][endJ].wall) {
			return currentState;
		} else {
			return maze[endI][endJ];
		}
	}
	
	public void printMaze() {
		String niceOutput;
		double[][] utilities = trials.get(trials.size()-1).utilities;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j].wall) {
					niceOutput = String.format("%1$8s", "%%%");
				} else {
					niceOutput = String.format("%1$8.3f", utilities[i][j]);
				}
                System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void printRewards() {
		String niceOutput;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
//				if (maze[i][j].wall) {
//					niceOutput = String.format("%1$8s", "%%%");
//				} else {
					niceOutput = String.format("%1$8.3f", maze[i][j].reward);
//				}
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
					niceOutput = String.format("%1$5s", "%");
				} else if (maze[i][j].goal) {
					niceOutput = String.format("%1$5.0f", maze[i][j].reward);
				} else {
					niceOutput = String.format("%1$5s", QCell.actionToString(maze[i][j].action));
				}
                System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void printReport() {
		System.out.println("Printing the values as '(row, column): utility'");
		double[][] utilities = trials.get(trials.size()-1).utilities;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (shouldSkip(maze[i][j])) {
					continue;
				}
				System.out.println("("+i+", "+j+"): "+ utilities[i][j]);
			}
		}
	}
	
	public void printReferenceLikeReport() {
		System.out.println("Printing the values as '(column, row): utility' as the reference values");
		double[][] utilities = trials.get(trials.size()-1).utilities;
		for (int j = 0; j < maze[0].length; j++) {
			for (int i = 0; i < maze.length; i++) {
				if (shouldSkip(maze[i][j])) {
					continue;
				}
				System.out.println("("+j+", "+i+"): "+ utilities[i][j]);
			}
		}
	}
	
	public void printResults() {
		printLastCoupleRMSE();
		printMaze();
		printPolicy();
		System.out.println("Number of steps: " + steps);
		System.out.println("Number of trials: " + trials.size());
//		printReport();
		printReferenceLikeReport();
		createMatLabFile();
	}
	
	public void printLastCoupleRMSE() {
		for (int i = 99; i >= 0; i--) {
			TrialData d = trials.get(trials.size()-(1+i));
			System.out.println("RMSE: " + d.rmse);
		}
	}
	
	public void createMatLabFile() {
		try {
			PrintWriter writer = new PrintWriter("qLearn.txt", "UTF-8");
			for (int n = 0; n < trials.size(); n++) {
				TrialData td = trials.get(n);
				double[][] utilities = td.utilities;
				for (int i = 0; i < utilities.length; i++) {
					for (int j = 0; j < utilities[i].length; j++) {
						if (shouldSkip(maze[i][j])) {
							continue;
						}
						writer.print(utilities[i][j]);
						writer.print(' ');
					}
				}
				writer.println(td.rmse);
			}
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	private boolean shouldSkip(QCell c) {
		if (c.wall || c.goal) {
//		if (c.wall) {
			return true;
		}
		return false;
	}
	
}
