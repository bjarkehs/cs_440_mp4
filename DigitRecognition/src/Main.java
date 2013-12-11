
public class Main {

	public static void main(String[] args) {
		boolean testWithBinaryFeatures = false;
		Reading r = new Reading(40);
		r.readData(testWithBinaryFeatures);
		Training tr = new Training(r.images, r.digits, 1000, 0);
		tr.trainData(5, true);
//		tr.printTrainData();
		
//		tr.digits.get(0).printWeights();
		
		Testing te = new Testing(tr);
		te.testData(testWithBinaryFeatures);
		te.printResults();

		System.out.println("DONE");
	}
}