package bimr.bfcl;

public interface TweetScheduleFacade {

	/**
	 * Retrieve tweets at every 15 minutes
	 */
	void twitterApiCallScheduled();

}
