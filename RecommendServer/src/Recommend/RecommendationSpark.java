package Recommend;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Database.DatabaseUtils;
import scala.Tuple2;
import scala.collection.mutable.WrappedArray;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RecommendationSpark {

	private static boolean IS_LOAD_FILE_LAST_RECOMMEND = false;
	
	private static int    currentModelUse = 0;
	
	private static int maximumItemRecommend = 100;
	
	private static SparkSession spark;
	private static SparkConf conf;
	private static JavaSparkContext sc;
	
	private static JavaRDD<UserRating> ratingsRDD;
	private static ALS als;
	private static ALSModel model, modelForRecommend;
	private static Dataset<Row> ratings;
	
	private static HashMap<Integer, ArrayList<Integer>> cacheRecommendResult;
	
	public static void init() throws IOException
	{
		 
		conf = new SparkConf().setMaster("local[*]").setAppName("SparkRecommendation");
		sc = new JavaSparkContext(conf);
		spark = SparkSession.builder()
       	     .master("local")
       	     .appName("Recommendation")
       	     .config("spark.some.config.option", "some-value")
       	     .getOrCreate();
		 spark.sparkContext().setLogLevel("OFF");
		 
		 cacheRecommendResult = new HashMap<>();
		 
		// loadModel();
		 loadDatabase();
		 trainALSModel();
		// ShareLoopGroup.submit(()-> recommendForAllUserAndCreateCache(maximumItemRecommend));
		 //saveModel();
		 //sc.close();
	}
	
	public static void saveModel(int modelIndex) throws IOException
	{
		String path = "savedModel_" + String.valueOf(modelIndex);
		model.write().overwrite().save(path);
	//	model.save("savedModel");
	}
	
	public static void trainALSModelAndSave() throws IOException
	{
		int indexTrainModel = (currentModelUse + 1) % 2;
		System.out.println("Train model :" + indexTrainModel);
		trainALSModel();
		saveModel(indexTrainModel);
		System.out.println("Saved model :" + currentModelUse);
		
		currentModelUse = indexTrainModel;
	}
	
	public static void loadModel()	
	{
		String filepath = "savedModel_" + String.valueOf(currentModelUse);
		modelForRecommend = ALSModel.read().load(filepath);
	}
	
	private static void loadDatabase()
	{
		DatabaseConfig.initDatabaseConfig();
		ratingsRDD = spark.read().jdbc(DatabaseConfig.url, DatabaseConfig.tableRating, DatabaseConfig.properties)
				.javaRDD().map(UserRating::parseRating);
		System.out.println("Size of rating databasE:" +ratingsRDD.count());
		
		// ratingsRDD = spark
       	//	  .read().textFile("Databases/datatest/u.data").javaRDD()
       	//	  .map(UserRating::parseRating);
		 	 
	}
	
	private static void trainALSModel()
	{
		ratings = spark.createDataFrame(ratingsRDD, UserRating.class);
		
		// ALS traning 
		als = new ALS()
				.setRank(10)
		  .setMaxIter(5)
		  .setRegParam(0.01)
		  .setUserCol("userId")
		  .setItemCol("itemId")
		  .setRatingCol("rating");
		model = als.fit(ratings);
		model.setColdStartStrategy("drop");
	}
	
	public static void recommendForAllUser(int numItem)
	{
		Dataset<Row> result = model.recommendForAllUsers(numItem);
	}
	
	public static void recommendForAllUserAndCreateCache(int numItem)
	{
		//System.out.println("Build cache");	
		try {
			if (loadLastRecommendResult()) 
			{
				System.out.println("load last result SUCCEED");
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Dataset<Row> result = model.recommendForAllUsers(numItem);
		createCache(result);
		ShareLoopGroup.scheduleWithFixedDelay(()->retrainModel(), 0, 10, TimeUnit.MINUTES, true) ;
	}
	
	public static ArrayList<SANPHAM> recommendForUserById(int id, int numItem) throws SQLException
	{
		if (cacheRecommendResult != null && cacheRecommendResult.containsKey(id))
		{
			ArrayList<Integer> listIdsCache = cacheRecommendResult.get(id);
			ArrayList<Integer> listIdsCacheReturn = new ArrayList<>();
			if (listIdsCache.size() > 0 )
			{
				int size = Math.min(numItem, listIdsCache.size());
				for (int i = 0 ; i < size; i++)
					listIdsCacheReturn.add(listIdsCache.get(i));
				System.out.println("load cache:" + id + listIdsCacheReturn.size());
				return DatabaseUtils.getListSanPhamByListID(listIdsCacheReturn);
			}
			
		}
		
		ArrayList<Integer> listIdsRecommendations = new ArrayList<>();
		if (numItem > maximumItemRecommend) return DatabaseUtils.getListSanPhamByListID(listIdsRecommendations);
		Dataset<Row> userRating  = ratings.select(als.getUserCol()).where(col(als.getUserCol()).equalTo(String.valueOf(id))).distinct().limit(1);
		if (userRating.count() == 0 || userRating.isEmpty()) 
			{
				System.out.println("No Rating:" + userRating.count() + userRating.isEmpty());
				return DatabaseUtils.getListSanPhamGoiYAnonymous();
			}
		Dataset<Row> result = model.recommendForUserSubset(userRating, numItem);
	
		List<Row> listIds = result. withColumn("itemId", explode(col("recommendations.itemId"))).select(col("itemId")).collectAsList();
		//result = result.select(col("recommendations.itemId"));
		 
		
		 for (Row r : listIds)
		 {
			 listIdsRecommendations.add( r.getInt(0));
		 }
		
		 if (!cacheRecommendResult.containsKey(id))
			 cacheRecommendResult.put(id, listIdsRecommendations);
		 
		// return  listIdsRecommendations;
		 System.out.println("Recommend ID" + id + " list recommend" + listIdsRecommendations.size());
		 return DatabaseUtils.getListSanPhamByListID(listIdsRecommendations);
	}
	
	private static void createCache(Dataset<Row> resultAllUserRecommend)
	{
		List<Row> result1 = resultAllUserRecommend.withColumn("itemId", explode(col("recommendations.itemId"))).select(col("userId"),col("itemId")).collectAsList();
		int currentId = 0;
		 for (Row r : result1)
		 {
			 int userId = r.getInt(0);
			 int itemId = r.getInt(1);
			 if (!cacheRecommendResult.containsKey(userId))
				 cacheRecommendResult.put(userId, new ArrayList<>());
			 if (userId != currentId)
			 {
				 cacheRecommendResult.get(userId).clear();
				 System.out.println("clear arraylist in cache: " + currentId);
				 currentId = userId;
			 }
			 cacheRecommendResult.get(userId).add(itemId);
		 }
	}
	
	private static void retrainModel()
	{
		loadDatabase();
		trainALSModel();
		recommendForAllUserAndCreateCache(maximumItemRecommend);
		saveLastRecommendResult();
	}
	
	@SuppressWarnings("unchecked")
	private static boolean loadLastRecommendResult() throws IOException 
	{
		if (!IS_LOAD_FILE_LAST_RECOMMEND)
			return false;
		String text;
			text = new String(Files.readAllBytes(Paths.get("lastRecommendResult.txt")), StandardCharsets.UTF_8);
			cacheRecommendResult = new Gson().fromJson(text, cacheRecommendResult.getClass());
			System.out.println ("LOAD CACHE:" + cacheRecommendResult.size());
		
		return true;
	}
	
	private static void saveLastRecommendResult()
	{
		if (!IS_LOAD_FILE_LAST_RECOMMEND)
			return;
		Gson gson = new Gson();
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String gsonString = gson.toJson(cacheRecommendResult, gsonType);
        //System.out.println(gsonString);
        try {
            FileWriter myWriter = new FileWriter("lastRecommendResult.txt");
            myWriter.write(gsonString);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
	}
	
}
