package bimr.util.rdf.vocabulary;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Bisp {
	public static final String URI = "http://xmlns.com/bisp/#";
	private static final Model m = ModelFactory.createDefaultModel();
	public static final Resource LOCATION;
	public static final Resource TWEET;
	public static final Resource PERSON;
	public static final Resource HOTSPOT;
	public static final Property geo;
	public static final Property longitude;
	public static final Property latitude;
	public static final Property country;
	public static final Property city;
	public static final Property name;
	public static final Property author;
	public static final Property text;
	public static final Property observationDate;
	public static final Property language;
	public static final Property id;
	public static final Property email;
	public static final Property accountName;
	public static final Property bisp;
	public static final Property howMany;
	public static final Property link;

	private Bisp() {
	}

	public static String getURI() {
		return URI;
	}

	static {
		LOCATION = m.createResource("http://xmlns.com/bisp/LOCATION#");
		TWEET = m.createResource("http://xmlns.com/bisp/TWEET#");
		PERSON = m.createResource("http://xmlns.com/bisp/PERSON#");
		HOTSPOT = m.createResource("http://xmlns.com/bisp/HOTSPOT#");
		geo = m.createProperty(URI, "geo");
		longitude = m.createProperty(URI, "longitude");
		latitude = m.createProperty(URI, "latitude");
		country = m.createProperty(URI, "country");
		city = m.createProperty(URI, "city");
		name = m.createProperty(URI, "name");
		author = m.createProperty(URI, "author");
		text = m.createProperty(URI, "text");
		observationDate = m.createProperty(URI, "observationDate");
		language = m.createProperty(URI, "language");
		id = m.createProperty(URI, "id");
		email = m.createProperty(URI, "email");
		accountName = m.createProperty(URI, "accountName");
		bisp = m.createProperty(URI, "bisp");
		howMany = m.createProperty(URI, "howMany");
		link = m.createProperty(URI, "link");
	}
}
