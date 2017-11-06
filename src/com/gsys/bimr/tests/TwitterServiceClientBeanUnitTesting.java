package com.gsys.bimr.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import com.gsys.bimr.rf.model.TwitterDataWrapper;
import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;
import com.gsys.bimr.rf.twitter.TwitterServiceClientBean;

public class TwitterServiceClientBeanUnitTesting {

	@Test
	public void testRetrieveTweets() {

		TwitterRequestWrapper request = new TwitterRequestWrapper();
		request.setHashtag("birdMigration");

		TwitterServiceClientBean client = new TwitterServiceClientBean();
		TwitterResponseWrapper response = client.retrieveTweets(request);
		
		assertNotNull(response);
		
		response.getTweets();
		
		for(TwitterDataWrapper tweet: response.getTweets()) {
			assertNotNull(tweet);
			assertNotNull(tweet.getLocation());
			assertNotNull(tweet.getMessage());	
			assertNotNull(tweet.getUser());
		}

	}
	
	@Test
	public void testRetrieveTweetsContent() {
		
		String hashTag = "birdMigration";
		
		TwitterRequestWrapper request = new TwitterRequestWrapper();
		request.setHashtag(hashTag);
		
		TwitterServiceClientBean client = new TwitterServiceClientBean();
		TwitterResponseWrapper response = client.retrieveTweets(request);
		
		assertNotNull(response);
		
		response.getTweets();
		
		for(TwitterDataWrapper tweet: response.getTweets()) {
			if(!tweet.getMessage().contains(hashTag))
				assertNull(tweet.getMessage());
		}
	}

}
