package Database;
public class Rate {
	public int itemId;
	public float rate;
	
	public Rate(int itemId, float Rate)
	{
		this.itemId = itemId;	
		this.rate = Rate;
	}
	
	public void printRate()
	{
		System.out.println("itemId= " + this.itemId +"\trate= "+ this.rate);
	}
}
