package battlechallenge.visual;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import battlechallenge.settings.Config;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.media.mediarss.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.io.File;
import java.net.URL;


public class YoutubeUploader {
	private String DEVELOPER_KEY = "AI39si4cu606Qf5HNIbsyfIDH6JVoMcbMTeOvfxLmG3UCIBXN-zv-SRw-wOQiwW9fGSHEkt7t7iFQPmvva7KxwPlvK5rcjBG7A";
	private String username = "AutonomousArmada@gmail.com";
	private String password = "DavidKevin";
	private String videoName;
	private String videoURL;
	
	public String getVideoURL() {
		return videoURL;
	}

	public YoutubeUploader(String videoName) throws MalformedURLException, IOException, ServiceException {
		this.videoName = videoName;
	}
	
	public void uploadVideo() throws MalformedURLException, IOException, ServiceException {
		YouTubeService service = new YouTubeService("test", "AI39si4cu606Qf5HNIbsyfIDH6JVoMcbMTeOvfxLmG3UCIBXN-zv-SRw-wOQiwW9fGSHEkt7t7iFQPmvva7KxwPlvK5rcjBG7A");
		service.setUserCredentials(username, password);

		VideoEntry newEntry = new VideoEntry();
		YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
		mg.setTitle(new MediaTitle());
		mg.getTitle().setPlainTextContent("Autonomous Armada Test Movie");
		mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Autos"));
		mg.setKeywords(new MediaKeywords());

		mg.getKeywords().addKeyword("AIChallenge");
		mg.getKeywords().addKeyword("Bot");
		mg.setDescription(new MediaDescription());
		mg.getDescription().setPlainTextContent("Game played for the Autonomous Armada AI Challenge");
		mg.setPrivate(false);
		newEntry.setGeoCoordinates(new GeoRssWhere(37.0,-122.0));
	    newEntry.setLocation("Atlanta, GA");
		mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "tag 1"));
		mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "tag 2"));
		File video = new File(videoName);
		
		if (!video.exists()) {
			System.out.println("Video does not exist");
			return;
		}
		
		MediaFileSource ms = new MediaFileSource(new File(videoName), "video/quicktime");
		newEntry.setMediaSource(ms);

		String uploadUrl =
				"http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
		VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry); // upload video, get video entry object
		videoURL = getVideoLink(createdEntry);
		}
	
	public String getVideoLink(VideoEntry videoEntry) {
		YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup(); 
		MediaPlayer mediaPlayer = mediaGroup.getPlayer();
		videoURL = mediaPlayer.getUrl();
		return videoURL;
	}
}
