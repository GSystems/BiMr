package com.gsys.bimr.df.model;

import twitter4j.GeoLocation;

/**
 * @author GLK
 */
public class TwitterData {

	private String user;

	private GeoLocation location;

	public TwitterData(String user, GeoLocation location) {
		this.user = user;
		this.location = location;
	}

	public String getUser() {
		return user;
	}

	public GeoLocation getLocation() {
		return location;
	}

}
