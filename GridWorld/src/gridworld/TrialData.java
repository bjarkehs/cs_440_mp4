package gridworld;

public class TrialData {
	public double rmse;
	public double[][] utilities;
	
	public TrialData(double rmse, double[][] utilities) {
		this.rmse = rmse;
		this.utilities = utilities;
	}
}
