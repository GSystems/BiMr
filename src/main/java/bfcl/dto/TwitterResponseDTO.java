package com.gsys.bimr.bfcl.dto;

import java.util.Map;

/**
 * @author GLK
 */
public class TwitterResponseDTO {

	private Map<String, TwitterDataDTO> tweets;

	public Map<String, TwitterDataDTO> getTweets() {
		return tweets;
	}

	public void setTweets(Map<String, TwitterDataDTO> tweets) {
		this.tweets = tweets;
	}

}
