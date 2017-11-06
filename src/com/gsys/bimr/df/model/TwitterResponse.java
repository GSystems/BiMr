package com.gsys.bimr.df.model;

import java.util.Map;

/**
 * @author GLK
 */
public class TwitterResponse {

	private Map<String, TwitterData> tweets;

	public Map<String, TwitterData> getTweets() {
		return tweets;
	}

	public void setTweets(Map<String, TwitterData> tweets) {
		this.tweets = tweets;
	}

}
