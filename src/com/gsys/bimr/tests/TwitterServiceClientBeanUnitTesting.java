package com.gsys.bimr.tests;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gsys.bimr.rf.model.TwitterDataWrapper;
import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;
import com.gsys.bimr.util.GeneralConstants;

import twitter4j.GeoLocation;

public class TwitterServiceClientBeanUnitTesting {

	@Test
	public void testRetrieveTweets() {

		TwitterRequestWrapper request = new TwitterRequestWrapper();
		request.setHashtags(generateHashTags());

		TwitterResponseWrapper response = generateTweets();

		assertNotNull(response);
		assertNotNull(response.getTweets());

		for (Map.Entry<String, TwitterDataWrapper> entry : response.getTweets().entrySet()) {
			assertNotNull(entry.getValue().getLocation());
			assertNotNull(entry.getValue().getUser());
			assertNotNull(entry.getKey());
		}
	}

	private TwitterResponseWrapper generateTweets() {
		TwitterResponseWrapper response = new TwitterResponseWrapper();
		Map<String, TwitterDataWrapper> tweets = new HashMap<>();
		TwitterDataWrapper tweet1 = new TwitterDataWrapper();
		TwitterDataWrapper tweet2 = new TwitterDataWrapper();
		TwitterDataWrapper tweet3 = new TwitterDataWrapper();
		GeoLocation location = new GeoLocation(2D, 0D);

		tweet1.setLocation(location);
		tweet1.setUser("user1");

		tweet2.setLocation(location);
		tweet2.setUser("user2");

		tweet3.setLocation(location);
		tweet3.setUser("user3");

		tweets.put("message1", tweet1);
		tweets.put("message2", tweet2);
		tweets.put("message3", tweet3);
		response.setTweets(tweets);
		return response;
	}

	private List<String> generateHashTags() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add(GeneralConstants.TWITTER_BIRDMIGRATION);
		return hashtags;
	}

}
