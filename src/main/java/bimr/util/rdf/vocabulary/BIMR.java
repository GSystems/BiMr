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
	private static final String MIGRATION_URI= "http://xmlns.com/bimr/migration#%s";

	private static final Model m = ModelFactory.createDefaultModel();

	public static final Property hotspot;
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
	public static final Property tweetId;
	public static final Property birdSpecies;
	public static final Property howMany;
	public static final Property link;
	public static final Property informationSourceId;
	public static final Property hasGeoEnabled;
	public static final Property id;

	public static final Property migration;
	public static final Property from;
	public static final Property to;

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
	public static final String getMigrationUri(final String id) {
		return String.format(MIGRATION_URI, id);
	}

	static {
		hotspot = m.createProperty(getBaseUri("hotspot"));
		observation = m.createProperty(getHotspotUri("observation"));
		location = m.createProperty(getObservationUri("location"));
		user = m.createProperty(getBaseUri("user"));
		tweet = m.createProperty(getObservationUri("tweet"));
		birdSpecies = m.createProperty(getObservationUri("birdSpecies"));
		longitude = m.createProperty(getLocationUri("longitude"));
		latitude = m.createProperty(getLocationUri("latitude"));
		city = m.createProperty(getLocationUri("city"));
		country = m.createProperty(getLocationUri("country"));
		state = m.createProperty(getLocationUri("state"));
		author = m.createProperty(getTweetUri("author"));
		text = m.createProperty(getTweetUri("text"));
		language = m.createProperty(getTweetUri("language"));
		link = m.createProperty(getTweetUri("link"));
		tweetId = m.createProperty(getTweetUri("tweetId"));
		howMany = m.createProperty(getObservationUri("howMany"));
		date = m.createProperty(getObservationUri("date"));
		informationSourceId = m.createProperty(getObservationUri("informationSourceId"));
//		hotspotId = m.createProperty(getHotspotUri("hotspotId"));
//		userId = m.createProperty(getUserUri("userId"));
		id = m.createProperty(getBaseUri("id"));
		hasGeoEnabled = m.createProperty(getUserUri("hasGeoEnabled"));

		migration = m.createProperty(getMigrationUri("migration"));
		from = m.createProperty(getMigrationUri("from"));
		to = m.createProperty(getMigrationUri("to"));
	}
}