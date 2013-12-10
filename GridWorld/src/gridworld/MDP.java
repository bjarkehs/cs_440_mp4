package gridworld;

//import java.math.BigDecimal;
//import java.math.MathContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import gridworld.Cell.Action;

public class MDP {
	public Cell[][] maze;
	public int iterations;
	public double convFactor = 0.000001;
	public List<Double[][]> listOfIterations = new ArrayList<Double[][]>();
	
	public MDP(Cell[][] maze) {
		this.maze = maze;
	}
	
	public int valueIteration(double gamma) {
		iterations = 0;
		while(true) {
			iterations++;
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
//					System.out.println("Location: " + i + "," + j);
					if (shouldSkip(maze[i][j])) {
						continue;
					}
					double max = Double.NEGATIVE_INFINITY;
					for (Action ac : Action.values()) {
						double sum = 0;
						sum = getSumOfDirection(i, j, ac);
//						System.out.println(ac + " is: " + sum);
						if (sum > max) {
							max = sum;
							maze[i][j].action = ac;
//							System.out.println("Action: " +ac);
//							System.out.println("Sum: " +sum);
						}
					}
					maze[i][j].newUtility = maze[i][j].reward + gamma*max;
				}
			}
			double error = 0;
			Double[][] utilities = new Double[maze.length][maze[0].length];
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[i].length; j++) {
					if (shouldSkip(maze[i][j])) {
						continue;
					}
					error += Math.pow((maze[i][j].newUtility-maze[i][j].utility), 2);
					maze[i][j].utility = maze[i][j].newUtility;
					utilities[i][j] = maze[i][j].utility;
				}
			}
			listOfIterations.add(utilities);
			error = Math.sqrt(error);
			System.out.println("ERROR = "+error);
			
			if (error <= convFactor) {
				break;
			}
		}
		return iterations;
	}
	
	private boolean shouldSkip(Cell c) {
		if (c.wall || c.goal) {
//		if (c.wall) {
			return true;
		}
		return false;
	}
	
	private double getSumOfDirection(int i, int j, Action ac) {
		double sum = 0;
		if (ac == Action.LEFT) {
			sum += getUtilityForDirection(i, j, Action.LEFT)*0.8; // LEFT
			sum += getUtilityForDirection(i, j, Action.UP)*0.1; // UP
			sum += getUtilityForDirection(i, j, Action.DOWN)*0.1; // DOWN
		}
		if (ac == Action.RIGHT) {
			sum += getUtilityForDirection(i, j, Action.RIGHT)*0.8; // RIGHT
			sum += getUtilityForDirection(i, j, Action.UP)*0.1; // UP
			sum += getUtilityForDirection(i, j, Action.DOWN)*0.1; // DOWN
		}
		if (ac == Action.UP) {
			sum += getUtilityForDirection(i, j, Action.UP)*0.8; // UP
			sum += getUtilityForDirection(i, j, Action.RIGHT)*0.1; // RIGHT
			sum += getUtilityForDirection(i, j, Action.LEFT)*0.1; // LEFT
		}
		if (ac == Action.DOWN) {
			sum += getUtilityForDirection(i, j, Action.DOWN)*0.8; // DOWN
			sum += getUtilityForDirection(i, j, Action.RIGHT)*0.1; // RIGHT
			sum += getUtilityForDirection(i, j, Action.LEFT)*0.1; // LEFT
		}
		return sum;
	}
	
	private double getUtilityForDirection(int i, int j, Action ac) {
		int endI = i;
		int endJ = j;
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
		int[] rowAndCol = getState(i, j, endI, endJ);
		return getUtility(rowAndCol[0], rowAndCol[1]);
	}
	
	private int[] getState(int startI, int startJ, int endI, int endJ) {
		int[] result = new int[2];
		result[0] = endI;
		result[1] = endJ;
		if (endI < 0 || endI > 5 || endJ < 0 || endJ > 5 || maze[endI][endJ].wall) {
			result[0] = startI;
			result[1] = startJ;
		}
		return result;
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
					niceOutput = String.format("%1$5s", Cell.actionToString(maze[i][j].action));
				}
                System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void printReport() {
		System.out.println("Printing the values as '(row, column): utility'");
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (shouldSkip(maze[i][j])) {
					continue;
				}
				System.out.println("("+i+", "+j+"): "+ maze[i][j].utility);
			}
		}
	}
	
	public void printReferenceLikeReport() {
		System.out.println("Printing the values as '(column, row): utility' as the reference values");
		for (int j = 0; j < maze[0].length; j++) {
			for (int i = 0; i < maze.length; i++) {
				if (shouldSkip(maze[i][j])) {
					continue;
				}
				System.out.println("("+j+", "+i+"): "+ maze[i][j].utility);
			}
		}
	}
	
	public void printResults() {
		printMaze();
		printPolicy();
		System.out.println("Number of iterations: " + iterations);
//		printReport();
		printReferenceLikeReport();
		createMatLabFile();
	}
	
	public void createMatLabFile() {
		try {
			PrintWriter writer = new PrintWriter("valueIter.txt", "UTF-8");
			for (int n = 0; n < listOfIterations.size(); n++) {
				Double[][] utilities = listOfIterations.get(n);
				for (int i = 0; i < utilities.length; i++) {
					for (int j = 0; j < utilities[i].length; j++) {
						if (shouldSkip(maze[i][j])) {
							continue;
						}
						writer.print(utilities[i][j]);
						writer.print(' ');
					}
				}
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public double[][] getUtilities() {
		double[][] utilities = new double[maze.length][maze[0].length];
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (shouldSkip(maze[i][j])) {
					continue;
				}
				utilities[i][j] = maze[i][j].utility;
			}
		}
		return utilities;
	}
}
