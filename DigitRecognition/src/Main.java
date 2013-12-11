
public class Main {

	public static void main(String[] args) {
		boolean testWithBinaryFeatures = false;
		Reading r = new Reading(0);
		r.readData(testWithBinaryFeatures);
		Training tr = new Training(r.images, r.digits, 500, 0);
		tr.trainData(5, false);
//		tr.printTrainData();
		
//		tr.digits.get(0).printWeights();
		
		Testing te = new Testing(tr);
		te.testData(testWithBinaryFeatures);
		te.printResults();

		System.out.println("DONE");
	}
}