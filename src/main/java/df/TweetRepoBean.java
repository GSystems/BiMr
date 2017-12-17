package main.java.df;

import java.util.List;

import javax.inject.Inject;

import main.java.df.mapper.MapMapper;
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
		return MapMapper.toTwitterResponseFromWrapper(
				twitterService.retrieveTweets(MapMapper.fromTwitterRequestToWrapper(request)));
	}

	@Override
	public void insertTweets(List<Tweet> tweets) {
		for (Tweet tweet : tweets) {
			twitterDAO.insertTweet(MapMapper.fromTweetToEntity(tweet));
		}
	}

	@Override
	public List<Tweet> retrieveTweetsFromDB() {
		return MapMapper.toTweetListFromEntity(twitterDAO.findAllTweets());
	}

}
