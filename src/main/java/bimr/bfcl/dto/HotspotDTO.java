package bimr.bfcl.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HotspotDTO implements Comparable<HotspotDTO> {
	private String informationSource;
	private List<String> birdSpecies;
	LocationDTO location;
	private String hotspotId;
	private Integer howMany;
	private LocalDateTime observationDate;
	private String tweetMessage;
	private Long tweetId;
	private String link;
	private String author;
	private String language;
	private TwitterUserDTO user;

	public String getInformationSource() {
		return informationSource;
	}

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	public List<String> getBirdSpecies() {
		return birdSpecies;
	}

	public void setBirdSpecies(List<String> birdSpecies) {
		this.birdSpecies = birdSpecies;
	}

	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	public String getHotspotId() {
		return hotspotId;
	}

	public void setHotspotId(String hotspotId) {
		this.hotspotId = hotspotId;
	}

	public Integer getHowMany() {
		return howMany;
	}

	public void setHowMany(Integer howMany) {
		this.howMany = howMany;
	}

	public LocalDateTime getObservationDate() {
		return observationDate;
	}

	public void setObservationDate(LocalDateTime observationDate) {
		this.observationDate = observationDate;
	}

	public String getTweetMessage() {
		return tweetMessage;
	}

	public void setTweetMessage(String tweetMessage) {
		this.tweetMessage = tweetMessage;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public TwitterUserDTO getUser() {
		return user;
	}

	public void setUser(TwitterUserDTO user) {
		this.user = user;
	}

	@Override
	public int compareTo(HotspotDTO o) {
		return observationDate.compareTo(o.observationDate);
	}
}
