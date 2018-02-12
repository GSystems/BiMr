package bimr.bf.transformer;

import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.MigrationDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GeoJsonTransformer {

	public static JSONObject fromHotspotsToJsonCollection(List<HotspotDTO> hotspots) {
		JSONObject hotspotsCollecion = new JSONObject();
		try {
			hotspotsCollecion.put("type", "FeatureCollection");
			JSONArray featureList = new JSONArray();

			// iterate through your list
			for (HotspotDTO hotspot : hotspots) {
				JSONObject feature = new JSONObject();

				if (hotspot.getLocation().getGeo() != null || hotspot.getLocation().getCity() != null) {
					JSONObject point = extractGeoFromHotspot(hotspot);
					feature.put("geometry", point);
				}
				JSONObject properties = extractPropertiesFromHotspot(hotspot);
				feature.put("type", "Feature");

				feature.put("properties", properties);

				featureList.put(feature);
				hotspotsCollecion.put("features", featureList);
			}
		} catch (JSONException e) {
			System.out.println("can't save json object: "+ e.toString());
		}
		return hotspotsCollecion;
	}

	private static JSONObject extractGeoFromHotspot(HotspotDTO hotspot) {
		JSONObject point = new JSONObject();
		point.put("type", "Point");
		JSONArray coord = new JSONArray("["+hotspot.getLocation().getGeo().getRight()+","+hotspot.getLocation().getGeo().getLeft()+"]");
		point.put("coordinates", coord);
		return point;
	}

	private static JSONObject extractPropertiesFromHotspot(HotspotDTO hotspot) {
		JSONObject properties = new JSONObject();

		properties.put("informationSourceId", hotspot.getInformationSource());
		properties.put("tweetMessage", hotspot.getTweetMessage());
		properties.put("tweetId", hotspot.getTweetId());
		properties.put("observationDate", hotspot.getObservationDate());
		properties.put("hotspotId", hotspot.getHotspotId());
		properties.put("howMany", hotspot.getHowMany());
		properties.put("city", hotspot.getLocation().getCity());
		properties.put("link", hotspot.getLink());
		properties.put("author", hotspot.getAuthor());
		properties.put("country", hotspot.getLocation().getCountry());
		properties.put("state", hotspot.getLocation().getState());
		properties.put("language", hotspot.getLanguage());

		if (hotspot.getBirdSpecies() != null) {
			extractBirdSpeciesFromHotspot(properties, hotspot.getBirdSpecies());
		}

		if (hotspot.getUser() != null) {
			extractPropertiesFromUser(properties, hotspot.getUser());
		}
		return properties;
	}

	private static void extractBirdSpeciesFromHotspot(JSONObject properties, List<String> birdSpecies) {
		JSONObject birdSpeciesJson = new JSONObject();
		for (String birdSpec : birdSpecies) {
			birdSpeciesJson.put("birdSpecies", birdSpec);
		}
		properties.put("birdSpeciesList", birdSpecies);
	}

	private static void extractPropertiesFromUser(JSONObject properties, TwitterUserDTO user) {
		JSONObject userJson = new JSONObject();
		userJson.put("id", user.getId());
		userJson.put("email", user.getEmail());
		userJson.put("location", user.getLocation());
		userJson.put("name", user.getName());
		userJson.put("screenName", user.getScreenName());
		userJson.put("isGeoEnabled", user.isGeoEnabled());

		properties.put("user", userJson);
	}

	public static JSONObject fromMigrationToJsonCollection(List<MigrationDTO> migrations) {
		JSONObject migrationsCollecion = new JSONObject();
		try {
			migrationsCollecion.put("type", "FeatureCollection");
			JSONArray featureList = new JSONArray();

			for (MigrationDTO migration : migrations) {
				JSONObject feature = new JSONObject();

				JSONObject fromPoint = extractGeoFromHotspot(migration.getFromHotspot());
				JSONObject toPoint = extractGeoFromHotspot(migration.getToHotspot());

				feature.put("geometry", fromPoint);

				JSONObject fromHotspotProps = extractPropertiesFromHotspot(migration.getFromHotspot());
				fromHotspotProps.put("geometry", fromPoint);

				JSONObject toHotspotProps = extractPropertiesFromHotspot(migration.getToHotspot());
				toHotspotProps.put("geometry", toPoint);

				JSONObject properties = new JSONObject();

				properties.put("species", migration.getSpecies());
				properties.put("fromHotpsotId", migration.getFromHotspot().getHotspotId());
				properties.put("toHotspotId", migration.getToHotspot().getHotspotId());
				properties.put("migrationId", migration.getMigrationId());
				properties.put("fromHotspot", fromHotspotProps);
				properties.put("toHotspot", toHotspotProps);

				feature.put("properties", properties);
				feature.put("type", "Feature");

				featureList.put(feature);
				migrationsCollecion.put("features", featureList);
			}
		} catch (JSONException e) {
			System.out.println("can't save json object: "+ e.toString());
		}
		return migrationsCollecion;
	}
}
