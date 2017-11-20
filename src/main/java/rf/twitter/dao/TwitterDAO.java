package main.java.rf.twitter.dao;

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

}
