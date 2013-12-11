import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Testing {
	public int totalTests;
	public int totalIncorrect;
	public int accuracy;
	public int[][] hitMatrix;
	public double[][] confusionMatrix;
	public Training training;
	
	public Testing(Training t) {
		training = t;
		totalTests = 0;
		totalIncorrect = 0;
		confusionMatrix = new double[10][10];
		hitMatrix = new int[10][10];
	}
	
	public boolean testData(boolean binaryFeatures) {
		try {
			File imgFile = new File("digitdata"+File.separator+"testimages");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("digitdata"+File.separator+"testlabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			int[][] testImage = new int[28][28]; 
			String line;
			int n = 0;
			int label = -1;
			while((line = imgInput.readLine()) != null) {
				if (n == 0) {
					testImage = new int[28][28];
					label = Integer.parseInt(labelInput.readLine());
					training.digits.get(label).tests++;
					this.totalTests++;
				}
				for (int j = 0; j < 28; j++) { 
					if (binaryFeatures) {
						if (line.charAt(j) != ' ') {
							testImage[n][j] = 1;
						}
					} else {
						if (line.charAt(j) == '+') {
							testImage[n][j] = 1;
						} else if (line.charAt(j) == '#') {
							testImage[n][j] = 2;
						}
					}
				}
				n = (n+1) % 28;
				if (n == 0) {
					double maxC = Double.NEGATIVE_INFINITY;
					int maxLabel = -1;
					for (int k = 0; k < training.digits.size(); k++) {
						double c = 0;
						Digit d = training.digits.get(k);
						for (int i = 0; i < testImage.length; i++) {
							for (int j = 0; j < testImage[0].length; j++) {
								c += d.weight[i][j] * testImage[i][j] + training.b[k];
							}
						}
						if (c > maxC) {
							maxC = c;
							maxLabel = d.label;
						}
					}
					if (maxLabel != label) {
						// Incorrect classification
						totalIncorrect++;
					}
					hitMatrix[label][maxLabel]++;
				}
			}
			accuracy = totalTests-totalIncorrect;
			calculateConfusionMatrix();
			imgInput.close();
			labelInput.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void printResults() {
		System.out.println();
		System.out.println("Confusion matrix is:");
		printMatrix(confusionMatrix);
		
		System.out.println();
		double performance = (double)accuracy/(double)totalTests*100;
		System.out.println("Total performance is: "+performance+"%");
	}
	
	public void printMatrix(double[][] matrix) {
		String niceOutput;
		System.out.print("   ");
		for (int k = 0; k < matrix[0].length; k++) {
			niceOutput = String.format("%1$6s", k);
			System.out.print(niceOutput);
		}
		System.out.println();
		for (int i = 0; i < matrix.length; i++) {
			niceOutput = String.format("%1$2s ", i);
			System.out.print(niceOutput);
			for (int j = 0; j < matrix[i].length; j++) {
				niceOutput = String.format("%1$6.1f", matrix[i][j]);
				System.out.print(niceOutput);
			}
			System.out.println();
		}
	}
	
	public void calculateConfusionMatrix() {
		for (int r = 0; r < 10; r++) {
			Digit d = training.digits.get(r);
			for (int c = 0; c < 10; c++) {
				confusionMatrix[r][c] = ((double)hitMatrix[r][c]/(double)d.tests)*100;
			}
		}
	}
}
