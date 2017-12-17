package main.java.df.model;

/**
 * @author GLK
 */
public class TwitterRequest {

	private String hashtag;
	private Long lastTweetId;

	public TwitterRequest(String hashtag, Long lastTweetId) {
		this.hashtag = hashtag;
		this.lastTweetId = lastTweetId;
	}

	public String getHashtag() {
		return hashtag;
	}

	public Long getLastTweetId() {
		return lastTweetId;
	}

}
