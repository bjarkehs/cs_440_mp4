
public class Image {
	public int[][] features;
	public int label;
	
	public Image(int label, int[][] features) {
		this.label = label;
		this.features = features;
	}
	
	public void printImage() {
		for (int i = 0; i < 28; i++) {
			for (int m = 0; m < 28; m++) {
				System.out.print(features[i][m]);
			}
			System.out.println();
		}
	}
}
