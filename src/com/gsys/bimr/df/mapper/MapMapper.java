package com.gsys.bimr.df.mapper;

import java.util.ArrayList;
import java.util.List;

import com.gsys.bimr.df.model.EBirdData;
import com.gsys.bimr.df.model.EBirdRequest;
import com.gsys.bimr.df.model.EBirdResponse;
import com.gsys.bimr.df.model.TwitterData;
import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;
import com.gsys.bimr.rf.model.EBirdDataWrapper;
import com.gsys.bimr.rf.model.EBirdRequestWrapper;
import com.gsys.bimr.rf.model.EBirdResponseWrapper;
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
	
	public static EBirdRequestWrapper fromEBirdRequestToWrapper(EBirdRequest request) {
		EBirdRequestWrapper requestWrapper = new EBirdRequestWrapper();
		request.setRequestUriPattern(request.getRequestUriPattern());
		return requestWrapper;
	}

	public static TwitterResponse fromTwitterResponseWrapperToResponse(TwitterResponseWrapper responseWrapper) {
		TwitterResponse response = new TwitterResponse();
		response.setTweets(fromTwitterTwitterDataWrapperToTwitterData(responseWrapper.getTweets()));
		return response;
	}

	public static EBirdResponse fromEBirdResponseWrapperToResponse(EBirdResponseWrapper responseWrapper) {
		EBirdResponse response = new EBirdResponse();
		response.seteBirdData(fromEBirdDataWrapperToEBirdData(responseWrapper.geteBirdData()));
		return response;
	}
	
	private static List<TwitterData> fromTwitterTwitterDataWrapperToTwitterData(List<TwitterDataWrapper> tweets) {
		List<TwitterData> twitterData = new ArrayList<>();
		for (TwitterDataWrapper tdw : tweets) {
			TwitterData td = new TwitterData();
			td.setLocation(tdw.getLocation());
			td.setMessage(tdw.getMessage());
			td.setUser(tdw.getUser());
			twitterData.add(td);
		}
		return twitterData;
	}
	
	private static List<EBirdData> fromEBirdDataWrapperToEBirdData(List<EBirdDataWrapper> ebirdData) {
		List<EBirdData> ebird = new ArrayList<>();
		for (EBirdDataWrapper ebdw: ebirdData) {
			EBirdData ebd = new EBirdData();
			ebd.setCommonName(ebdw.getCommonName());
			ebd.setCountryName(ebdw.getCountryName());
			ebd.setLatitude(ebdw.getLatitude());
			ebd.setLocalityName(ebdw.getLocalityName());
			ebd.setLongitude(ebdw.getLongitude());
			ebd.setObservationDate(ebdw.getObservationDate());
			ebd.setScientificName(ebdw.getScientificName());
			ebd.setStateName(ebdw.getStateName());
			ebd.setUserDisplayName(ebdw.getUserDisplayName());
		}
		return ebird;
	}

}
