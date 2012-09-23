package battlechallenge.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import battlechallenge.settings.Config;

import com.google.gdata.util.ServiceException;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import javax.imageio.ImageIO;

/**
 * Generate audio and video frames and use the {@link IMediaWriter} to
 * encode that media and write it out to a file.
 *
 */

public class GenerateVideo {
	
	private String currDirectory;
	private File f;
	
	public PriorityQueue<File> getAvailableGameFrames() {
		PriorityQueue<File> gamePictures = new PriorityQueue<File>(300, comp);
		f = new File(currDirectory);
		for (String filename: f.list()) {
			File file = new File(f.getAbsolutePath()+Config.sep+filename);
			gamePictures.offer(file);
		}
		return gamePictures;
	}
	
	static Comparator<File> comp = new Comparator<File>() { // frames are created with IDs in order, sort them this way
		  public int compare(File f1, File f2) {
		    String name1 = f1.getName().substring(0, f1.getName().length()-4);
		    String name2 = f2.getName().substring(0, f2.getName().length()-4);
		    Integer one = Integer.parseInt(name1);
		    Integer two = Integer.parseInt(name2);
		    return one.compareTo(two);
		  }
	};
	

	public GenerateVideo(String currDirectory) {
		this.currDirectory = currDirectory; // the directory where the images for this game are stored
	}
	
	public boolean genVideo() throws IOException, InterruptedException, ServiceException {
		PriorityQueue<File> gameFrames = getAvailableGameFrames();
		int numFrames = gameFrames.size();
		if (numFrames == 0) { // No Images were saved so don't create a video
			System.out.println("No game images exist, an error must have occured");
			return false;
		}
	    //make a IMediaWriter to write the file.
	    String videoPath = currDirectory+Config.sep+ f.getName() + ".mp4";
	    final IMediaWriter writer = ToolFactory.makeWriter(videoPath);
	    writer.addVideoStream(0, 0,
	    		BCViz.DEFAULT_WIDTH_PX, BCViz.DEFAULT_HEIGHT_PX);
	    
	    BufferedImage gameFrame = null;
	 
	    for (int index = 0; index < numFrames; index++) {
	      gameFrame = ImageIO.read(gameFrames.poll());
	      // encode the image to stream #0
	      writer.encodeVideo(0,gameFrame, // encode each game image
	              index*1000, TimeUnit.MILLISECONDS);
	    }
	    writer.encodeVideo(0,gameFrame,
	              (numFrames + 3)*1000, TimeUnit.MILLISECONDS);
	    writer.encodeVideo(0,gameFrame,
	              (numFrames + 4)*1000, TimeUnit.MILLISECONDS);
//	    writer.encodeVideo(0,gameFrame,
//	              (numFrames + 5)*1000, TimeUnit.MILLISECONDS);
	    // tell the writer to close and write the trailer 
	    writer.close();
	    return true;
	  }
}