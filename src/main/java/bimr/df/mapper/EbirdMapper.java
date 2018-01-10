package bimr.df.mapper;

import java.util.ArrayList;
import java.util.List;

import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;
import bimr.rf.ebird.wrapper.EbirdDataWrapper;
import bimr.rf.ebird.wrapper.EbirdRequestWrapper;
import bimr.rf.ebird.wrapper.EbirdResponseWrapper;

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
