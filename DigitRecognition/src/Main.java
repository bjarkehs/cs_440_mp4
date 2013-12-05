
public class Main {

	public static void main(String[] args) {
		Reading r = new Reading(false);
		r.readData();
		Training tr = new Training(r.images, r.digits, 1);
		tr.trainData(5, false);
		//tr.printTrainData();
		
		Testing te = new Testing(tr);
		te.testData();
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