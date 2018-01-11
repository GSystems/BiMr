package bimr.bfcl.dto;

import java.util.List;

/**
 * @author GLK
 */
public class TweetResponseDTO {

	private List<TweetDTO> tweets;

	public List<TweetDTO> getTweets() {
		return tweets;
	}

	public void setTweets(List<TweetDTO> tweets) {
		this.tweets = tweets;
	}

}
