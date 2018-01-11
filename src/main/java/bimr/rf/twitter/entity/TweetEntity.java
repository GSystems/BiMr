package bimr.rf.twitter.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import bimr.util.GeneralConstants;

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
	protected static final String FIND_LAST_TWEET_ID_QRY = "SELECT MIN(te.tweetId) FROM TweetEntity te";
	protected static final String FIND_MIN_DATE_QRY = "SELECT MIN(te.observationDate) FROM TweetEntity te";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tweetId;
	private String tweetMessage;
	private String latitude;
	private String longitude;
	private Date observationDate;

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
