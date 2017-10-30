package com.gsys.bimr.rf.twitter;

import java.util.logging.Logger;

import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;
import com.gsys.bimr.rf.transformer.DataTransformer;
import com.gsys.bimr.util.GeneralConstants;

import twitter4j.Query;
import twitter4j.QueryResult;
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

	    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
	    configurationBuilder.setDebugEnabled(true)
	            .setOAuthConsumerKey(GeneralConstants.CONSUMER_KEY)
	            .setOAuthConsumerSecret(GeneralConstants.CONSUMER_SECRET)
	            .setOAuthAccessToken(GeneralConstants.ACCESS_TOKEN)
	            .setOAuthAccessTokenSecret(GeneralConstants.ACCESS_TOKEN_SECRET);

		TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
		Twitter twitter = twitterFactory.getInstance();
		Query query = new Query(request.getHashtag());
		QueryResult result;
		try {
			result = twitter.search(query);
			response.setTweets(DataTransformer.fromTwitterRawResponseToWrapper(result.getTweets()));
		} catch (TwitterException e) {
			LOGGER.severe("Unable to connect: " + e.getErrorMessage());
		}

//		Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
//		Query query = new Query(request.getHashtag());
//
//		Integer numberOfTweets = GeneralConstants.MAX_NUMBER_OF_TWEETS;
//		Long lastID = Long.MAX_VALUE;
//		List<Status> tweets = new ArrayList<>();
//
//		while (tweets.size() < numberOfTweets) {
//			if (numberOfTweets - tweets.size() > 100) {
//				query.setCount(100);
//			} else {
//				query.setCount(numberOfTweets - tweets.size());
//			}
//			try {
//				QueryResult result = twitter.search(query);
//				tweets.addAll(result.getTweets());
//
//				for (Status t : tweets)
//					if (t.getId() < lastID) {
//						lastID = t.getId();
//					}
//			} catch (TwitterException te) {
//				LOGGER.severe("Couldn't connect: " + te);
//				break;
//			}
//			query.setMaxId(lastID - 1);
//		}

		return response;
	}

}
