package main.java.rf.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.rf.transformer.DataTransformer;
import main.java.rf.twitter.wrapper.TweetWrapper;
import main.java.rf.twitter.wrapper.TwitterRequestWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;
import main.java.util.TwitterEnum;
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

	private static final Logger log = Logger.getLogger(TwitterServiceClientBean.class.getName());

	@Override
	public TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request) {
		TwitterResponseWrapper response = new TwitterResponseWrapper();
		List<TweetWrapper> tweets = new ArrayList<>();
		String hashtag = request.getHashtag();
		Long lastId = request.getLastTweetId();
		if (hashtag != null && !hashtag.equals("")) {
			tweets.addAll(DataTransformer.fromTwitterApiResponseToWrapper(retrieveTweetsFromApi(hashtag, lastId)));
		}
		response.setTweets(tweets);
		return response;
	}

	private List<Status> retrieveTweetsFromApi(String hashtag, Long sinceId) {
		List<Status> tweets = new ArrayList<>();
		ConfigurationBuilder configurationBuilder = credentialsSetup();
		Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

		Query query = new Query(hashtag);
		query.setSinceId(sinceId);
		query.setLang("en");
		QueryResult result = null;
		try {
			result = twitter.search(query);
			tweets.addAll(result.getTweets());
		} catch (TwitterException e) {
			log.severe("Couldn't connect: " + e);
		}
		return tweets;
	}

	private ConfigurationBuilder credentialsSetup() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true).setOAuthConsumerKey(TwitterEnum.CONSUMER_KEY.getCode())
				.setOAuthConsumerSecret(TwitterEnum.CONSUMER_SECRET.getCode())
				.setOAuthAccessToken(TwitterEnum.ACCESS_TOKEN.getCode())
				.setOAuthAccessTokenSecret(TwitterEnum.ACCESS_TOKEN_SECRET.getCode());
		return configurationBuilder;
	}

}
