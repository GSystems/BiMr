package com.gsys.bimr.df.mapper;

import java.util.ArrayList;
import java.util.List;

import com.gsys.bimr.df.model.TwitterData;
import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;
import com.gsys.bimr.rf.model.TwitterDataWrapper;
import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;

public class MapMapper {

	private MapMapper() {
	}

	public static TwitterRequestWrapper fromTwitterRequestToWrapper(TwitterRequest request) {
		TwitterRequestWrapper requestWrapper = new TwitterRequestWrapper();
		requestWrapper.setHashtag(request.getHashtag());
		return requestWrapper;
	}

	public static TwitterResponse fromTwitterResponseWrapperToResponse(TwitterResponseWrapper responseWrapper) {
		TwitterResponse response = new TwitterResponse();
		response.setTweets(fromTwitterTwitterDataWrapperToTwitterData(responseWrapper.getTweets()));
		return response;
	}

	private static List<TwitterData> fromTwitterTwitterDataWrapperToTwitterData(List<TwitterDataWrapper> tweets) {
		List<TwitterData> twitterData = new ArrayList<>();
		for (TwitterDataWrapper tdw : tweets) {
			TwitterData td = new TwitterData();
			td.setLocation(tdw.getLocation());
			td.setMessage(tdw.getMessage());
			td.setUser(tdw.getMessage());
			twitterData.add(td);
		}
		return twitterData;
	}

}
