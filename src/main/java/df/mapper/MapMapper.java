package main.java.df.mapper;

import java.util.ArrayList;
import java.util.List;

import main.java.df.model.EBirdData;
import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;
import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;
import main.java.rf.ebird.wrapper.EBirdDataWrapper;
import main.java.rf.ebird.wrapper.EBirdRequestWrapper;
import main.java.rf.ebird.wrapper.EBirdResponseWrapper;
import main.java.rf.twitter.entity.TweetEntity;
import main.java.rf.twitter.wrapper.TweetWrapper;
import main.java.rf.twitter.wrapper.TwitterRequestWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;

public class MapMapper {

	private MapMapper() {
	}

	public static TwitterRequestWrapper fromTwitterRequestToWrapper(TwitterRequest request) {
		return new TwitterRequestWrapper(request.getHashtag());
	}

	public static EBirdRequestWrapper fromEBirdRequestToWrapper(EBirdRequest request) {
		EBirdRequestWrapper requestWrapper = new EBirdRequestWrapper();
		requestWrapper.setRequestUriPattern(request.getRequestUriPattern());
		return requestWrapper;
	}

	public static TwitterResponse toTwitterResponseFromWrapper(TwitterResponseWrapper responseWrapper) {
		TwitterResponse response = new TwitterResponse();
		response.setTweets(toTwitterDataListFromWrapper(responseWrapper.getTweets()));
		return response;
	}

	private static List<Tweet> toTwitterDataListFromWrapper(List<TweetWrapper> tweetsWrapper) {
		List<Tweet> tweets = new ArrayList<>();
		for (TweetWrapper tweetWrapper : tweetsWrapper) {
			Tweet tweet = toTwitterDataFromWrapper(tweetWrapper);
			tweets.add(tweet);
		}
		return tweets;
	}

	private static Tweet toTwitterDataFromWrapper(TweetWrapper tweetWrapper) {
		Tweet tweet = new Tweet();
		tweet.setId(tweetWrapper.getId());
		tweet.setLatitude(tweetWrapper.getLatitude());
		tweet.setLongitude(tweetWrapper.getLongitude());
		tweet.setObservationDate(tweetWrapper.getObservationDate());
		tweet.setTweetMessage(tweetWrapper.getTweetMessage());
		return tweet;
	}

	public static TweetEntity fromTweetToEntity(Tweet tweet) {
		TweetEntity tweetEntity = new TweetEntity();
		tweetEntity.setId(tweet.getId());
		tweetEntity.setLatitude(tweet.getLatitude());
		tweetEntity.setLongitude(tweet.getLongitude());
		tweetEntity.setObservationDate(tweet.getObservationDate());
		tweetEntity.setTweetMessage(tweet.getTweetMessage());
		return tweetEntity;
	}

	// TwitterUserEntity userEntity = new TwitterUserEntity();
	// userEntity.setEmail(user.getEmail());
	// userEntity.setId(user.getId());
	// userEntity.setScreenName(user.getScreenName());
	// userEntity.setUrl(user.getUrl());
	public static List<Tweet> toTweetListFromEntity(List<TweetEntity> entities) {
		List<Tweet> tweets = new ArrayList<>();
			Tweet tweet = toTweetFromEntity(entity);
			tweets.add(tweet);
		}
		return tweets;
	}

	private static Tweet toTweetFromEntity(TweetEntity tweetEntity) {
		Tweet tweet = new Tweet();
		tweet.setId(tweetEntity.getId());
		tweet.setLatitude(tweetEntity.getLatitude());
		tweet.setLongitude(tweetEntity.getLongitude());
		tweet.setObservationDate(tweetEntity.getObservationDate());
		tweet.setTweetMessage(tweetEntity.getTweetMessage());
		return tweet;
	}

	public static EBirdResponse toEbirdsResponseFromWrapper(EBirdResponseWrapper responseWrapper) {
		EBirdResponse response = new EBirdResponse();
		List<EBirdData> data = new ArrayList<>();
		if (responseWrapper.getEbirdData() != null) {
			data = toEbirdDataFromWrapper(responseWrapper.getEbirdData());
		}
		response.setEbirdData(data);
		return response;
	}

	private static List<EBirdData> toEbirdDataFromWrapper(List<EBirdDataWrapper> ebirdsData) {
		List<EBirdData> ebirds = new ArrayList<>();
		for (EBirdDataWrapper ebirdWrapper : ebirdsData) {
			EBirdData ebird = new EBirdData();
			ebird.setCommonName(ebirdWrapper.getCommonName());
			ebird.setCountryName(ebirdWrapper.getCountryName());
			ebird.setLatitude(ebirdWrapper.getLatitude());
			ebird.setLocalityName(ebirdWrapper.getLocalityName());
			ebird.setLongitude(ebirdWrapper.getLongitude());
			ebird.setObservationDate(ebirdWrapper.getObservationDate());
			ebird.setScientificName(ebirdWrapper.getScientificName());
			ebird.setStateName(ebirdWrapper.getStateName());
			ebird.setUserDisplayName(ebirdWrapper.getUserDisplayName());
			ebirds.add(ebird);
		}
		return ebirds;
	}

}
