package bimr.util;

public enum RdfEnum {

	URI("http://xmlns.com/");

	private String code;

	RdfEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
