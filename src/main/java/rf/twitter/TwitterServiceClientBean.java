package main.java.rf.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.rf.transformer.DataTransformer;
import main.java.rf.twitter.wrapper.TweetWrapper;
import main.java.rf.twitter.wrapper.TwitterRequestWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;
import main.java.util.GeneralConstants;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author GLK
 */
public class TwitterServiceClientBean implements TwitterServiceClient {

	private static final Logger LOGGER = Logger.getLogger(TwitterServiceClientBean.class.getName());

	@Override
	public TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request) {

		TwitterResponseWrapper response = new TwitterResponseWrapper();
		List<TweetWrapper> tweets = new ArrayList<>();
		String hashtag = request.getHashtag();
		if (hashtag != null && !hashtag.equals("")) {
			tweets.addAll(DataTransformer.fromTwitterApiResponseToWrapper(callTwitterApi(hashtag)));
		}
		response.setTweets(tweets);
		return response;
	}

	private List<Status> retrieveTweetsFromApi(String hashtag) {
		List<Status> tweets = new ArrayList<>();
		ConfigurationBuilder configurationBuilder = credentialsSetup();
		Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

		Query query = new Query(hashtag);
		Integer numberOfTweets = GeneralConstants.MAX_NUMBER_OF_TWEETS;
		long sinceId = 930464129936175104l;
		long lastId = 0l;
		query.setSinceId(sinceId);
		QueryResult result = null;
		while (tweets.size() < numberOfTweets) {
			try {
				result = twitter.search(query);
				tweets.addAll(result.getTweets());
				if (!tweets.isEmpty()) {
					lastId = tweets.get(tweets.size() - 1).getId();
				} else {
					break;
				}
			} catch (TwitterException e) {
				LOGGER.severe("Couldn't connect: " + e);
				break;
			} finally {
				query.setSinceId(lastId);
			}
		}
		return tweets;
	}

	private List<Status> callTwitterApi(String hashtag) {
		ConfigurationBuilder configurationBuilder = credentialsSetup();
		Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

		Query query = new Query(hashtag);
		Integer numberOfTweets = GeneralConstants.MAX_NUMBER_OF_TWEETS;
		Long lastID = Long.MAX_VALUE;
		List<Status> tweets = new ArrayList<>();
		QueryResult result = null;

		while (tweets.size() < numberOfTweets) {
			if (numberOfTweets - tweets.size() > GeneralConstants.MAX_NUMBER_OF_TWEETS) {
				query.setCount(GeneralConstants.MAX_NUMBER_OF_TWEETS);
			} else {
				query.setCount(numberOfTweets - tweets.size());
			}
			try {
				result = twitter.search(query);
				tweets.addAll(result.getTweets());
				for (Status t : tweets) {
					if (t.getId() < lastID) {
						lastID = t.getId();
					}
				}
			} catch (TwitterException e) {
				LOGGER.severe("Couldn't connect: " + e);
				break;
			} finally {
				query.setMaxId(lastID - 1);
			}
		}
		return tweets;
	}

	private ConfigurationBuilder credentialsSetup() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(GeneralConstants.CONSUMER_KEY)
				.setOAuthConsumerSecret(GeneralConstants.CONSUMER_SECRET)
				.setOAuthAccessToken(GeneralConstants.ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(GeneralConstants.ACCESS_TOKEN_SECRET);
		return configurationBuilder;
	}

}
