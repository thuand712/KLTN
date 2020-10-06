package Recommend;

import java.util.Properties;

public class DatabaseConfig {
	
	public static String url = "jdbc:mysql://127.0.0.1:3306";
	public static String tableRating = "id13575215_lamdep.danhgia";
	public static String user = "root";
	public static String password = "";
	public static Properties properties = new Properties();
	
	public static void initDatabaseConfig()
	{
		properties.put("user", user);
		properties.put("password", password);
	}
}
