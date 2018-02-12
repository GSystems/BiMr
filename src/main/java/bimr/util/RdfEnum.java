package bimr.util;

public enum RdfEnum {

	RDF_XML_FORMAT("RDF/XML-ABBREV"),
	TURTLE_FORMAT("N-TRIPLES"),
	HOTSPOT("hotspot"),
	LOCATION("location"),
	TWEET("tweet"),
	USER("user"),
	OBSERVATION("observation");

	private String code;

	RdfEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}