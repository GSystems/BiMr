package com.gsys.bimr.rf.model;

import java.util.Map;

/**
 * @author GLK
 */
public class TwitterResponseWrapper {

	private Map<String, TwitterDataWrapper> tweets;

	public Map<String, TwitterDataWrapper> getTweets() {
		return tweets;
	}

	public void setTweets(Map<String, TwitterDataWrapper> tweets) {
		this.tweets = tweets;
	}

}
