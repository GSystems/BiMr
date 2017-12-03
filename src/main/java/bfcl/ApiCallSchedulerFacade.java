package main.java.bfcl;

public interface ApiCallSchedulerFacade {

	/**
	 * Retrieve tweets from API automatically by calling retrieveTweetsFromApi
	 * method at every 15 minutes
	 * 
	 */
	void twitterApiCallScheduler();

}
