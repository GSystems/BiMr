package main.java.rf.twitter.dao;

import java.util.List;

import main.java.rf.BaseDAO;
import main.java.rf.twitter.entity.TweetEntity;

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

}
