package battlechallenge.visual;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import battlechallenge.settings.Config;
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
	
	public PriorityQueue<File> getAvailableGameFrames() {
		PriorityQueue<File> gamePictures = new PriorityQueue<File>(300, comp);
		File f = new File(currDirectory); //Config.sep+filename);
		for (String filename: f.list()) {
			File file = new File(f.getAbsolutePath()+Config.sep+filename);
			gamePictures.offer(file);
		}
		return gamePictures;
	}
	
	static Comparator<File> comp = new Comparator<File>() {
		  public int compare(File f1, File f2) {
		    String name1 = f1.getName().substring(0, f1.getName().length()-4);
		    String name2 = f2.getName().substring(0, f2.getName().length()-4);
		    Integer one = Integer.parseInt(name1);
		    Integer two = Integer.parseInt(name2);
		    return one.compareTo(two);
		  }
	};
	

	public GenerateVideo(String currDirectory) {
		this.currDirectory = currDirectory;
	}
	
	public void genVideo() throws IOException, InterruptedException {
		//System.out.println(currDirectory);
		PriorityQueue<File> gameFrames = getAvailableGameFrames();
		int numFrames = gameFrames.size();
		if (numFrames == 0) { // No Images were saved so don't create a video
			return;
		}
	    //make a IMediaWriter to write the file.
	    String videoName = currDirectory+".mp4";
	    //System.out.println(videoName);
	    final IMediaWriter writer = ToolFactory.makeWriter(videoName);
	    writer.addVideoStream(0, 0,
	    		BCViz.DEFAULT_WIDTH_PX, BCViz.DEFAULT_HEIGHT_PX);
	    
	    BufferedImage gameFrame = null;
	    //ImageIO.read(gameFrames.poll()); // b/c of git file is the first index FIX
	    for (int index = 0; index < numFrames; index++) {
	      gameFrame = ImageIO.read(gameFrames.poll());
	      // encode the image to stream #0
	      writer.encodeVideo(0,gameFrame,
	              index*1000, TimeUnit.MILLISECONDS);
	      //System.out.println("encoded image: " + gameFrame + " Index: " + index);
	    }
	    writer.encodeVideo(0,gameFrame,
	              (numFrames + 3)*1000, TimeUnit.MILLISECONDS);
	    writer.encodeVideo(0,gameFrame,
	              (numFrames + 4)*1000, TimeUnit.MILLISECONDS);
	    writer.encodeVideo(0,gameFrame,
	              (numFrames + 5)*1000, TimeUnit.MILLISECONDS);
	    
	    // tell the writer to close and write the trailer 
	    writer.close();  
	  }
}