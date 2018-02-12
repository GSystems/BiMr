package bimr.df.model;

import java.time.LocalDate;

/**
 * @author GLK
 */
public class Tweet {

	private Long id;
	private Long tweetId;
	private String tweetMessage;
	private Double latitude;
	private Double longitude;
	private LocalDate observationDate;
	private TwitterUser user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public LocalDate getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(LocalDate observationDate) {
		this.observationDate = observationDate;
	}

	public TwitterUser getUser() {
		return user;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}
}
