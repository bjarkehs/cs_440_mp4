import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Reading {
	public List<Image> images;
	public List<Digit> digits;

	public Reading(int randomizeWeights) {
		images = new ArrayList<Image>();
		digits = new ArrayList<Digit>();
		for (int i = 0; i < 10; i++) {
			digits.add(new Digit(i, randomizeWeights));
		}
	}
	
	public void readData(boolean binaryFeatures) {
		try {
			File imgFile = new File("digitdata"+File.separator+"trainingimages");
			BufferedReader imgInput = new BufferedReader(new FileReader(imgFile));
			File labelFile = new File("digitdata"+File.separator+"traininglabels");
			BufferedReader labelInput = new BufferedReader(new FileReader(labelFile));
			String line;
			int k = 0;
			int[][] tmpImage = new int[28][28];
			int label = -1;
			while((line = imgInput.readLine()) != null) {
				if (k == 0) {
					tmpImage = new int[28][28];
					label = Integer.parseInt(labelInput.readLine());
					digits.get(label).samples++;
				}
				for (int j = 0; j < 28; j++) {
					if (binaryFeatures) {
						if (line.charAt(j) != ' ') {
							tmpImage[k][j] = 1;
						}
					} else {
						if (line.charAt(j) == '+') {
							tmpImage[k][j] = 1;
						} else if (line.charAt(j) == '#') {
							tmpImage[k][j] = 2;
						}
					}
				}
				k = (k+1) % 28;
				if (k == 0) {
					images.add(new Image(label, tmpImage));
				}
			}
			imgInput.close();
			labelInput.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
