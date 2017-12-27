package main.java.bfcl;

import java.util.List;

import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;

public interface ScheduleFacade {

	/**
	 * Retrieve tweets at every 15 minutes
	 */
	void twitterApiCallScheduled();

	/**
	 * Retrieves tweets from twitter api and persist them into database
	 * 
	 * @param request
	 * @param pipeline
	 * @return
	 */
	void retrieveTweetsFromApi(TwitterRequestDTO request);

	/**
	 * Retrieve the most recent tweet id from the database
	 * 
	 */
	Long retrieveLastTweetId();

	/**
	 * Insert a list of tweets into database
	 * 
	 * @param tweets
	 */
	void persistTweets(List<TweetDTO> tweets);

}