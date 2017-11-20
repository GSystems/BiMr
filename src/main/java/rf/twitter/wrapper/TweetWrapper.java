package com.gsys.bimr.rf.model;

import twitter4j.GeoLocation;

/**
 * @author GLK
 */
public class TwitterDataWrapper {

	private String user;

	private GeoLocation location;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public GeoLocation getLocation() {
		return location;
	}

	public void setLocation(GeoLocation location) {
		this.location = location;
	}

}
