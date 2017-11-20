package main.java.rf.twitter.wrapper;

import java.util.Date;

/**
 * @author GLK
 */
public class TweetWrapper {

	private String id;
	private String tweetMessage;
	private String latitude;
	private String longitude;
	private Date observationDate;
	private TwitterUserWrapper user;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public TwitterUserWrapper getUser() {
		return user;
	}

	public void setUser(TwitterUserWrapper user) {
		this.user = user;
	}

}
