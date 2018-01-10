package bimr.rf.twitter.wrapper;

import java.util.Date;

/**
 * @author GLK
 */
public class TweetWrapper {

	private Long tweetId;
	private String tweetMessage;
	private String latitude;
	private String longitude;
	private Date observationDate;

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getTweetMessage() {
		return tweetMessage;
	}

	public void setTweetMessage(String tweetMessage) {
		this.tweetMessage = tweetMessage;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Date getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}

}
