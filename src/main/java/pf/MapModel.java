package main.java.pf;

import java.util.List;

import main.java.bfcl.dto.EBirdDataDTO;
import main.java.bfcl.dto.TweetDTO;

/**
 * @author GLK
 * this class will contain a single
 * model representation to populate the map
 */
public class MapModel {

	private List<TweetDTO> tweets;
	private List<EBirdDataDTO> ebirdData;

	public List<EBirdDataDTO> getEbirdData() {
		return ebirdData;
	}

	public void setEbirdData(List<EBirdDataDTO> ebirdData) {
		this.ebirdData = ebirdData;
	}

	public List<TweetDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TweetDTO> tweets) {
		this.tweets = tweets;
	}

}
