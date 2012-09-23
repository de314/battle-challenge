package battlechallenge.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import battlechallenge.settings.Config;

public class ImageExporter {
	
	public static final String DEFAULT_GAME_DIR = "games" + Config.sep;

	private File saveDir;
	
	private String timestamp;

	private String saveDirFilename;
	
	public File getSaveDir() {
		return saveDir;
	}

	public String getTimeStamp() {
		return timestamp;
	}

	public void setSaveDir(File saveDir) {
		this.saveDir = saveDir;
	}
	
	public String getSaveDirFilename() {
		return saveDirFilename;
	}


	public ImageExporter(String timestamp) { 
		this.timestamp = timestamp;
		String saveDirFilename = DEFAULT_GAME_DIR + timestamp;
		saveDir = new File(saveDirFilename);
		if (!this.saveDir.exists()) {
			saveDir.mkdirs(); 
			this.saveDirFilename = saveDirFilename;
		}
	}
	public void deleteCurrDir() { // Done so that game doesn't need to know current game directory
		deleteCurrDir(saveDir);
	}
	
	public boolean deleteCurrDir(File dir){
		 if (dir.isDirectory()) {
		        String[] children = dir.list();
		        for (int i=0; i<children.length; i++) {
		            boolean success = deleteCurrDir(new File(dir, children[i]));
		            if (!success) {
		            	 System.out.println("Deletion failed");
		            }
		        }
		    }
		 return dir.delete();
	}
		
	public void saveNewImage(int turnCount, BufferedImage img) {
		this.saveNewImage(saveDir.getAbsoluteFile() + Config.sep + turnCount + ".jpg", img);
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
