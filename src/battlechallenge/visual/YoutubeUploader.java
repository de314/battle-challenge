//package battlechallenge.visual;
//
//import java.io.File;
//import java.net.URL;
//import com.google.gdata.client.youtube.YouTubeService;
//import com.google.gdata.data.media.MediaFileSource;
//import com.google.gdata.data.youtube.VideoEntry;
//import com.google.gdata.data.youtube.YouTubeMediaGroup;
//import com.google.gdata.util.*;
//import com.google.gdata.data.*;
//import com.google.gdata.client.*;
//
//
//public class YoutubeUploader {
//	public static String DEVELOPER_KEY = "AI39si4cu606Qf5HNIbsyfIDH6JVoMcbMTeOvfxLmG3UCIBXN-zv-SRw-wOQiwW9fGSHEkt7t7iFQPmvva7KxwPlvK5rcjBG7A";
//	
//	YouTubeService service = new YouTubeService(DEVELOPER_KEY);
//	service.setUserCredentials("AutonomousArmada@gmail.com", "DavidKevin");
//	
//	VideoEntry newEntry = new VideoEntry();
//	YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
//	mg.setTitle(new MediaTitle());
//	mg.getTitle().setPlainTextContent("Autonomous Armada Test Movie");
//	mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Autos"));
//	mg.setKeywords(new MediaKeywords());
//	mg.getKeywords().addKeyword("AIChallenge");
//	mg.getKeywords().addKeyword("Bot");
//	mg.setDescription(new MediaDescription());
//	mg.getDescription().setPlainTextContent("Game played for the Autonomous Armada AI Challenge");
//	mg.setPrivate(false);
//	mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "mydevtag"));
//	mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "anotherdevtag"));
//
//	newEntry.setGeoCoordinates(new GeoRssWhere(37.0,-122.0));
//	// alternatively, one could specify just a descriptive string
//	// newEntry.setLocation("Mountain View, CA");
//
//	MediaFileSource ms = new MediaFileSource(new File("file.mov"), "video/quicktime");
//	newEntry.setMediaSource(ms);
//
//	String uploadUrl =
//	  "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
//
//	VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
//}
