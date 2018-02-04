package bimr.util.rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class BIMR {
	private static final String BASE_URI = "http://xmlns.com/bimr#%s";
	private static final String HOTSPOT_URI = "http://xmlns.com/bimr/hotspot#%s";
	private static final String USER_URI = "http://xmlns.com/bimr/user#%s";
	private static final String OBSERVATION_URI = "http://xmlns.com/bimr/observation#%s";
	private static final String LOCATION_URI = "http://xmlns.com/bimr/location#%s";
	private static final String TWEET_URI = "http://xmlns.com/bimr/tweet#%s";


	private static final Model m = ModelFactory.createDefaultModel();

	public static final Resource HOTSPOT;
	public static final Property observation;
	public static final Property location;
	public static final Property user;
	public static final Property tweet;
	public static final Property longitude;
	public static final Property latitude;
	public static final Property city;
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
	public static final Property hotspotId;
	public static final Property hasGeoEnabled;

	private BIMR() {
	}

	public static final String getBaseUri(final String id) {
		return String.format(BASE_URI, id);
	}

	public static final String getHotspotUri(final String id) {
		return String.format(HOTSPOT_URI, id);
	}

	public static final String getUserUri(final String id) {
		return String.format(USER_URI, id);
	}

	public static final String getObservationUri(final String id) {
		return String.format(OBSERVATION_URI, id);
	}

	public static final String getLocationUri(final String id) {
		return String.format(LOCATION_URI, id);
	}

	public static final String getTweetUri(final String id) {
		return String.format(TWEET_URI, id);
	}

	static {
		HOTSPOT = m.createResource(getBaseUri("hotspot"));
		observation = m.createProperty(getBaseUri("observation"));
		location = m.createProperty(getBaseUri("location"));
		user = m.createProperty(getBaseUri("user"));
		tweet = m.createProperty(getBaseUri("tweet"));
		longitude = m.createProperty(getBaseUri("longitude"));
		latitude = m.createProperty(getBaseUri("latitude"));
		city = m.createProperty(getBaseUri("city"));
		country = m.createProperty(getBaseUri("country"));
		state = m.createProperty(getBaseUri("state"));
		author = m.createProperty(getBaseUri("author"));
		text = m.createProperty(getBaseUri("text"));
		language = m.createProperty(getBaseUri("language"));
		userId = m.createProperty(getBaseUri("userId"));
		link = m.createProperty(getBaseUri("link"));
		tweetId = m.createProperty(getBaseUri("tweetId"));
		birdSpecies = m.createProperty(getBaseUri("birdSpecies"));
		howMany = m.createProperty(getBaseUri("howMany"));
		date = m.createProperty(getBaseUri("date"));
		informationSourceId = m.createProperty(getBaseUri("informationSourceId"));
		hotspotId = m.createProperty(getBaseUri("hotspotId"));
		hasGeoEnabled = m.createProperty(getBaseUri("hasGeoEnabled"));
	}
}