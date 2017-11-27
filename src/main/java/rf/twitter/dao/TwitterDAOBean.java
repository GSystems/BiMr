package main.java.rf.twitter.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import main.java.rf.BaseDAOBean;
import main.java.rf.twitter.entity.TweetEntity;

@Component
public class TwitterDAOBean extends BaseDAOBean<TweetEntity, Long> implements TwitterDAO {

	@Override
	public TweetEntity insertTweet(TweetEntity tweet) {
		return insert(tweet);
	}

	@Override
	public List<TweetEntity> findAllTweets() {
		return findAll();
	}

}
