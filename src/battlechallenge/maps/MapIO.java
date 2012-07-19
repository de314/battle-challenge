package battlechallenge.maps;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import battlechallenge.settings.Config;

public class MapIO {

	public static Map<Integer, Collection<MapDescription>> getAvailableMaps() {
		HashMap<Integer, Collection<MapDescription>> maps = new HashMap<Integer, Collection<MapDescription>>();
		Queue<File> mapFiles = new LinkedList<File>();
		getFile(new File(Config.mapDir), mapFiles);
		while (!mapFiles.isEmpty()) {
			MapDescription temp = getMapDescription(mapFiles.poll());
			if (!maps.containsKey(temp.getNumPlayers()))
				maps.put(temp.getNumPlayers(), new LinkedList<MapDescription>());
			maps.get(temp.getNumPlayers()).add(temp);
		}
		return maps;
	}

	public static MapDescription getMapDescription(File f) {
		try {
			Scanner scan = new Scanner(f);
			String[] header = scan.nextLine().split(",");
			int width = Integer.parseInt(header[0].trim());
			int height = Integer.parseInt(header[1].trim());
			int numPlayers = Integer.parseInt(header[2].trim());
			StringBuilder map = new StringBuilder("\n");
			for (int i=0;i<height;i++)
				map.append("  ").append(scan.nextLine()).append("  \n");
			return new MapDescription(f.getName(), f.getAbsolutePath(),
					numPlayers, width, height, map.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void getFile(File dir, Queue<File> mapFiles) {
		for (String filename : dir.list()) {
			File f = new File(dir.getAbsolutePath()+Config.sep+filename);
			if (f.isDirectory())
				getFile(f, mapFiles);
			else
				mapFiles.offer(f);
		}
	}

}
