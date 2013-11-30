package gridworld;


public class Main {
	public static void main(String[] args) {

		ReadMaze rm = new ReadMaze("maze.txt", -0.04);
		MDP m = new MDP(rm.maze);
		m.printMaze();
		System.out.println();
		int i = m.valueIteration(0.99);
		m.printMaze();
		m.printPolicy();
		System.out.println("Number of iterations: " + i);
	}
}
