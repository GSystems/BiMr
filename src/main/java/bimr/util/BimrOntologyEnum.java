package bimr.util;

public enum BimrOntologyEnum {

	RDF_XML_FORMAT("RDF/XML-ABBREV"),
	TURTLE_FORMAT("N-TRIPLES"),
	HOTSPOT("hotspot"),
	LOCATION("location"),
	TWEET("tweet"),
	USER("user"),
	OBSERVATION("observation"),

	GET_ALL_HOTSPOTS_QRY("SELECT * WHERE {?subject ?property ?object}");

	private String code;

	BimrOntologyEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
