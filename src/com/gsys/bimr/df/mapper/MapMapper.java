package com.gsys.bimr.df.mapper;

import java.util.HashMap;
import java.util.Map;

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
		requestWrapper.setHashtags(request.getHashtags());
		return requestWrapper;
	}

	public static TwitterResponse fromTwitterResponseWrapperToResponse(TwitterResponseWrapper responseWrapper) {
		TwitterResponse response = new TwitterResponse();
		response.setTweets(fromTwitterTwitterDataWrapperToTwitterData(responseWrapper.getTweets()));
		return response;
	}

	private static Map<String, TwitterData> fromTwitterTwitterDataWrapperToTwitterData(
			Map<String, TwitterDataWrapper> tweets) {
		Map<String, TwitterData> twitterData = new HashMap<>();
		for (Map.Entry<String, TwitterDataWrapper> tdw : tweets.entrySet()) {
			TwitterDataWrapper td = tdw.getValue();
			twitterData.put(tdw.getKey(), new TwitterData(td.getUser(), td.getLocation()));
		}
		return twitterData;
	}

}
