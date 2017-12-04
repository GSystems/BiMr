package main.java.util;

public enum EbirdsEnum {

	API_REQUEST_URI(
			"http://ebird.org/ws1.1/data/notable/region/recent?rtype=subnational1&r=US-NV&back=5&maxResults=500&locale=en_US&fmt=json");

	private String code;

	EbirdsEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
