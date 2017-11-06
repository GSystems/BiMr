package com.gsys.bimr.pf;

import java.util.Map;

import com.gsys.bimr.bfcl.dto.TwitterDataDTO;

/**
 * @author GLK
 * this class will contain a single model representation to populate the map 
 */
public class MapModel {

	private Map<String, TwitterDataDTO> tweets;

	public Map<String, TwitterDataDTO> getTweets() {
		return tweets;
	}

	public void setTweets(Map<String, TwitterDataDTO> tweets) {
		this.tweets = tweets;
	}

}
