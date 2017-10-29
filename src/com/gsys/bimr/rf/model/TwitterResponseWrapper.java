package com.gsys.bimr.rf.model;

import java.util.List;

/**
 * @author GLK
 */
public class TwitterResponseWrapper {

	private List<TwitterDataWrapper> tweets;

	public List<TwitterDataWrapper> getTweets() {
		return tweets;
	}

	public void setTweets(List<TwitterDataWrapper> tweets) {
		this.tweets = tweets;
	}

}
