package com.gsys.bimr.tests;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;
import com.gsys.bimr.rf.twitter.TwitterServiceClientBean;
import com.gsys.bimr.util.GeneralConstants;

public class TwitterServiceClientBeanUnitTesting {

	@Test
	public void testRetrieveTweets() {

		TwitterRequestWrapper request = new TwitterRequestWrapper();
		request.setHashtags(generateHashTags());

		TwitterServiceClientBean client = new TwitterServiceClientBean();
		TwitterResponseWrapper response = client.retrieveTweets(request);

		assertNotNull(response);
		assertNotNull(response.getTweets());
	}
	
	private List<String> generateHashTags() {
		List<String> hashtags = new ArrayList<>();
		hashtags.add(GeneralConstants.TWITTER_BIRDMIGRATION);
		return hashtags;
	}

}
