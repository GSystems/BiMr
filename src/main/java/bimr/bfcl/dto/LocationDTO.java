package bimr.bfcl.dto;

import org.apache.commons.lang3.tuple.Pair;

public class LocationDTO {

	private Pair<Double, Double> geo;
	private String city;
	private String state;
	private String country;

	public Pair<Double, Double> getGeo() {
		return geo;
	}

	public void setGeo(Pair<Double, Double> geo) {
		this.geo = geo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
