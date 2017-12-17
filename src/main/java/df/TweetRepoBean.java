package main.java.df;

import java.util.List;

import javax.inject.Inject;

import main.java.df.mapper.TweetMapper;
import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;
import main.java.rf.twitter.TwitterServiceClientBean;
import main.java.rf.twitter.dao.TwitterDAO;

public class TweetRepoBean implements TweetRepo {

	@Inject
	private TwitterServiceClientBean twitterService;

	@Inject
	private TwitterDAO twitterDAO;

	@Override
	public TwitterResponse retrieveTweets(TwitterRequest request) {
		twitterService = new TwitterServiceClientBean();
		return TweetMapper.toTwitterResponseFromWrapper(
				twitterService.retrieveTweets(TweetMapper.fromTwitterRequestToWrapper(request)));
	}

	@Override
	public void insertTweets(List<Tweet> tweets) {
		twitterDAO.insertTweet(TweetMapper.fromTweetListToEntity(tweets));
	}

	@Override
	public List<Tweet> retrieveTweetsFromDB() {
		return TweetMapper.toTweetListFromEntity(twitterDAO.findAllTweets());
	}

	@Override
	public Long retrieveLastTweetId() {
		return twitterDAO.retrieveLastTweetId();
	}

}
