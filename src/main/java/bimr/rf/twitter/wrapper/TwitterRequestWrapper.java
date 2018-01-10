package bimr.rf.twitter.wrapper;

/**
 * @author GLK
 */
public class TwitterRequestWrapper {

	private String hashtag;
	private Long lastTweetId;

	public TwitterRequestWrapper(String hashtag, Long lastTweetId) {
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
