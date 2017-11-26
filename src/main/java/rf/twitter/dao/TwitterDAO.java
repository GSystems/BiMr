package main.java.rf.twitter.dao;

import java.util.List;

import main.java.rf.BaseDAO;
import main.java.rf.twitter.entity.TweetEntity;

public interface TwitterDAO extends BaseDAO<TweetEntity, Long> {

	/**
	 * Insert a tweet into database
	 * 
	 * @param tweets
	 * @return
	 */
	TweetEntity insertTweet(TweetEntity tweets);

	/**
	 * Return all tweets from database
	 * 
	 * @return
	 */
	List<TweetEntity> findAllTweets();

}
