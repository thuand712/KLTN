package Recommend;

import java.io.Serializable;

import org.apache.spark.sql.Row;

public class UserRating implements Serializable {
  private int userId;
  private int itemId;
  private float rating;

  public UserRating() {}

  public UserRating(int userId, int itemId, float rating) {
    this.userId = userId;
    this.itemId = itemId;
    this.rating = rating;
  }

  public int getUserId() {
    return userId;
  }

  public int getItemId() {
    return itemId;
  }

  public float getRating() {
    return rating;
  }


  public static UserRating parseRating(String str) {
    String[] fields = str.split("\t");
    if (fields.length != 4) {
      throw new IllegalArgumentException("Each line must contain 4 fields");
    }
    int userId = Integer.parseInt(fields[0]);
    int itemId = Integer.parseInt(fields[1]);
    float rating = Float.parseFloat(fields[2]);
    return new UserRating(userId, itemId, rating);
  }
  
  public static UserRating parseRating(Row row) {
	    return new UserRating(row.getInt(1), row.getInt(0), row.getInt(4));
	  }
  
}
