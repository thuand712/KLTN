package DropWizard;

public class RecommendServerConfiguration extends io.dropwizard.Configuration {

	public String myConfig;
	
	public String getMyConfig()
	{
		return myConfig;
	}
	
	public void setMyConfig(String myConfig)
	{
		this.myConfig = myConfig;
	}
	
}
