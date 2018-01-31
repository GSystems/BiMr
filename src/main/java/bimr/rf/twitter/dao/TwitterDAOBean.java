package bimr.rf.twitter.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import bimr.rf.BaseDAOBean;
import bimr.rf.twitter.entity.TweetEntity;

/**
 * @author GLK
 */
public class TwitterDAOBean extends BaseDAOBean<TweetEntity, Long> implements TwitterDAO {

	@Override
	public void insertTweet(List<TweetEntity> tweets) {
		for (TweetEntity tweet : tweets) {
//			insert(tweet);
			bang(tweet);
		}
	}

	@Override
	public List<TweetEntity> findAllTweets() {
		return findAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> retrieveLastTweetId() {
		Query query = getEntityManager().createNamedQuery(TweetEntity.FIND_LAST_TWEET_ID);
		return query.getResultList();
	}

	@Override
	public List<Date> retrieveMinDate() {
		Query query = getEntityManager().createNamedQuery(TweetEntity.FIND_MIN_DATE);
		return query.getResultList();
	}

}
