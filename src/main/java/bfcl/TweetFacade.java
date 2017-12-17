package main.java.bfcl;

import java.util.List;

import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;

public interface TweetFacade {

	/**
	 * Retrieves tweets from twitter api and persist them into database
	 * 
	 * @param request
	 * @return
	 */
	void retrieveTweetsFromApi(TwitterRequestDTO request);

	/**
	 * Persist tweets into database
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
	 * Retrieve tweets from API automatically by calling retrieveTweetsFromApi
	 * method at every 15 minutes
	 * 
	 */
	void twitterApiCallScheduled();

}
