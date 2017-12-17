package main.java.df;

import java.util.List;

import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;

public interface TweetRepo {

	/**
	 * Retrieve tweets from twitter api
	 * 
	 * @param request
	 * @return
	 */
	TwitterResponse retrieveTweets(TwitterRequest request);

	/**
	 * Persist tweets into database
	 * 
	 * @param tweets
	 */
	void insertTweets(List<Tweet> tweets);

	/**
	 * Retrieve tweets from database
	 *
	 * @return
	 */
	List<Tweet> retrieveTweetsFromDB();

}
