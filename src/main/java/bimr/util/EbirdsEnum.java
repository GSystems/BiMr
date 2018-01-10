package bimr.util;

public enum EbirdsEnum {

	HOTSPOT_SIGHTINGS_SUMMARY_API_REQUEST_URI(
			"http://ebird.org/ws1.1/product/obs/hotspot/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true"),
	NEAREST_LOCATIONS_WITH_OBSERVATIONS_OF_SPECIES("http://ebird.org/ws1.1/data/nearest/geo_spp/recent?lng=-76.51&lat=42.46&sci=branta%20canadensis&hotspot=true&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_NEARBY_NOTABLE_OBSERVATIONS("http://ebird.org/ws1.1/data/notable/geo/recent?lng=-76.51&lat=42.46&dist=20&back=30&detail=simple&locale=en_US&fmt=json"),
	RECENT_NEARBY_OBSERVATIONS("http://ebird.org/ws1.1/data/obs/geo/recent?lng=-76.51&lat=42.46&dist=2&back=5&maxResults=500&locale=en_US&fmt=json"),
	RECENT_NEARBY_OBSERVATIONS_OF_SPECIES("http://ebird.org/ws1.1/data/obs/geo_spp/recent?lng=-76.51&lat=42.46&sci=branta%20canadensis&dist=15&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_NOTABLE_OBSERVATIONS_AT_HOTSPOTS("http://ebird.org/ws1.1/data/notable/hotspot/recent?r=L99381&back=30&detail=simple&locale=en_US&fmt=json"),
	RECENT_NOTABLE_OBSERVATIONS_AT_LOCATIONS("http://ebird.org/ws1.1/data/notable/loc/recent?r=L99381&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json"),
	RECENT_NOTABLE_OBSERVATIONS_IN_REGION("http://ebird.org/ws1.1/data/notable/region/recent?rtype=subnational1&r=US-NV&back=30&locale=en_US&fmt=json"),
	RECENT_OBSERVATIONS_AT_HOTSPOTS("http://ebird.org/ws1.1/data/obs/hotspot/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_OBSERVATIONS_AT_LOCATIONS("http://ebird.org/ws1.1/data/obs/loc/recent?r=L99381&r=L104031&back=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_OBSERVATIONS_IN_REGION("http://ebird.org/ws1.1/data/obs/region/recent?rtype=subnational1&r=US-NV&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_OBSERVATIONS_OF_SPECIES_AT_HOTSPOTS("http://ebird.org/ws1.1/data/obs/hotspot_spp/recent?r=L99381&r=L104031&sci=larus%20delawarensis&back=30&detail=simple&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_OBSERVATIONS_OF_SPECIES_AT_LOCATIONS("http://ebird.org/ws1.1/data/obs/loc_spp/recent?r=L99381&r=L104031&sci=larus%20delawarensisback=5&maxResults=500&detail=simple&locale=en_US&fmt=json&includeProvisional=true"),
	RECENT_OBSERVATIONS_OF_SPECIES_IN_REGION("http://ebird.org/ws1.1/data/obs/region_spp/recent?rtype=subnational1&r=US-NV&sci=larus%20delawarensis&back=5&maxResults=500&locale=en_US&fmt=json&includeProvisional=true");

	private String code;

	EbirdsEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
