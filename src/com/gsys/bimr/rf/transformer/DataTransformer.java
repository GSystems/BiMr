package com.gsys.bimr.rf.transformer;

import java.util.ArrayList;
import java.util.List;

import com.gsys.bimr.rf.model.TwitterDataWrapper;

import twitter4j.Status;

/**
 * @author GLK
 */
public class DataTransformer {

	private DataTransformer() {
	}

	public static List<TwitterDataWrapper> fromResponseToWrapper(List<Status> tweets) {
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

}
