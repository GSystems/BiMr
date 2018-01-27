package bimr.util;

public enum RdfEnum {

	FILENAME("hotspots.rdf"),
	DIRECTORY_NAME("database/dataset1"),
	RDF_XML_FORMAT("RDF/XML-ABBREV"),
	TURTLE_FORMAT("N-TRIPLES"),
	BASE_URI("http://xmlns.com/bisp/"),
	LOCATION_URI("http://xmlns.com/bisp/location#"),
	TWEET_URI("http://xmlns.com/bisp/tweet#"),
	USER_URI("http://xmlns.com/bisp/user#"),
	OBSERVATION_URI("http://xmlns.com/bisp/observation#");

	private String code;

	RdfEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
