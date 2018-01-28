package bimr.util.rdf.ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class Bisp {
	private static final String URI = "http://xmlns.com/hotspot#%s";
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

	public static final String getUri(final String s) {
		return String.format(URI, s);
	}

	static {
		HOTSPOT = m.createResource(getUri("hotspot"));
		observation = m.createProperty(getUri("observation"));
		location = m.createProperty(getUri("location"));
		user = m.createProperty(getUri("user"));
		tweet = m.createProperty(getUri("tweet"));
		longitude = m.createProperty(getUri("longitude"));
		latitude = m.createProperty(getUri("latitude"));
		name = m.createProperty(getUri("name"));
		country = m.createProperty(getUri("country"));
		state = m.createProperty(getUri("state"));
		author = m.createProperty(getUri("author"));
		text = m.createProperty(getUri("text"));
		language = m.createProperty(getUri("language"));
		userId = m.createProperty(getUri("userId"));
		link = m.createProperty(getUri("link"));
		tweetId = m.createProperty(getUri("tweetId"));
		birdSpecies = m.createProperty(getUri("birdSpecies"));
		howMany = m.createProperty(getUri("howMany"));
		date = m.createProperty(getUri("date"));
		informationSourceId = m.createProperty(getUri("informationSourceId"));
	}
}