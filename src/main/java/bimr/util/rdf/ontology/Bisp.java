package bimr.util.rdf.ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Bisp {
	public static final String URI = "http://xmlns.com/bisp/";
	private static final Model m = ModelFactory.createDefaultModel();
	public static final Resource HOTSPOT;
	public static final Property observation;
	public static final Property location;
	public static final Property user;
	public static final Property tweet;
	public static final Property longitude;
	public static final Property latitude;
	public static final Property name;
	public static final Property country;
	public static final Property state;
	public static final Property author;
	public static final Property text;
	public static final Property date;
	public static final Property language;
	public static final Property userId;
	public static final Property tweetId;
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
		observation = m.createProperty(URI, "observation");
		location = m.createProperty(URI + "observation#location");
		user = m.createProperty(URI, "user");
		tweet = m.createProperty(URI, "observation#" + "tweet");
		longitude = m.createProperty(URI, "location#" + "longitude");
		latitude = m.createProperty(URI, "location#" + "latitude");
		name = m.createProperty(URI, "location#" + "name");
		country = m.createProperty(URI, "location#" + "country");
		state = m.createProperty(URI, "location#" + "state");
		author = m.createProperty(URI, "tweet#" + "author");
		text = m.createProperty(URI, "tweet#" + "text");
		language = m.createProperty(URI, "tweet#" + "language");
		userId = m.createProperty(URI, "tweet#userId");
		link = m.createProperty(URI, "tweet#" + "link");
		tweetId = m.createProperty(URI, "tweetId");
		birdSpecies = m.createProperty(URI, "observation#" + "birdSpecies");
		howMany = m.createProperty(URI, "observation#" + "howMany");
		date = m.createProperty(URI, "date");
		informationSourceId = m.createProperty(URI, "observation#" + "informationSourceId");
	}
}