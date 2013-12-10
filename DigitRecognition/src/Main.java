
public class Main {

	public static void main(String[] args) {
		boolean testWithBinaryFeatures = false;
		Reading r = new Reading(0);
		r.readData(testWithBinaryFeatures);
		Training tr = new Training(r.images, r.digits, 500);
		tr.trainData(5, false);
		//tr.printTrainData();
		
//		tr.digits.get(0).printWeights();
		
		Testing te = new Testing(tr);
		te.testData(testWithBinaryFeatures);
		te.printResults();
//		
//		ImageWindow i = new ImageWindow();
//		i.printImage(te.getLikelyhood(4));
//		i.printImage(te.getLikelyhood(9));
//		i.printImage(te.getOddsRatio(4,9));
//		te.printLikelyhood(4);
//		te.printLikelyhood(9);
//		te.printOddsRatio(4,9);
		
		System.out.println("DONE");
	}
}