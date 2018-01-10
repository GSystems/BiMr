package main.java.df.mapper;

import java.util.ArrayList;
import java.util.List;

import main.java.df.model.EbirdData;
import main.java.df.model.EbirdRequest;
import main.java.df.model.EbirdResponse;
import main.java.rf.ebird.wrapper.EbirdDataWrapper;
import main.java.rf.ebird.wrapper.EbirdRequestWrapper;
import main.java.rf.ebird.wrapper.EbirdResponseWrapper;

public class EbirdMapper {

	public static EbirdResponse toEbirdsResponseFromWrapper(EbirdResponseWrapper responseWrapper) {
		EbirdResponse response = new EbirdResponse();
		List<EbirdData> data = new ArrayList<>();
		if (responseWrapper.getEbirdData() != null) {
			data = toEbirdDataFromWrapper(responseWrapper.getEbirdData());
		}
		response.setEbirdData(data);
		return response;
	}

	private static List<EbirdData> toEbirdDataFromWrapper(List<EbirdDataWrapper> ebirdsData) {
		List<EbirdData> ebirds = new ArrayList<>();
		for (EbirdDataWrapper ebirdWrapper : ebirdsData) {
			EbirdData ebird = new EbirdData();
			ebird.setCommonName(ebirdWrapper.getCommonName());
			ebird.setCountryName(ebirdWrapper.getCountryName());
			ebird.setLatitude(ebirdWrapper.getLatitude());
			ebird.setLocalityName(ebirdWrapper.getLocalityName());
			ebird.setLongitude(ebirdWrapper.getLongitude());
			ebird.setObservationDate(ebirdWrapper.getObservationDate());
			ebird.setScientificName(ebirdWrapper.getScientificName());
			ebird.setStateName(ebirdWrapper.getStateName());
			ebird.setUserDisplayName(ebirdWrapper.getUserDisplayName());
			ebirds.add(ebird);
		}
		return ebirds;
	}

	public static EbirdRequestWrapper fromEBirdRequestToWrapper(EbirdRequest request) {
		EbirdRequestWrapper requestWrapper = new EbirdRequestWrapper();
		requestWrapper.setRequestUriPattern(request.getRequestUriPattern());
		return requestWrapper;
	}

}
