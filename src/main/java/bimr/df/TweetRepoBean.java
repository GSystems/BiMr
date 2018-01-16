package bimr.df;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import bimr.df.mapper.TweetMapper;
import bimr.df.model.Tweet;
import bimr.df.model.TwitterRequest;
import bimr.df.model.TwitterResponse;
import bimr.rf.twitter.TwitterServiceClientBean;
import bimr.rf.twitter.dao.TwitterDAO;

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
	public Future<List<Tweet>> retrieveTweetsFromDB() {
		return new AsyncResult<>(TweetMapper.toTweetListFromEntity(twitterDAO.findAllTweets()));
	}

	@Override
	public Future<List<Long>> retrieveLastTweetId() {
		return new AsyncResult<>(twitterDAO.retrieveLastTweetId());
	}

	@Override
	public Future<List<Date>> retrieveMinDate() {
		return new AsyncResult<>(twitterDAO.retrieveMinDate());
	}

}
