package battlechallenge.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

	public static enum OperatingSystem {
		WINDOWS, MAC, UNIX, SOLARIS
	};

	public static final String osName;
	public static final OperatingSystem os;
	public static final String sep;
	public static final String n;
	public static final String mapDir;
	private static final Map<String, String> params;

	static {
		osName = System.getProperty("os.name");
		String lowerOsName = osName.toLowerCase();
		if (lowerOsName.indexOf("win") > 0)
			os = OperatingSystem.WINDOWS;
		else if (lowerOsName.indexOf("mac") > 0)
			os = OperatingSystem.MAC;
		else if (lowerOsName.indexOf("sunos") > 0)
			os = OperatingSystem.SOLARIS;
		else
			os = OperatingSystem.UNIX;
		sep = System.getProperty("path.separator");
		n = System.getProperty("line.separator");
		params = new HashMap<String, String>();
		parseConfigFile();
		mapDir = params.get("mapDir");
	}

	private static void parseConfigFile() {
		// TODO:
		try {
			Scanner scan = new Scanner(new File("config.txt"));
			while (scan.hasNext()) {
				String[] arr = scan.nextLine().split("=");
				if (arr.length == 2)
					params.put(arr[0].trim().toLowerCase(), arr[1].trim()
							.toLowerCase().replace("/", sep).replace("\\", sep));
			}
		} catch (FileNotFoundException e) {
			params.put("mapDir", "Maps" + sep);
		}
	}
}
