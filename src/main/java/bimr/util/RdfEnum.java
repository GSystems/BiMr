package bimr.util;

public enum RdfEnum {

	BASE_URI("http://xmlns.com/"),
	FILENAME("hotspots.rdf"),
	RDF_XML_FORMAT("RDF/XML-ABBREV"),
	TURTLE_FORMAT("N-TRIPLES"),
	LOCATION_URI("location"),
	TWEET_URI("tweet"),
	USER_URI("user");

	private String code;

	RdfEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
