package main.java.bfcl;

import java.util.List;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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
	void retrieveTweetsFromApi(TwitterRequestDTO request, StanfordCoreNLP pipeline);

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
	 * Retrieve tweets from API automatically by calling retrieveTweetsFromApi
	 * method at every 15 minutes
	 * 
	 * @param pipeline
	 */
	void twitterApiCallScheduled(StanfordCoreNLP pipeline);

	/**
	 * Retrieve the most recent tweet id from the database
	 * 
	 */
	Long retrieveLastTweetId();
}
