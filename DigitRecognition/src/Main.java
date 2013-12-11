
public class Main {

	public static void main(String[] args) {
		boolean testWithBinaryFeatures = false;
		double[] bias = new double[10];
		bias[0] = 1;
		bias[1] = 0.5;
		bias[2] = 2;
		bias[3] = 3;
		bias[4] = 3.3;
		bias[5] = 2.1;
		bias[6] = 1.6;
		bias[7] = 1.8;
		bias[8] = 1.9;
		bias[9] = 5;
		
		Reading r = new Reading(40);
		r.readData(testWithBinaryFeatures);
		Training tr = new Training(r.images, r.digits, 1000, bias);
		tr.trainData(5, true);
//		tr.printTrainData();
		
//		tr.digits.get(0).printWeights();
		
		Testing te = new Testing(tr);
		te.testData(testWithBinaryFeatures);
		te.printResults();

		System.out.println("DONE");
	}
}