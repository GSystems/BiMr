package bimr.df.mapper;

import java.util.ArrayList;
import java.util.List;

import bimr.df.model.EBirdData;
import bimr.df.model.EBirdRequest;
import bimr.df.model.EBirdResponse;
import bimr.rf.ebird.wrapper.EBirdDataWrapper;
import bimr.rf.ebird.wrapper.EBirdRequestWrapper;
import bimr.rf.ebird.wrapper.EBirdResponseWrapper;

public class EbirdMapper {

	public static EBirdResponse toEbirdsResponseFromWrapper(EBirdResponseWrapper responseWrapper) {
		EBirdResponse response = new EBirdResponse();
		List<EBirdData> data = new ArrayList<>();
		if (responseWrapper.getEbirdData() != null) {
			data = toEbirdDataFromWrapper(responseWrapper.getEbirdData());
		}
		response.setEbirdData(data);
		return response;
	}

	private static List<EBirdData> toEbirdDataFromWrapper(List<EBirdDataWrapper> ebirdsData) {
		List<EBirdData> ebirds = new ArrayList<>();
		for (EBirdDataWrapper ebirdWrapper : ebirdsData) {
			EBirdData ebird = new EBirdData();
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

	public static EBirdRequestWrapper fromEBirdRequestToWrapper(EBirdRequest request) {
		EBirdRequestWrapper requestWrapper = new EBirdRequestWrapper();
		requestWrapper.setRequestUriPattern(request.getRequestUriPattern());
		return requestWrapper;
	}

}
