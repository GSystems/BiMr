package com.gsys.bimr.pf;

import java.util.List;

import com.gsys.bimr.bfcl.dto.TwitterDataDTO;

/**
 * @author GLK
 * this class will contain a single model representation to populate the map 
 */
public class MapModel {

	private List<TwitterDataDTO> tweets;

	public List<TwitterDataDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TwitterDataDTO> tweets) {
		this.tweets = tweets;
	}
	
}
