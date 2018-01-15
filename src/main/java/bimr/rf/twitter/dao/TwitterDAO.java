package bimr.rf.twitter.dao;

import java.util.Date;
import java.util.List;

import bimr.rf.BaseDAO;
import bimr.rf.twitter.entity.TweetEntity;

public interface TwitterDAO extends BaseDAO<TweetEntity, Long> {

	/**
	 * Insert a list of tweets into database
	 * 
	 * @param tweets
	 * @return
	 */
	void insertTweet(List<TweetEntity> tweets);

	/**
	 * Return all tweets from database
	 * 
	 * @return
	 */
	List<TweetEntity> findAllTweets();

	/**
	 * Retrieve the most recent tweet id from the database
	 * 
	 */
	List<Long> retrieveLastTweetId();

	List<Date> retrieveMinDate();
}
