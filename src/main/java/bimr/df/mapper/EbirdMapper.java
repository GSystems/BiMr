package bimr.df.mapper;

import java.util.ArrayList;
import java.util.List;

import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;
import bimr.rf.ebird.entity.EbirdDataEntity;
import bimr.rf.ebird.wrapper.EbirdDataWrapper;
import bimr.rf.ebird.wrapper.EbirdRequestWrapper;
import bimr.rf.ebird.wrapper.EbirdResponseWrapper;

public class EbirdMapper {

	private EbirdMapper() {
	}

	public static EbirdResponse toEbirdsResponseFromWrapper(EbirdResponseWrapper responseWrapper) {
		EbirdResponse response = new EbirdResponse();
		List<EbirdData> data = new ArrayList<>();
		for (EbirdDataWrapper ebirdDataWrapper : responseWrapper.getEbirdData()) {
			data.add(toEbirdDataFromWrapper(ebirdDataWrapper));
		}
		response.setEbirdData(data);
		return response;
	}

	private static EbirdData toEbirdDataFromWrapper(EbirdDataWrapper ebirdDataWrapper) {
		EbirdData ebirdData = new EbirdData();
		ebirdData.setCommonName(ebirdDataWrapper.getCommonName());
		ebirdData.setCountryName(ebirdDataWrapper.getCountryName());
		ebirdData.setLatitude(ebirdDataWrapper.getLatitude());
		ebirdData.setLocalityName(ebirdDataWrapper.getLocalityName());
		ebirdData.setLongitude(ebirdDataWrapper.getLongitude());
		ebirdData.setObservationDate(ebirdDataWrapper.getObservationDate());
		ebirdData.setScientificName(ebirdDataWrapper.getScientificName());
		ebirdData.setStateName(ebirdDataWrapper.getStateName());
		ebirdData.setUserDisplayName(ebirdDataWrapper.getUserDisplayName());
		return ebirdData;
	}

	public static EbirdRequestWrapper fromEbirdRequestToWrapper(EbirdRequest request) {
		EbirdRequestWrapper requestWrapper = new EbirdRequestWrapper();
		requestWrapper.setRequestUriPattern(request.getRequestUriPattern());
		return requestWrapper;
	}

	public static List<EbirdDataEntity> fromEbirdDataListToEntity(List<EbirdData> ebirdDataList) {
		List<EbirdDataEntity> ebirdDataEntityList = new ArrayList<>();
		for (EbirdData ebirdData : ebirdDataList) {
			ebirdDataEntityList.add(fromEbirdDataToEntity(ebirdData));
		}
		return ebirdDataEntityList;
	}

	private static EbirdDataEntity fromEbirdDataToEntity(EbirdData ebirdData) {
		EbirdDataEntity ebirdDataEntity = new EbirdDataEntity();
		ebirdDataEntity.setCommonName(ebirdData.getCommonName());
		ebirdDataEntity.setCountryName(ebirdData.getCountryName());
		ebirdDataEntity.setLatitude(ebirdData.getLatitude());
		ebirdDataEntity.setLocalityName(ebirdData.getLocalityName());
		ebirdDataEntity.setLongitude(ebirdData.getLongitude());
		ebirdDataEntity.setObservationDate(ebirdData.getObservationDate());
		ebirdDataEntity.setScientificName(ebirdData.getScientificName());
		ebirdDataEntity.setStateName(ebirdData.getStateName());
		ebirdDataEntity.setUserDisplayName(ebirdData.getUserDisplayName());
		return ebirdDataEntity;
	}
}
