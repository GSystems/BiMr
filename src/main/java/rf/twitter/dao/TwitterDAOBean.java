package main.java.rf.twitter.dao;

import java.util.List;

import javax.persistence.Query;

import main.java.rf.BaseDAOBean;
import main.java.rf.twitter.entity.TweetEntity;
import main.java.util.GeneralConstants;

/**
 * @author GLK
 */
public class TwitterDAOBean extends BaseDAOBean<TweetEntity, Long> implements TwitterDAO {

	@Override
	public void insertTweet(List<TweetEntity> tweets) {
		for (TweetEntity tweet : tweets) {
			insert(tweet);
		}
	}

	@Override
	public List<TweetEntity> findAllTweets() {
		return findAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long retrieveLastTweetId() {
		Query query = getEntityManager().createNamedQuery(TweetEntity.FIND_LAST_TWEET_ID);
		List<TweetEntity> result = query.getResultList();
		if (result.get(0) == null) {
			return GeneralConstants.DEFAULT_SINCE_ID;
		}
		return result.get(0).getTweetId();
	}

}
