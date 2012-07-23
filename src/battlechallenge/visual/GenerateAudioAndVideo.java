package battlechallenge.visual;

/*******************************************************************************
 * Copyright (c) 2008, 2010 Xuggle Inc.  All rights reserved.
 *  
 * This file is part of Xuggle-Xuggler-Main.
 *
 * Xuggle-Xuggler-Main is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Xuggle-Xuggler-Main is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Xuggle-Xuggler-Main.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

//package battlechallenge.visual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import battlechallenge.settings.Config;

import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.demos.Balls;
import com.xuggle.mediatool.demos.MovingBalls;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import com.xuggle.xuggler.IPixelFormat.Type;
import javax.imageio.ImageIO;

/**
 * Generate audio and video frames and use the {@link IMediaWriter} to
 * encode that media and write it out to a file.
 *
 */

public class GenerateAudioAndVideo {
	
	public static PriorityQueue<File> getAvailableGameFrames() {
//		List<File> gamePicts = new LinkedList<File>();
		PriorityQueue<File> gamePictures = new PriorityQueue<File>(300, comp);
		getFile(new File(Config.gameDir), gamePictures);
		return gamePictures;
	}
	
	private static void getFile(File dir, Queue<File> pictures) {
		for (String filename : dir.list()) {
			File f = new File(dir.getAbsolutePath()+Config.sep+filename);
			if (f.isDirectory())
				getFile(f, pictures);
			else
				pictures.offer(f);
		}
	}
	
	static Comparator comp = new Comparator() {
		  public int compare(Object o1, Object o2) {
		    File f1 = (File) o1;
		    File f2 = (File) o2;
		    String name1 = f1.getName().substring(0, f1.getName().length()-4);
		    String name2 = f2.getName().substring(0, f2.getName().length()-4);
		    if (name1.equals(".gitig") || name2.equals(".gitig")) {
		    	return 1;
		    }
		    Integer one = Integer.parseInt(name1);
		    Integer two = Integer.parseInt(name2);
//		    if (f1.isDirectory() && !f2.isDirectory()) {
//		      // Directory before non-directory
//		      return -1;
//		    } else if (!f1.isDirectory() && f2.isDirectory()) {
//		      // Non-directory after directory
//		      return 1;
//		    } else {
		      // Alphabetic order otherwise
		      return (one).compareTo(two);
//		    }
		  }
	};
	




  public static void main(String[] args) throws IOException, InterruptedException {
	PriorityQueue<File> gameFrames = getAvailableGameFrames();
	System.out.println(gameFrames);
	int numFrames = gameFrames.size();
	
    
    final Toolkit toolkit = Toolkit.getDefaultToolkit();
      
    //make a IMediaWriter to write the file.
    final IMediaWriter writer = ToolFactory.makeWriter("gameOutput2.mp4");
    
    writer.addVideoStream(0, 0,
        800, 1000);
    Double FRAME_RATE = 100.0;
    
    long startTime = System.nanoTime();
//    long startTime = System.currentTimeMillis();
    ImageIO.read(gameFrames.poll()); // b/c of git file is the first index FIX
    for (int index = 1; index < numFrames; index++) { // b/c of git file is the first index FIX
     
      BufferedImage gameFrame = ImageIO.read(gameFrames.poll());
      // encode the image to stream #0
      writer.encodeVideo(0,gameFrame,
          System.nanoTime()-startTime, TimeUnit.NANOSECONDS);
      
//      writer.encodeVideo(0,gameFrame,
//    		  System.currentTimeMillis()-startTime, TimeUnit.MILLISECONDS);
      System.out.println("encoded image: " + index);
      
      // sleep for framerate milliseconds
      Thread.sleep((long) (40000 / FRAME_RATE));
    }
    // tell the writer to close and write the trailer 
    writer.close();  

  }  
}


