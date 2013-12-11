package gridworld;


public class Main {
	public static void main(String[] args) {

		ReadMaze rm = new ReadMaze("maze.txt", -0.04);
		MDP m = new MDP(rm.maze);
		m.printMaze();
		System.out.println();
		m.valueIteration(0.99);
		m.printResults();
		System.out.println("DONE with Value Iteration");
		double[][] trueUtilities = m.getUtilities();
		
		QReadMaze qrm = new QReadMaze("maze.txt", -0.04);
		int Ne = 15;
		double Rplus = 80;
		int alphaValue = 160;
		QLearning qm = new QLearning(qrm.maze, qrm.startCell, Ne, Rplus, alphaValue, trueUtilities);
		qm.runAlgorithm(0.99);
		qm.printResults();
		System.out.println("DONE with Q-Learning");
	}
}
