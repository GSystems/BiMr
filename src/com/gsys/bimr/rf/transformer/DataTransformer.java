package com.gsys.bimr.rf.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private static JSONObject jsonObject;
	
	private DataTransformer() {
	}

	public static List<TwitterDataWrapper> fromTwitterRawResponseToWrapper(List<Status> tweets) {
		List<TwitterDataWrapper> tweetsWrapper = new ArrayList<>();
		for (Status status : tweets) {
			TwitterDataWrapper wrapper = new TwitterDataWrapper();
			wrapper.setLocation(status.getGeoLocation());
			wrapper.setMessage(status.getText());
			wrapper.setUser(status.getUser().getScreenName());
			tweetsWrapper.add(wrapper);
		}
		return tweetsWrapper;
	}
	
	public static List<EBirdDataWrapper> fromEBirdRawResponseToWrapper(String ebirdData) {
		// preparing JSON object utility
		ebirdJSONUtilityInit();
		
		try {
			jsonObject = (JSONObject) jsonParser.parse(ebirdData);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		List<EBirdDataWrapper> ebirdWrapper = new ArrayList<>();
		
		EBirdDataWrapper wrapper = new EBirdDataWrapper();
		wrapper.setCommonName((String) jsonObject.get("comName"));
		wrapper.setCountryName((String) jsonObject.get("countryName"));
		wrapper.setLatitude((Double) jsonObject.get("lat"));
		wrapper.setLocalityName((String) jsonObject.get("locName"));
		wrapper.setLongitude((Double) jsonObject.get("lng"));
		wrapper.setObservationDate((Date) jsonObject.get("obsDt"));
		wrapper.setScientificName((String) jsonObject.get("sciName"));
		wrapper.setStateName((String) jsonObject.get("subnational1Name"));
		wrapper.setUserDisplayName((String) jsonObject.get("userDisplayName"));
		ebirdWrapper.add(wrapper);
		
		return ebirdWrapper;
	}
	
	private static void ebirdJSONUtilityInit() {
		jsonParser = new JSONParser();
		jsonObject = new JSONObject();
	}

}
