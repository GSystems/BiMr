package com.gsys.bimr.rf.transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gsys.bimr.rf.model.TwitterDataWrapper;

import twitter4j.Status;

/**
 * @author GLK
 */
public class DataTransformer {

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

}
