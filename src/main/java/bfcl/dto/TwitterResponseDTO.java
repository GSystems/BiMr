package main.java.bfcl.dto;

import java.util.List;

/**
 * @author GLK
 */
public class TwitterResponseDTO {

	private List<TweetDTO> tweets;

	public List<TweetDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TweetDTO> tweets) {
		this.tweets = tweets;
	}

}
