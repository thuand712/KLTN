package Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class NeighborhoodBased {

	private final int NUM_USER_DEFAULT = 944; //5;//944//13
	private final int NUM_ITEM_DEFAULT = 1683; //8;//1683//7
	private final int K_NEAREST_NEIGHBOR = 2;
	private boolean NEED_EVALUATE = true;
	
	private HashMap<Integer, RowInfo> itemRatingInfo = new HashMap<>();
	private float[][] utilityMatrix;
	private float[][] normalizedMatrix;
	private double[][] similarityMatrix;
	private int numUser = 0;
	private int numItem = 0;
	
	//Evaluate
	private float[][] predictMatrix;
	private float[][] testMatrix;
	
	
	public NeighborhoodBased()
	{
		this.numUser = NUM_USER_DEFAULT;
		this.numItem = NUM_ITEM_DEFAULT;
		utilityMatrix = new float[NUM_ITEM_DEFAULT][NUM_USER_DEFAULT];
		normalizedMatrix = new float[NUM_ITEM_DEFAULT][NUM_USER_DEFAULT];
		similarityMatrix = new double[NUM_ITEM_DEFAULT][NUM_ITEM_DEFAULT];
		if (NEED_EVALUATE) 
		{
			predictMatrix = new float[NUM_ITEM_DEFAULT][NUM_USER_DEFAULT];
			testMatrix = new float[NUM_ITEM_DEFAULT][NUM_USER_DEFAULT];
		}
			
	}
	
	public NeighborhoodBased(int numItem, int numUser)
	{
		this.numUser = numUser;
		this.numItem = numItem;
		utilityMatrix = new float[numItem][numUser];
		normalizedMatrix = new float[numItem][numUser];
		similarityMatrix = new double[numItem][numItem];
		if (NEED_EVALUATE) 
		{
			predictMatrix = new float[numItem][numUser];
			testMatrix = new float[numItem][numUser];
		}
	}
	
	
	public void setUtilityMatrix(float[][] UtilityMatrix)
	{
		this.utilityMatrix = UtilityMatrix;
	}
	
	public float[][] getTestMatrix() {
		return testMatrix;
	}

	public void setTestMatrix(float[][] testMatrix) {
		this.testMatrix = testMatrix;
	}

	public void normalizedMatrix()
	{
		if (this.itemRatingInfo == null || this.itemRatingInfo.size() == 0)
		{
			System.out.println("There are no rating in utilitymatrix, normalized ended with nothing done");
			return;
		}
		for (int i=1; i< this.numItem; i++)
		{
			if (this.itemRatingInfo.containsKey(i))
			{
				float averageValue = (this.itemRatingInfo.get(i).sum / this.itemRatingInfo.get(i).numValue);
				//System.out.println("Average value: " + averageValue);
				for (int j=1; j< this.numUser; j++)
				{
					if (this.utilityMatrix[i][j] != 0)
					normalizedMatrix[i][j] = this.utilityMatrix[i][j] - averageValue;
				}
			}
		}
	}
	
	public void calculateSimilarityMatrix()
	{
		for (int i=1; i< this.numItem; i++)
		{
			
			for (int j= i; j< this.numItem ; j++)
			{
				double similarity = this.similarity(i, j);
				this.similarityMatrix[i][j] = similarity;
				this.similarityMatrix[j][i] = similarity;
			}
		}
	}
	
	public void calculateAverageRatePerItem()
	{
		for (Map.Entry<Integer, RowInfo> entry : this.itemRatingInfo.entrySet())
		{
			entry.getValue().average = entry.getValue().sum / entry.getValue().numValue;
		}
	}

	public HashMap<Integer, RowInfo> getItemRatingInfo() {
		return itemRatingInfo;
	}

	public void setitemRatingInfo(HashMap<Integer, RowInfo> itemRatingInfo) {
		this.itemRatingInfo = itemRatingInfo;
	}
	

	public float[][] getNormalizedMatrix() {
		return normalizedMatrix;
	}

	public void setNormalizedMatrix(float[][] normalizedMatrix) {
		this.normalizedMatrix = normalizedMatrix;
	}

	public double[][] getSimilarityMatrix() {
		return similarityMatrix;
	}

	public void setSimilarityMatrix(double[][] similarityMatrix) {
		this.similarityMatrix = similarityMatrix;
	}

	public float[][] getUtilityMatrix() {
		return utilityMatrix;
	}
	
	public double similarity(int itemA, int itemB)
	{
		if (itemA == itemB) return 1;
		return similarityTwoArr(this.normalizedMatrix[itemA],this.normalizedMatrix[itemB], this.numUser);
	}
	
	private double similarityTwoArr(float[] arrA, float[] arrB, int size)
	{
		double sim = 0;
		float sumAxB = 0;
		float sumPowA = 0;
		float sumPowB = 0;
		for (int i = 1; i < size; i++)
		{
			sumAxB += arrA[i] * arrB[i];
			sumPowA += Math.pow(arrA[i], 2);
			sumPowB += Math.pow(arrB[i], 2);
		}
		sim =  sumAxB / (Math.sqrt(sumPowA) * Math.sqrt(sumPowB) +0.000000001f);

		return sim;
	}
	
	public void printUtilityMatrix()
	{
		for (int i= 1; i< this.numItem; i++)	
	      {
			if (!this.itemRatingInfo.containsKey(i))
				continue;
			 for (int j= 1; j< this.numUser; j++)
				 
	    		  System.out.print(utilityMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public void printInfo()
	{
		 for (Map.Entry<Integer, RowInfo> entry: itemRatingInfo.entrySet())
	      {
	    	  System.out.println("user ID:" + entry.getKey() +"\t sum= " + entry.getValue().sum + "\t numValue = " + entry.getValue().numValue );  
	      }
	}
	
	public void printNormalizedMatrix()
	{
		for (int i=1; i< this.numItem; i++)
	      {
			 if (!this.itemRatingInfo.containsKey(i))
	  				continue;
	    	  for (int j=1; j< this.numUser; j++)
	    	  {
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(normalizedMatrix[i][j] + "\t");
	    	  }
	    	  System.out.println();
	    	  
	      }
	}
	
	public void printSimilarityMatrix()
	{
		for (int i=1; i< this.numItem; i++)
	      {
			if (!this.itemRatingInfo.containsKey(i))
				continue;
	    	  for (int j=1; j< this.numItem; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(this.similarityMatrix[j][i] + "\t");
	    	  System.out.println();
	      }
	}
	
	//user-user
	public ArrayList<RateInfo> predictRateUserItem(int userId, int itemId)
	{
		ArrayList<RateInfo>  rateInfo = new ArrayList<>();
		for (int i = 1; i< this.numUser; i++)
		{
			if (this.utilityMatrix[i][itemId] != 0 && userId != i)
			{
				rateInfo.add(new RateInfo(i, this.similarityMatrix[userId][i],this.utilityMatrix[i][itemId]));
			}
		}
			
		/*
		for (RateInfo ri : rateInfo)
		{
			System.out.println("user " + ri.userId 
			+ "\trate item " + itemId + "\tvalue= " + ri.rate 
			+ "\tsimilarity = " + ri.sim);
		}
		*/
		return rateInfo;
	}
	//user-user
	public ArrayList<Rate> predictItemsForUser(int userId)
	{
		ArrayList<RateInfo>  rateInfo = new ArrayList<>();
		
		//get ListUserId with similarity descending
		for (int i = 1; i< this.numUser; i++)
		{
			if (i == userId)
				continue;
			//System.out.println("userID = " + i + "sim = " + this.similarityMatrix[userId][i]);
			rateInfo.add(new RateInfo(i, this.similarityMatrix[userId][i], 0));
		}
		
		sortDescendingBySim(rateInfo);	

		
		ArrayList<Rate> predictResult = new ArrayList<>();
		for (int i = 1; i< this.numItem; i++)
		
		if (NEED_EVALUATE || this.utilityMatrix[userId][i] == 0)
		{
			ArrayList<RateInfo> listFiltered = filterListRateInfoWithItemId(rateInfo, i);
			float rate = 0;
			if (listFiltered.size() > 0) 
			{ 
				rate = predictRateUserToItem(listFiltered);
				predictResult.add(new Rate(i, rate));	
			}	
		}
		sortDescendingByRate(predictResult);
	
		return predictResult;
	}
	//user-user
	public void sortDescendingBySim(ArrayList<RateInfo> listToSort)
	{
		Collections.sort(listToSort, new Comparator<RateInfo>() {
	        @Override
	        public int compare(RateInfo ri1, RateInfo ri2)
	        {
	        	if (ri1.sim == ri2.sim) return 0;
	            if (ri1.sim < ri2.sim) return 1;
	            return -1;
	        }
	    });
	}
	
	public void sortDescendingByRate(ArrayList<Rate> listToSort)
	{
		Collections.sort(listToSort, new Comparator<Rate>() {
	        @Override
	        public int compare(Rate ri1, Rate ri2)
	        {
	        	if (ri1.rate == ri2.rate) return 0;
	            if (ri1.rate < ri2.rate) return 1;
	            return -1;
	        }
	    });
	}
	
	//user-user
		public void sortAscendingByItemId(ArrayList<Rate> listToSort)
		{
			Collections.sort(listToSort, new Comparator<Rate>() {
		        @Override
		        public int compare(Rate ri1, Rate ri2)
		        {
		        	if (ri1.itemId == ri2.itemId) return 0;
		            if (ri1.itemId  > ri2.itemId) return 1;
		            return -1;
		        }
		    });
		}
	
	//user-user
	public ArrayList<RateInfo> filterListRateInfoWithItemId(ArrayList<RateInfo> listNeedFilter,int itemId)
	{
		ArrayList<RateInfo> listFiltered = new ArrayList<>();
		int count = 0;
		for (RateInfo ri : listNeedFilter)
		{
			if (this.utilityMatrix[ri.userId][itemId] != 0 
					&& ri.sim > 0
					)
			{
				ri.rate = this.utilityMatrix[ri.userId][itemId];
				listFiltered.add(ri);
				count +=1;
				if (count >= this.K_NEAREST_NEIGHBOR) break;
				//System.out.println("user ID: " + ri.userId +  "\titemId:" + itemId + "\tsim: " + ri.sim);
			}
		}
		
		return listFiltered;
	}
	
	private float predictRateUserToItem(ArrayList<RateInfo> sortedSim)
	{
		float ratePredict = 0;
		float sumRatexSim = 0;
		float sumSim = 0;
		for (RateInfo ri : sortedSim)
		{		
				sumRatexSim += ri.rate * ri.sim;
				sumSim += Math.abs(ri.sim);		
		}
		if (Float.isNaN(sumSim)) sumSim = 0.0000000001f;
		ratePredict = sumRatexSim / sumSim;
		return ratePredict;
	}
	
	//item-item
	public ArrayList<Rate> item_itemPredictItemsForUser(int userId, int numItemPredict)
	{
		ArrayList<Rate>  rateInfo = new ArrayList<>();
		for (int i = 1; i < numItem ; i++)
		{
			if (this.utilityMatrix[i][userId] != 0) continue;
			rateInfo.add(new Rate(i, iipredictRateUserForItem(userId, i)));
			
		}
		sortDescendingByRate(rateInfo);
		
		/*
		int count = 0;
		for (Rate rate:rateInfo)
		{
			 rate.printRate();
			 count+=1;
			 if (count >= numItemPredict)break;
		}*/
		return rateInfo;
	}
	
	public float iipredictRateUserForItem(int userId, int itemId)
	{
		ArrayList<iiRateInfo> listUserRated = iigetSimilarityItem(userId, itemId);
		int size = Math.min(listUserRated.size(), K_NEAREST_NEIGHBOR);
		if (size == 0) return 0;
		float sumRatexSim = 0;
		float sumSim = 0;
		for (int i = 0; i < size; i++)
		{
			sumRatexSim += listUserRated.get(i).rate * listUserRated.get(i).sim;
			sumSim += Math.abs(listUserRated.get(i).sim);
		}
		//System.out.println("sumRatexSim= " + sumRatexSim + "\tsumSim= "+sumSim);
		return sumRatexSim / sumSim;
	}
	
	public ArrayList<iiRateInfo> iigetSimilarityItem(int userId, int itemId)
	{
		ArrayList<iiRateInfo> listUserRated = new ArrayList<>();
		for (int i = 1 ; i < numItem; i++)
		{
			if (this.utilityMatrix[i][userId] == 0)
				continue;
			double sim = this.similarity(itemId, i);
			if (sim > 0)
			listUserRated.add(new iiRateInfo(i, this.similarity(itemId, i), this.utilityMatrix[i][userId]));
		}
		iisortDescendingBySim(listUserRated);
		
		//for (iiRateInfo iirate : listUserRated)
			//System.out.println("ID: " + itemId + " id:" + iirate.itemId + "\tsim= "+iirate.sim +"\trate= " + iirate.rate);
		
		return listUserRated;
	}
	
	public void iisortDescendingBySim(ArrayList<iiRateInfo> listToSort)
	{
		Collections.sort(listToSort, new Comparator<iiRateInfo>() {
	        @Override
	        public int compare(iiRateInfo ri1, iiRateInfo ri2)
	        {
	        	if (ri1.sim == ri2.sim) return 0;
	            if (ri1.sim < ri2.sim) return 1;
	            return -1;
	        }
	    });
	}
	
	public ArrayList<Integer> getListItemsSimilar(ArrayList<Integer> listItemIds)
	{
		ArrayList<Integer> listIdsResult = new ArrayList<>();
		
		for (int v : listItemIds)
			listIdsResult.addAll(getListItemsSimilar(v, 3));

		return listIdsResult;
	}
	
	private ArrayList<Integer> getListItemsSimilar(int itemId, int numItem)
	{
		ArrayList<iiRateInfo> listiiRateInfo = new ArrayList<>();
		for (int i = 1; i < this.numItem; i++)
		{
			if (i == itemId) continue;
			listiiRateInfo.add(new iiRateInfo(i, this.similarityMatrix[i][itemId], 0));
		}
		this.iisortDescendingBySim(listiiRateInfo);
		
		/*
		for (iiRateInfo r: listiiRateInfo)
		{
			System.out.println(r.itemId + " \t sim= " + r.sim);
		}*/
		
		ArrayList<Integer> listIdsResult = new ArrayList<>();
		int count = 0;
		for (iiRateInfo iRate : listiiRateInfo)
		{
			count +=1;
			listIdsResult.add(iRate.itemId);
			if (count > numItem)
				break;
		}
		return listIdsResult;
	}
		
	//sortedSim la danh sach nhung user da rate item theo thu tu sim giam dan
	
	public void predictAllUserForEvaluate()
	{
		if (!NEED_EVALUATE) return;
		long count = 0;
		for (int i = 0; i< this.numUser; i++)
		{
			ArrayList<Rate> listRate = this.item_itemPredictItemsForUser(i, 1);
			for (Rate r: listRate)
			{
				this.predictMatrix[r.itemId][i] = r.rate;
				count += 1;
			}
			//System.out.println("fill complete predict for user " + i);
		}
		System.out.println("fill complete "+ count + " predict " );
	}
	
	public void printPredictMatrix()
	{
		for (int i=1; i< this.numUser; i++)
	      {
			if (!this.itemRatingInfo.containsKey(i))
				continue;
	    	  for (int j=1; j< this.numItem; j++)
	    		 // if (normalizedMatrix[i][j] != 0) 
	    			  System.out.print(predictMatrix[i][j] + "\t");
	    	  System.out.println();
	      }
	}
	
	public double RMSE()
	{
		double rmse = 0;
		int count = 0;
		predictAllUserForEvaluate();
		
		for (int i = 1; i < this.numUser ; i++)
			for (int j = 0; j < this.numItem ; j++)
			{
				if (this.testMatrix[i][j] != 0)
				{
					
					double delta = Math.pow(this.testMatrix[i][j] - this.predictMatrix[i][j], 2);
					//System.out.println("delta =  " + delta + "\t" + this.testMatrix[i][j] +"\t"+ this.predictMatrix[i][j] );
					rmse += delta;			
					count += 1;
					if (Double.isNaN(rmse))
					{
						System.out.println("REAL : " + this.testMatrix[i][j] + "\tPREDICT: " + this.predictMatrix[i][j] + "\t"+i+"\t"+j);
					    break;
					}
				}
				if (Double.isNaN(rmse)) break;
			}
		System.out.println("Count = " + count);
		rmse = Math.sqrt(rmse / count);
		return rmse;
	}
}
