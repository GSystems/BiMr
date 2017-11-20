package com.gsys.bimr.bfcl.dto;

import twitter4j.GeoLocation;

/**
 * @author GLK
 */
public class TwitterDataDTO {
	
	private String user;
	
	private GeoLocation location;
	
	public TwitterDataDTO(String user, GeoLocation location) {
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
