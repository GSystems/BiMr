package main.java.rf.twitter.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import main.java.util.GeneralConstants;

/**
 * @author GLK
 */

@Entity
@Table(schema = GeneralConstants.SCHEMA, name = "tweets")
@NamedQuery(name = TweetEntity.FIND_LAST_TWEET_ID, query = TweetEntity.FIND_LAST_TWEET_ID_QRY)
public class TweetEntity implements Serializable {

	private static final long serialVersionUID = -6863014092565181817L;

	public static final String FIND_LAST_TWEET_ID = "TweetEntity.findLastTweetId";
	protected static final String FIND_LAST_TWEET_ID_QRY = "SELECT MAX(te.id) FROM TweetEntity te";

	@Id
	private Long id;
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
