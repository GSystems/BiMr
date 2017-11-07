package com.gsys.bimr.rf.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gsys.bimr.rf.model.TwitterDataWrapper;
import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;
import com.gsys.bimr.rf.transformer.DataTransformer;
import com.gsys.bimr.util.GeneralConstants;

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

	public static final Logger LOGGER = Logger.getLogger(TwitterServiceClientBean.class.getName());

	@Override
	public TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request) {

		TwitterResponseWrapper response = new TwitterResponseWrapper();
		Map<String, TwitterDataWrapper> tweets = new HashMap<>();
		for (String hashtag : request.getHashtags()) {
			if(hashtag != null && !hashtag.equals("")) {
				tweets.putAll(DataTransformer.fromTwitterRawResponseToWrapper(callTwitterApi(hashtag)));
			}
		}
		response.setTweets(tweets);
		return response;
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
				for (Status t : tweets)
					if (t.getId() < lastID) {
						lastID = t.getId();
					}
			} catch (TwitterException te) {
				LOGGER.severe("Couldn't connect: " + te);
				break;
			}
			query.setMaxId(lastID - 1);
		}
		return result.getTweets();
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
