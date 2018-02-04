package bimr.bf.transformer;

import bimr.bfcl.dto.HotspotDTO;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RdfModelTransformer {

	public static JSONObject fromHotspotsToJsonCollection(List<HotspotDTO> hotspots) {
		JSONObject hotspotsCollecion = new JSONObject();
		try {
			hotspotsCollecion.put("type", "hotspotsCollecion");
			JSONArray featureList = new JSONArray();

			// iterate through your list
			for (HotspotDTO hotspot : hotspots) {

				// {"geometry": {"type": "Point", "coordinates": [-94.149, 36.33]}
				JSONObject point = new JSONObject();
				point.put("type", "Point");

				// construct a JSONArray from a string; can also use an array or list
				JSONArray coord = new JSONArray("["+hotspot.getLongitude()+","+hotspot.getLatitude()+"]");

				point.put("coordinates", coord);

				JSONObject feature = new JSONObject();
				feature.put("geometry", point);
				featureList.put(feature);
				hotspotsCollecion.put("features", featureList);
				System.out.println(hotspotsCollecion.toString());
				break;
			}
		} catch (JSONException e) {
			System.out.println("can't save json object: "+ e.toString());
		}
		return hotspotsCollecion;
	}
}
