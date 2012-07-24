package battlechallenge.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import battlechallenge.settings.Config;

public class ImageExporter {
	
	public static final String DEFAULT_GAME_DIR = "games" + Config.sep;

	private File saveDir;
	
	private String saveDirFilename;
	
	public String getSaveDirFilename() {
		return saveDirFilename;
	}


	public ImageExporter(String saveDirFilename) {
		this.saveDir = new File(saveDirFilename);
		if (!this.saveDir.exists()) {
			this.saveDir.mkdirs(); 
			this.saveDirFilename = saveDirFilename;
		}
	}
	
	public void saveNewImage(int turnCount, BufferedImage img) {
		this.saveNewImage(saveDir.getAbsoluteFile()+ Config.sep + turnCount + ".jpg", img);
	}
	
	private void saveNewImage(String filename, BufferedImage img) {
		try {
		File f = new File(filename);
		ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			System.out.println("Failed to write");
		}
	}
}
