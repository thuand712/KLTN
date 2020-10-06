package Database;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Recommend.Time;

public class UserHistory {
	private static final long TIME_REMOVE_RECENT_VIEW = 3 * Time.SECOND_IN_DAY;
	private static final int  TOP_K_BEST_POINT_ITEM = 3;
	private static final int  MIN_POINT_TO_RECOMMEND = 700;	//diem toi thieu de goi y
	private static final int  NUM_TYPE_ITEM_VIEWED = 3; //so luong danh muc san pham dung de lay ra sp
	HashMap<Integer, Integer> listItemViewed;
	
	HashMap<Integer, ItemPredictRate> listItemPredictRate;
	
	LinkedHashMap<String, Integer> recentlyTypeViewed = new LinkedHashMap<String, Integer>();
	
	public UserHistory()
	{
		this.listItemPredictRate = new HashMap<>();
		this.listItemViewed = new HashMap<>();
	}
	
	public void startView(int itemId, String itemType)
	{
		if (!this.listItemPredictRate.containsKey(itemId))
			this.listItemPredictRate.put(itemId, new ItemPredictRate(itemId));
		this.listItemViewed.put(itemId, Time.getUnixTime());
		
		ItemPredictRate itemPredictRate = this.listItemPredictRate.get(itemId);
		itemPredictRate.increaseViewCount(1);
		itemPredictRate.setTimeStartRead(Time.getUnixTime());
		
		if (!recentlyTypeViewed.containsKey(itemType))
			recentlyTypeViewed.put(itemType, Time.getUnixTime());
		if (recentlyTypeViewed.size() > NUM_TYPE_ITEM_VIEWED)
		{
			for (String type : recentlyTypeViewed.keySet())
			{
				recentlyTypeViewed.remove(type);
				if (recentlyTypeViewed.size() <= NUM_TYPE_ITEM_VIEWED) break;
			}
		}
	}
	
	public void endView(int itemId)
	{
		if (!this.listItemPredictRate.containsKey(itemId)) return;
		this.listItemPredictRate.get(itemId).endRead(itemId, Time.getUnixTime());
	}
	
	public boolean isRecentlyViewed(int itemId)
	{
		
		if (!this.listItemViewed.containsKey(itemId))
		{
			//System.out.println("FALSE");
			return false;
		}
		//System.out.println((Time.getUnixTime() - this.listItemViewed.get(itemId) < TIME_REMOVE_RECENT_VIEW));
		return  Time.getUnixTime() - this.listItemViewed.get(itemId) < TIME_REMOVE_RECENT_VIEW  ;
	}
	
	public void resetDaily()
	{
		int curTime = Time.getUnixTime();
		for (Map.Entry<Integer, Integer> entry : this.listItemViewed.entrySet())
		{
			if (curTime - entry.getValue() > TIME_REMOVE_RECENT_VIEW)
				this.listItemViewed.remove(entry.getKey());
		}
	}
	
	public Set<String> getRecentlyTypeViewed()
	{
		return this.recentlyTypeViewed.keySet();
	}
	
	//lay danh sach id co diem so cao dua tren so luot xem va tho
	public ArrayList<Integer> getListIdsHighPoint(int num)
	{
		ArrayList<Integer> listIds = new ArrayList<>();
		int count = 0;
		for (Map.Entry<Integer, ItemPredictRate> entry : this.listItemPredictRate.entrySet())
		{
			if (entry.getValue().getPoint() > MIN_POINT_TO_RECOMMEND)
			{
				count += 1;
				listIds.add(entry.getKey());
			}
			if (count > num) break;
		}
		return listIds;
	}
		
	public class ItemPredictRate implements Comparator<ItemPredictRate>, Comparable<ItemPredictRate>
	{
		private int itemId;
		private int viewCount;
		private int timeView;
		
		private long lastTimeStartRead = 0;

		public ItemPredictRate(int itemId)
		{
			this.itemId = itemId;
			this.viewCount = 0;
			this.timeView = 0;
		}
		public void increaseViewCount(int add) 
		{
			this.viewCount += add;
		}

		public void increaseTimeView(int add) 
		{
			this.timeView += add;
		}
		
		public int getViewCount()
		{
			return this.viewCount;
		}
		
		public long getTimeView()
		{
			return this.timeView;
		}
		
		public void setTimeStartRead(long time)
		{
			if (this.lastTimeStartRead != 0)
			{
				// lan cuoi cung chua thuc hien endRead duoc.
				this.timeView += Math.min(30, Time.getUnixTime() - this.lastTimeStartRead);
			}
			this.lastTimeStartRead = time;
		}
		
		public void endRead(int itemId, long time)
		{
			if (this.itemId != itemId) return;
			this.timeView += Math.max(time - this.lastTimeStartRead, 1);
			this.lastTimeStartRead = 0;
			
		}
		
		public int getPoint()
		{
			return this.viewCount * 10 + this.timeView;
		}
		@Override
		public int compareTo(ItemPredictRate arg0) {
			return this.getPoint() - arg0.getPoint();
		}
		@Override
		public int compare(ItemPredictRate arg0, ItemPredictRate arg1) {
			return arg0.getPoint() - arg1.getPoint();
		}
	}
	

}
