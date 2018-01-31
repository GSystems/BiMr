package bimr.df;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import bimr.df.model.Tweet;
import bimr.df.model.TwitterRequest;
import bimr.df.model.TwitterResponse;

import javax.ejb.LocalBean;

public interface TweetRepo {

	/**
	 * Retrieve tweets from twitter api
	 * 
	 * @param request
	 * @return
	 */
	TwitterResponse retrieveTweets(TwitterRequest request);

	/**
	 * Insert a list of tweets into database
	 * 
	 * @param tweets
	 */
	void insertTweets(List<Tweet> tweets);

	/**
	 * Retrieve tweets from database
	 *
	 * @return
	 */
	Future<List<Tweet>> retrieveTweetsFromDB();

	/**
	 * Retrieve the most recent tweet id from the database
	 * 
	 */
	Future<List<Long>> retrieveLastTweetId();

	Future<List<Date>> retrieveMinDate();
}
