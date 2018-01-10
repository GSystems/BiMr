package bimr.bfcl;

import java.util.List;

import bimr.bfcl.dto.EbirdDataDTO;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TwitterRequestDTO;

public interface ScheduleFacade {

	/**
	 * Retrieve tweets at every 15 minutes
	 */
	void twitterApiCallScheduled();

	/**
	 * Retrieves tweets from twitter api and persist them into database
	 *
	 * @param request
	 * @return
	 */
	void retrieveTweetsFromApi(TwitterRequestDTO request);

	/**
	 * Retrieve the most recent tweet id from the database
	 */
	Long retrieveLastTweetId();

	/**
	 * Insert a list of tweets into database
	 *
	 * @param tweets
	 */
	void persistTweets(List<TweetDTO> tweets);

	/**
	 * Insert a list of ebird data into the database
	 *
	 * @param ebirds
	 */
	void persistEbirdData(List<EbirdDataDTO> ebirds);

}
