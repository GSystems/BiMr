package bimr.rf.twitter.entity;

import bimr.util.GeneralConstants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author GLK
 */

@Entity
@Table(schema = GeneralConstants.SCHEMA, name = "tweets")
@NamedQueries({ @NamedQuery(name = TweetEntity.FIND_LAST_TWEET_ID, query = TweetEntity.FIND_LAST_TWEET_ID_QRY),
		@NamedQuery(name = TweetEntity.FIND_MIN_DATE, query = TweetEntity.FIND_MIN_DATE_QRY)}
)

public class TweetEntity implements Serializable {

	private static final long serialVersionUID = -6863014092565181817L;

	public static final String FIND_LAST_TWEET_ID = "TweetEntity.findLastTweetId";
	public static final String FIND_MIN_DATE = "TweetEntity.findMinDate";
	protected static final String FIND_LAST_TWEET_ID_QRY = "SELECT MAX(te.tweetId) FROM TweetEntity te";
	protected static final String FIND_MIN_DATE_QRY = "SELECT MIN(te.observationDate) FROM TweetEntity te";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tweetId;
	private String tweetMessage;
	private Double latitude;
	private Double longitude;
	private LocalDate observationDate;

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

}
