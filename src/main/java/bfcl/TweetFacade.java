package main.java.bfcl;

import java.util.List;

import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;

/**
 * @author GLK
 */
public interface TweetFacade {

	/**
	 * Retrieves tweets from twitter api and persist them into database
	 * 
	 * @param request
	 * @param pipeline
	 * @return
	 */
	void retrieveTweetsFromApi(TwitterRequestDTO request);

	/**
	 * Insert a list of tweets into database
	 * 
	 * @param tweets
	 */
	void persistTweets(List<TweetDTO> tweets);

	/**
	 * Retrieve tweets from database
	 * 
	 * @return
	 */
	List<TweetDTO> retrieveTweetsFromDB();

	/**
	 * Retrieve the most recent tweet id from the database
	 * 
	 */
	Long retrieveLastTweetId();
}
