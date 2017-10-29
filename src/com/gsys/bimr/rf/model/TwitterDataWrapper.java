package com.gsys.bimr.rf.model;

import twitter4j.GeoLocation;

/**
 * @author GLK
 */
public class TwitterDataWrapper {

	private String user;
	private String message;
	private GeoLocation location;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GeoLocation getLocation() {
		return location;
	}

	public void setLocation(GeoLocation location) {
		this.location = location;
	}

}
