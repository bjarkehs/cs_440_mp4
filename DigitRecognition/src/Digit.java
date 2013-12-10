
public class Digit {
	public int[][] feature;
	public int samples;
	public int tests;
	public int label;
	public double[][] weight;
	
	public Digit(int l, int randomizeWeights) {
		this.label = l;
		this.feature = new int[28][28];
		this.weight = new double[28][28];
		this.samples = 0;
		this.tests = 0;
		if (randomizeWeights != 0) {
			for (int i = 0; i < weight.length; i++) {
				for (int j = 0; j < weight[0].length; j++) {
					weight[i][j] = (Math.random()*(randomizeWeights*2))-randomizeWeights;
				}
			}
		}
	}
	
	public void printWeights() {
		for (int i = 0; i < weight.length; i++) {
			for (int j = 0; j < weight[0].length; j++) {
				System.out.println(weight[i][j]);
			}
		}
	}
}
