package bimr.rf.twitter.wrapper;

import java.util.Date;

/**
 * @author GLK
 */
public class TwitterRequestWrapper {

	private String hashtag;
	private Long lastTweetId;
	private Date untilDate;

	public TwitterRequestWrapper(String hashtag, Long lastTweetId, Date untilDate) {
		this.hashtag = hashtag;
		this.lastTweetId = lastTweetId;
		this.untilDate = untilDate;
	}

	public String getHashtag() {
		return hashtag;
	}

	public Long getLastTweetId() {
		return lastTweetId;
	}

	public Date getUntilDate() {
		return untilDate;
	}
}
