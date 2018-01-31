package bimr.df.model;

public class TwitterUser {
	private String id;
	private String email;
	private String location;
	private String screenName;
	private String isGeoEnabled;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String isGeoEnabled() {
		return isGeoEnabled;
	}

	public void setIsGeoEnabled(String isGeoEnabled) {
		this.isGeoEnabled = isGeoEnabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
