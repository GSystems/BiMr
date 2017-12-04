package main.java.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author GLK
 */
public class GeneralConstants {

	private GeneralConstants() {
	}

	public static final String SCHEMA = "BIMR";

	public static final Integer MAX_NUMBER_OF_TWEETS = 100;

	public static final String CONSUMER_KEY = "jsJnEdd07JSdW46jfAhhYrKBG";
	public static final String CONSUMER_SECRET = "aERQTUoMvOYvgpv7Uof19ix9ESBJp0Lvi0T69h5lTkYHRnS6pt";
	public static final String ACCESS_TOKEN = "4831081090-3KETssKWWNdFnfagOtnQGDSvUitx7dHS1dkUE16";
	public static final String ACCESS_TOKEN_SECRET = "UQg4utsCPp2CiW5XC8cWiiZImcqZa56S9ovCqspTMt0xM";

	// TODO move these constants in EbirdEnum
	public static final String EBIRDS_HOTSPOT_SIGHTINGS_SUMMARY_API_REQUEST_URI = "http://ebird.org/ws1.1/product/obs/hotspot/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_NEAREST_LOCATIONS_WITH_OBSERVATIONS_OF_SPECIES = "http://ebird.org/ws1.1/data/nearest/geo_spp/recent?lng=-76.51&lat=42.46&sci=branta%20canadensis&hotspot=true&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_NEARBY_NOTABLE_OBSERVATIONS = "http://ebird.org/ws1.1/data/notable/geo/recent?lng=-76.51&lat=42.46&dist=2&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json";
	public static final String EBIRDS_RECENT_NEARBY_OBSERVATIONS = "http://ebird.org/ws1.1/data/obs/geo/recent?lng=-76.51&lat=42.46&dist=2&back=5&maxResults=500&locale=en_US&fmt=json";
	public static final String EBIRDS_RECENT_NEARBY_OBSERVATIONS_OF_SPECIES = "http://ebird.org/ws1.1/data/obs/geo_spp/recent?lng=-76.51&lat=42.46&sci=branta%20canadensis&dist=15&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_NOTABLE_OBSERVATIONS_AT_HOTSPOTS = "http://ebird.org/ws1.1/data/notable/hotspot/recent?r=L99381&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json";
	public static final String EBIRDS_RECENT_NOTABLE_OBSERVATIONS_AT_LOCATIONS = "http://ebird.org/ws1.1/data/notable/loc/recent?r=L99381&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json";
	public static final String EBIRDS_RECENT_NOTABLE_OBSERVATIONS_IN_REGION = "http://ebird.org/ws1.1/data/notable/region/recent?rtype=subnational1&r=US-NV&back=5&maxResults=500&locale=en_US&fmt=json";
	public static final String EBIRDS_RECENT_OBSERVATIONS_AT_HOTSPOTS = "http://ebird.org/ws1.1/data/obs/hotspot/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_OBSERVATIONS_AT_LOCATIONS = "http://ebird.org/ws1.1/data/obs/loc/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_OBSERVATIONS_IN_REGION = "http://ebird.org/ws1.1/data/obs/region/recent?rtype=subnational1&r=US-NV&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_AT_HOTSPOTS = "http://ebird.org/ws1.1/data/obs/hotspot_spp/recent?r=L99381&r=L104031&sci=larus%20delawarensis&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_AT_LOCATIONS = "http://ebird.org/ws1.1/data/obs/loc_spp/recent?r=L99381&r=L104031&sci=larus%20delawarensisback=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true";
	public static final String EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_IN_REGION = "http://ebird.org/ws1.1/data/obs/region_spp/recent?rtype=subnational1&r=US-NV&sci=larus%20delawarensis&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true";
	
	public static final String RETWEET = "RT";

	public static final String DATE_FORMAT = "dd-MM-yyyy";

	public static final List<String> EBIRDS_API_REQUEST_URIS = 
			Arrays.asList(
					EBIRDS_HOTSPOT_SIGHTINGS_SUMMARY_API_REQUEST_URI,
					EBIRDS_NEAREST_LOCATIONS_WITH_OBSERVATIONS_OF_SPECIES,
					EBIRDS_RECENT_NEARBY_NOTABLE_OBSERVATIONS,
					EBIRDS_RECENT_NEARBY_OBSERVATIONS,
					EBIRDS_RECENT_NEARBY_OBSERVATIONS_OF_SPECIES,
					EBIRDS_RECENT_NOTABLE_OBSERVATIONS_AT_HOTSPOTS,
					EBIRDS_RECENT_NOTABLE_OBSERVATIONS_AT_LOCATIONS,
					EBIRDS_RECENT_NOTABLE_OBSERVATIONS_IN_REGION,
					EBIRDS_RECENT_OBSERVATIONS_AT_HOTSPOTS,
					EBIRDS_RECENT_OBSERVATIONS_AT_LOCATIONS,
					EBIRDS_RECENT_OBSERVATIONS_IN_REGION,
					EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_AT_HOTSPOTS,
					EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_AT_LOCATIONS,
					EBIRDS_RECENT_OBSERVATIONS_OF_SPECIES_IN_REGION
					);

}
