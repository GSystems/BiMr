package main.java.df;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
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
	public Future<TwitterResponse> retrieveTweets(TwitterRequest request) {
		twitterService = new TwitterServiceClientBean();
		return new AsyncResult<>(TweetMapper.toTwitterResponseFromWrapper(
				twitterService.retrieveTweets(TweetMapper.fromTwitterRequestToWrapper(request))));
	}

	@Override
	public void insertTweets(List<Tweet> tweets) {
		twitterDAO.insertTweet(TweetMapper.fromTweetListToEntity(tweets));
	}

	@Override
	public Future<List<Tweet>> retrieveTweetsFromDB() {
		return new AsyncResult<>(TweetMapper.toTweetListFromEntity(twitterDAO.findAllTweets()));
	}

	@Override
	public Future<List<Long>> retrieveLastTweetId() {
		return new AsyncResult<>(twitterDAO.retrieveLastTweetId());
	}

}
