package bimr.bfcl.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author GLK
 */
public class TweetDTO {

	private Long id;
	private Long tweetId;
	private String tweetMessage;
	private Double latitude;
	private Double longitude;
	private LocalDateTime observationDate;
	private TwitterUserDTO user;

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

	public LocalDateTime getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(LocalDateTime observationDate) {
		this.observationDate = observationDate;
	}

	public TwitterUserDTO getUser() {
		return user;
	}

	public void setUser(TwitterUserDTO user) {
		this.user = user;
	}
}
