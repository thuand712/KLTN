package Database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Recommend.ShareLoopGroup;

public class CacheUserHistory {
	
	private static HashMap<String, UserHistory> cacheUserHistory;
	private final static String PATH_TO_SAVE = "CacheUserHistory\\cacheUserHistory.txt";
	
	private static transient boolean isChanged = false;
	
	public static void save()
	{
		writeToFile();
	}
	
	public static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	private static void load()
	{
	
		String content = null;
		try {
			content = readFile(PATH_TO_SAVE, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		java.lang.reflect.Type collectionType = new TypeToken<HashMap<String, UserHistory>>(){}.getType();
		cacheUserHistory = gson.fromJson(content, collectionType);
		//System.out.println("Size of hashmap:" + cacheUserHistory.size());
	}
	
	public static void init()
	{
		cacheUserHistory = new HashMap<>();
		load();
		ShareLoopGroup.scheduleWithFixedDelay(()->save(), 0, 10, TimeUnit.SECONDS, true) ;
	}
	
	public void resetDaily()
	{
		for (Map.Entry<String, UserHistory> entry:cacheUserHistory.entrySet())
		{
			entry.getValue().resetDaily();
		}
	}
	
	public static void userStartViewItem(String userId, int itemId, String itemType)
	{
		if (!cacheUserHistory.containsKey(userId))
		cacheUserHistory.put(userId,  new UserHistory());
		UserHistory userHistory = cacheUserHistory.get(userId);
		userHistory.startView(itemId, itemType);
		isChanged = true;
	}
	
	public static void userEndViewItem(String userId, int itemId)
	{
		if (!cacheUserHistory.containsKey(userId))
		cacheUserHistory.put(userId,  new UserHistory());
		UserHistory userHistory = cacheUserHistory.get(userId);
		userHistory.endView(itemId);
		isChanged = true;
	}
	
	public static UserHistory getUserHistory(String userId)
	{
		if (cacheUserHistory.containsKey(userId))
			return cacheUserHistory.get(userId);
		return null;
	}
	
	public static boolean hasRecentlyViewed(String userId, int itemId)
	{
		if (!cacheUserHistory.containsKey(userId))
		{
			//System.out.println("Cacheuserhistory: FALSE");
			return false;
		}
		//System.out.println("Cacheuserhistory: TRUE");
		return cacheUserHistory.get(userId).isRecentlyViewed(itemId);	
	}
	
	public static ArrayList<Integer> getListIdsViewed(String userId,int num)
	{
		if (!cacheUserHistory.containsKey(userId))
			return new ArrayList<Integer>();
		return cacheUserHistory.get(userId).getListIdsHighPoint(num);
	}
	
	private static void writeToFile()
	{
		if (!isChanged) return;
		try {
		      FileWriter myWriter = new FileWriter(PATH_TO_SAVE);
		      
		      Gson gson = new Gson();
		      String dataToWrite = gson.toJson(cacheUserHistory);
		      
		      myWriter.write(dataToWrite);
		  	  isChanged = false;
		      myWriter.close();
		     // System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) 
			{
		     // System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
}
