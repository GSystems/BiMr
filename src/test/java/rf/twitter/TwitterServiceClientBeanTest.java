package test.java.rf.twitter;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import main.java.rf.twitter.wrapper.TweetWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;

public class TwitterServiceClientBeanTest {

	@Test
	public void testRetrieveTweets() {

		TwitterResponseWrapper response = generateTweets();

		assertNotNull(response);
		assertNotNull(response.getTweets());

		for (TweetWrapper tweet : response.getTweets()) {
			assertNotNull(tweet.getLatitude());
			assertNotNull(tweet.getLongitude());
			assertNotNull(tweet.getTweetId());
			assertNotNull(tweet.getTweetMessage());
		}
	}

	private TwitterResponseWrapper generateTweets() {
		TwitterResponseWrapper response = new TwitterResponseWrapper();
		List<TweetWrapper> tweets = new ArrayList<>();
		TweetWrapper tweet1 = new TweetWrapper();
		TweetWrapper tweet2 = new TweetWrapper();
		TweetWrapper tweet3 = new TweetWrapper();
		String latitude = "123";
		String longitude = "321";

		tweet1.setLatitude(latitude);
		tweet1.setLongitude(longitude);
		tweet1.setTweetId(1L);
		tweet1.setTweetMessage("message1");

		tweet2.setLatitude(latitude);
		tweet2.setLongitude(longitude);
		tweet2.setTweetId(2L);
		tweet2.setTweetMessage("message2");

		tweet3.setLatitude(latitude);
		tweet3.setLongitude(longitude);
		tweet3.setTweetId(3L);
		tweet3.setTweetMessage("message3");

		tweets.add(tweet1);
		tweets.add(tweet2);
		tweets.add(tweet3);
		response.setTweets(tweets);
		return response;
	}

}
