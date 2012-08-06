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
		System.out.println(currDirectory);
		PriorityQueue<File> gameFrames = getAvailableGameFrames();
		int numFrames = gameFrames.size();
	    System.out.println(numFrames); 
	    System.out.println(gameFrames);
	    //make a IMediaWriter to write the file.
	    String videoName = currDirectory+".mp4";
	    System.out.println(videoName);
	    final IMediaWriter writer = ToolFactory.makeWriter(videoName);
	    writer.addVideoStream(0, 0,
	    		BCViz.DEFAULT_WIDTH_PX, BCViz.DEFAULT_HEIGHT_PX);
	    
	    long startTime = System.currentTimeMillis();
	    BufferedImage gameFrame = null;
	    //ImageIO.read(gameFrames.poll()); // b/c of git file is the first index FIX
	    for (int index = 0; index < numFrames; index++) { // b/c of git file is the first index FIX
	      gameFrame = ImageIO.read(gameFrames.poll());
	      // encode the image to stream #0
	      writer.encodeVideo(0,gameFrame,
	              index*1000, TimeUnit.MILLISECONDS);
	      System.out.println("encoded image: " + gameFrame + " Index: " + index);
//	      writer.flush();
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


//  public static void main(String[] args) throws IOException, InterruptedException {
//	PriorityQueue<File> gameFrames = getAvailableGameFrames();
//	int numFrames = gameFrames.size();
//      
//    //make a IMediaWriter to write the file.
//    final IMediaWriter writer = ToolFactory.makeWriter("gameOutput.mp4");
//    
//    writer.addVideoStream(0, 0,
//    		BCViz.DEFAULT_WIDTH_PX, BCViz.DEFAULT_HEIGHT_PX);
//    
////    long startTime = System.nanoTime();
//    long startTime = System.currentTimeMillis();
//    ImageIO.read(gameFrames.poll()); // b/c of git file is the first index FIX
//    for (int index = 1; index < numFrames; index++) { // b/c of git file is the first index FIX
//      BufferedImage gameFrame = ImageIO.read(gameFrames.poll());
//      // encode the image to stream #0
//      writer.encodeVideo(0,gameFrame,
//              startTime + index*1000-startTime, TimeUnit.MILLISECONDS);
//    }
//    // tell the writer to close and write the trailer 
//    writer.close();  
//
//  }  
//}
//
//
