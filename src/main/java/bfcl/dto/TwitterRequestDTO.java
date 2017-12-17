package main.java.bfcl.dto;

/**
 * @author GLK
 */
public class TwitterRequestDTO {

	private String hashtag;

	private String lastTweetId;

	public TwitterRequestDTO(String hashtag) {
		this.hashtag = hashtag;
	}

	public String getHashtag() {
		return hashtag;
	}

	public String getLastTweetId() {
		return lastTweetId;
	}

	public void setLastTweetId(String lastTweetId) {
		this.lastTweetId = lastTweetId;
	}

}
