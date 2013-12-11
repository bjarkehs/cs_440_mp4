import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Training {
	public List<Digit> digits;
	public List<Image> images;
	public int alphaParameter;
	public int[][] hitMatrix;
	public double[][] confusionMatrix;
	public List<Integer> epochAccuracy;
	public double[] b;
	
	public Training(List<Image> images, List<Digit> digits, int alphaParameter, double[] bias) {
		this.alphaParameter = alphaParameter;
		this.images = images;
		this.digits = digits;
		if (bias == null) {
			bias = new double[10];
		}
		this.b = bias;
		epochAccuracy = new ArrayList<Integer>();
	}
	
	public void trainData(int epoch, boolean randomizeImages) {
		for (int step = 1; step <= epoch; step++) {
			List<Image> listOfImages = new ArrayList<Image>(images);
			if (randomizeImages) {
				Collections.shuffle(listOfImages);
			}
			int totalIncorrect = 0;
			hitMatrix = new int[10][10];
			confusionMatrix = new double[10][10];
			Image img = null;
			while (listOfImages.size() > 0) {
				img = listOfImages.remove(0);
//				img.printImage();
				double maxC = Double.NEGATIVE_INFINITY;
				int maxLabel = -1;
				for (int k = 0; k < digits.size(); k++) {
					double c = 0;
					Digit d = digits.get(k);
					for (int i = 0; i < img.features.length; i++) {
						for (int j = 0; j < img.features[0].length; j++) {
							c += d.weight[i][j] * img.features[i][j] + b[k];
						}
					}
					if (c > maxC) {
						maxC = c;
						maxLabel = d.label;
					}
				}
				if (maxLabel != img.label) {
					// Incorrect classification
					totalIncorrect++;
					Digit guessed = digits.get(maxLabel);
					Digit correct = digits.get(img.label);
					for (int i = 0; i < img.features.length; i++) {
						for (int j = 0; j < img.features[0].length; j++) {
							correct.weight[i][j] = correct.weight[i][j] + getAlpha(step) * img.features[i][j];
							guessed.weight[i][j] = guessed.weight[i][j] - getAlpha(step) * img.features[i][j];
						}
					}
				}
				hitMatrix[img.label][maxLabel]++;
			}
			calculateConfusionMatrix();
			printEpoch(step);
			epochAccuracy.add(images.size()-totalIncorrect);
		}
		printResult(epoch);
	}
	
	public void printResult(int epoch) {
		printTrainingCurve();
	}
	
	public void printTrainingCurve() {
		String niceOutput;
		System.out.print("|-----------");
		for (int k = 0; k < epochAccuracy.size(); k++) {
			System.out.print("---------");
		}
		System.out.println("-|");
		System.out.print("| Epoch:    ");
		for (int k = 0; k < epochAccuracy.size(); k++) {
			niceOutput = String.format("| %1$6s ", k+1);
			System.out.print(niceOutput);
		}
		System.out.println(" |");
		System.out.print("|-----------");
		for (int k = 0; k < epochAccuracy.size(); k++) {
			System.out.print("---------");
		}
		System.out.println(" |");
		System.out.print("| Accuracy: ");
		for (int i = 0; i < epochAccuracy.size(); i++) {
			int correct = epochAccuracy.get(i);
			double accuracy = (double)correct/(double)images.size()*100;
			niceOutput = String.format("| %1$5.1f", accuracy);
			System.out.print(niceOutput);
			System.out.print("% ");
		}
		System.out.println(" |");
		System.out.print("|___________");
		for (int k = 0; k < epochAccuracy.size(); k++) {
			System.out.print("_________");
		}
		System.out.println("_|");
	}
	
	public void printEpoch(int step) {
		System.out.println("Epoch no. " + step);
		printMatrix(confusionMatrix);
		System.out.println();
	}
	
	public double getAlpha(int step) {
		if (alphaParameter == 0) {
			return 1;
		} else {
			return (double)alphaParameter/(double)(alphaParameter+step);
		}
	}
	
	public void calculateConfusionMatrix() {
		for (int r = 0; r < 10; r++) {
			Digit d = digits.get(r);
			for (int c = 0; c < 10; c++) {
				confusionMatrix[r][c] = ((double)hitMatrix[r][c]/(double)d.samples)*100;
			}
		}
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
	
	public void printTrainData() {
		String niceOutput;
		System.out.println("Total Samples: " + images.size());
		for (Digit d : digits) {
			System.out.println("Label of digits: "+ d.label);
			for (int i = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++) {
	            	niceOutput = String.format("%1$3s", d.feature[i][j]);
	                System.out.print(niceOutput);
				}
				System.out.println();
			}
		}
	}	
}
