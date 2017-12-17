package main.java.bfcl.dto;

/**
 * @author GLK
 */
public class TwitterRequestDTO {

	private String hashtag;
	private Long lastTweetId;

	public TwitterRequestDTO(String hashtag, Long lastTweetId) {
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
