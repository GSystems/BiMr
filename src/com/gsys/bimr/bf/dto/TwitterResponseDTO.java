package com.gsys.bimr.bf.dto;

import java.util.List;

/**
 * @author GLK
 */
public class TwitterResponseDTO {

	private List<TwitterDataDTO> tweets;

	public List<TwitterDataDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TwitterDataDTO> tweets) {
		this.tweets = tweets;
	}

}
