package DropWizard;

import java.io.IOException;

import Recommend.RecommendationSpark;
import Recommend.ShareLoopGroup;
import io.dropwizard.setup.Environment;
import Database.CacheUserHistory;
import Database.DatabaseUtils;
import Database.NeighborhoodBased;
import Database.RowInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends io.dropwizard.Application<RecommendServerConfiguration> {

	@Override
	public void run(RecommendServerConfiguration configuration, Environment environment) throws Exception {
		// TODO Auto-generated method stub
		environment.jersey().register(new RecommendController(environment.getValidator()));
	}

	public static void main(String[] args) throws Exception {
		DatabaseUtils.initConnection();
		CacheUserHistory.init();
		new Server().run("server", "application.yml");
		
		/*
		 NeighborhoodBased nb = new NeighborhoodBased();
		DatabaseUtils.loadRatings("datatest\\ua.base", nb.getUtilityMatrix(),nb.getItemRatingInfo());	
		DatabaseUtils.loadRatings("datatest\\ua.test", nb.getTestMatrix(), new HashMap<Integer, RowInfo>());	
		
		//nb.printUtilityMatrix();	
		nb.normalizedMatrix();		
		//nb.printNormalizedMatrix();
		nb.calculateSimilarityMatrix();
		//nb.printSimilarityMatrix();
		
		//System.out.println("predict rate userId = 11 & itemId = 1: "+ nb.iipredictRateUserForItem(12, 1));
		nb.predictAllUserForEvaluate();
		System.out.println(nb.RMSE());*/
		//initRecommendationSystem();

		
	}
	
	private static void initRecommendationSystem() throws IOException
	{
		RecommendationSpark.init();
	}
	
}
