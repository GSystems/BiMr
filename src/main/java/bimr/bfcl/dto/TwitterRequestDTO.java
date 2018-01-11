package bimr.bfcl.dto;

import java.util.Date;

/**
 * @author GLK
 */
public class TwitterRequestDTO {

	private String hashtag;
	private Long lastTweetId;
	private Date untilDate;

	public TwitterRequestDTO(String hashtag, Long lastTweetId, Date untilDate) {
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
