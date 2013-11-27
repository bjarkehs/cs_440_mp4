
public class Cell {
	public double reward;
	public double utility;
	public boolean wall;
	public int action;
	
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
}
