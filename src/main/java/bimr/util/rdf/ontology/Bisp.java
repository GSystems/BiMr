package bimr.util.rdf.ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Bisp {
	public static final String URI = "http://xmlns.com/bisp/";
	private static final Model m = ModelFactory.createDefaultModel();
	public static final Resource HOTSPOT;
	public static final Property location;
	public static final Property user;
	public static final Property tweet;
	public static final Property longitude;
	public static final Property latitude;
	public static final Property name;
	public static final Property country;
	public static final Property city;
	public static final Property author;
	public static final Property text;
	public static final Property observationDate;
	public static final Property language;
	public static final Property id;
	public static final Property birdSpecies;
	public static final Property howMany;
	public static final Property link;
	public static final Property informationSourceId;

	private Bisp() {
	}

	public static String getURI() {
		return URI;
	}

	static {
		HOTSPOT = m.createResource(URI);
		location = m.createProperty(URI + "location");
		user = m.createProperty(URI,"user");
		tweet = m.createProperty(URI, "tweet");
		longitude = m.createProperty(URI, "location#" + "longitude");
		latitude = m.createProperty(URI, "location#" + "latitude");
		name = m.createProperty(URI, "location#" + "name");
		country = m.createProperty(URI, "location#" + "country");
		city = m.createProperty(URI, "location#" + "city");
		author = m.createProperty(URI, "tweet#" + "author");
		text = m.createProperty(URI, "tweet#" + "text");
		language = m.createProperty(URI, "tweet#" + "language");
		id = m.createProperty(URI, "tweet#" + "id");
		link = m.createProperty(URI, "tweet#" + "link");
		birdSpecies = m.createProperty(URI, "birdSpecies");
		howMany = m.createProperty(URI, "howMany");
		observationDate = m.createProperty(URI, "observationDate");
		informationSourceId = m.createProperty(URI, "informationSourceId");
	}
}
