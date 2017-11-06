package com.gsys.bimr.pf;

import java.util.List;

import com.gsys.bimr.bfcl.dto.EBirdDataDTO;
import com.gsys.bimr.bfcl.dto.TwitterDataDTO;

/**
 * @author GLK
 * this class will contain a single model representation to populate the map 
 */
public class MapModel {

	private List<TwitterDataDTO> tweets;
	
	private List<EBirdDataDTO> ebirdData;

	public List<EBirdDataDTO> getEbirdData() {
		return ebirdData;
	}

	public void setEbirdData(List<EBirdDataDTO> ebirdData) {
		this.ebirdData = ebirdData;
	}

	public List<TwitterDataDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TwitterDataDTO> tweets) {
		this.tweets = tweets;
	}
	
}
