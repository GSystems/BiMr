package main.java.df.mapper;

import java.util.ArrayList;
import java.util.List;

import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;
import main.java.rf.twitter.entity.TweetEntity;
import main.java.rf.twitter.wrapper.TweetWrapper;
import main.java.rf.twitter.wrapper.TwitterRequestWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;

/**
 * @author GLK
 */
public class TweetMapper {

	private TweetMapper() {
	}

	public static TwitterRequestWrapper fromTwitterRequestToWrapper(TwitterRequest request) {
		return new TwitterRequestWrapper(request.getHashtag(), request.getLastTweetId());
	}

	public static TwitterResponse toTwitterResponseFromWrapper(TwitterResponseWrapper responseWrapper) {
		TwitterResponse response = new TwitterResponse();
		response.setTweets(toTwitterListFromWrapper(responseWrapper.getTweets()));
		return response;
	}

	private static List<Tweet> toTwitterListFromWrapper(List<TweetWrapper> tweetsWrapper) {
		List<Tweet> tweets = new ArrayList<>();
		for (TweetWrapper tweetWrapper : tweetsWrapper) {
			tweets.add(toTwitterFromWrapper(tweetWrapper));
		}
		return tweets;
	}

	private static Tweet toTwitterFromWrapper(TweetWrapper tweetWrapper) {
		Tweet tweet = new Tweet();
		tweet.setTweetId(tweetWrapper.getTweetId());
		tweet.setLatitude(tweetWrapper.getLatitude());
		tweet.setLongitude(tweetWrapper.getLongitude());
		tweet.setObservationDate(tweetWrapper.getObservationDate());
		tweet.setTweetMessage(tweetWrapper.getTweetMessage());
		return tweet;
	}

	public static List<TweetEntity> fromTweetListToEntity(List<Tweet> tweets) {
		List<TweetEntity> entityList = new ArrayList<>();
		for (Tweet tweet : tweets) {
			entityList.add(fromTweetToEntity(tweet));
		}
		return entityList;
	}

	public static TweetEntity fromTweetToEntity(Tweet tweet) {
		TweetEntity tweetEntity = new TweetEntity();
		tweetEntity.setId(tweet.getId());
		tweetEntity.setTweetId(tweet.getTweetId());
		tweetEntity.setLatitude(tweet.getLatitude());
		tweetEntity.setLongitude(tweet.getLongitude());
		tweetEntity.setObservationDate(tweet.getObservationDate());
		tweetEntity.setTweetMessage(tweet.getTweetMessage());
		return tweetEntity;
	}

	public static List<Tweet> toTweetListFromEntity(List<TweetEntity> entities) {
		List<Tweet> tweets = new ArrayList<>();
		for (TweetEntity entity : entities) {
			tweets.add(toTweetFromEntity(entity));
		}
		return tweets;
	}

	private static Tweet toTweetFromEntity(TweetEntity tweetEntity) {
		Tweet tweet = new Tweet();
		tweet.setId(tweetEntity.getId());
		tweet.setTweetId(tweetEntity.getTweetId());
		tweet.setLatitude(tweetEntity.getLatitude());
		tweet.setLongitude(tweetEntity.getLongitude());
		tweet.setObservationDate(tweetEntity.getObservationDate());
		tweet.setTweetMessage(tweetEntity.getTweetMessage());
		return tweet;
	}

}
