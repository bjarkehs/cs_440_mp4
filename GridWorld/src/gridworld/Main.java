package gridworld;
import gridworld.Cell.Action;


public class Main {
	public static void main(String[] args) {

		ReadMaze rm = new ReadMaze("maze.txt");
		rm.printMaze();
		System.out.println(Cell.actionToString(Action.RIGHT));
	}
}
