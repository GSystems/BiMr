package com.gsys.bimr.rf.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gsys.bimr.rf.model.EBirdDataWrapper;
import com.gsys.bimr.rf.model.TwitterDataWrapper;

import twitter4j.Status;

/**
 * @author GLK
 */
public class DataTransformer {

	private static JSONParser jsonParser;
	private static JSONArray jsonArray;
	private static JSONObject jsonObject;

	private DataTransformer() {
	}

	public static Map<String, TwitterDataWrapper> fromTwitterRawResponseToWrapper(List<Status> tweets) {
		Map<String, TwitterDataWrapper> tweetsWrapper = new HashMap<>();
		for (Status status : tweets) {
			TwitterDataWrapper wrapper = new TwitterDataWrapper();
			wrapper.setLocation(status.getGeoLocation());
			wrapper.setUser(status.getUser().getScreenName());
			tweetsWrapper.put(status.getText(), wrapper);
		}
		return tweetsWrapper;
	}

	public static List<EBirdDataWrapper> fromEBirdRawResponseToWrapper(String ebirdData) {
		// preparing JSON object utility
		ebirdJSONUtilityInit();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			jsonArray = (JSONArray) jsonParser.parse(ebirdData);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

		Iterator i = jsonArray.iterator();
		List<EBirdDataWrapper> ebirdWrapper = new ArrayList<>();
		while (i.hasNext()) {
			jsonObject = (JSONObject) i.next();
			EBirdDataWrapper wrapper = new EBirdDataWrapper();
			wrapper.setCommonName((String) jsonObject.get("comName"));
			wrapper.setCountryName((String) jsonObject.get("countryName"));
			wrapper.setLatitude((Double) jsonObject.get("lat"));
			wrapper.setLocalityName((String) jsonObject.get("locName"));
			wrapper.setLongitude((Double) jsonObject.get("lng"));
			try {
				wrapper.setObservationDate((Date) formatter.parse((String) jsonObject.get("obsDt")));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			wrapper.setScientificName((String) jsonObject.get("sciName"));
			wrapper.setStateName((String) jsonObject.get("subnational1Name"));
			wrapper.setUserDisplayName((String) jsonObject.get("userDisplayName"));
			ebirdWrapper.add(wrapper);
		}

		return ebirdWrapper;
	}

	private static void ebirdJSONUtilityInit() {
		jsonParser = new JSONParser();
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
	}

}
