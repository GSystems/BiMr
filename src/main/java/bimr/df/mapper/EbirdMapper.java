package bimr.df.mapper;

import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;
import bimr.rf.ebird.entity.EbirdDataEntity;
import bimr.rf.ebird.wrapper.EbirdDataWrapper;
import bimr.rf.ebird.wrapper.EbirdRequestWrapper;
import bimr.rf.ebird.wrapper.EbirdResponseWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EbirdMapper {

	private EbirdMapper() {
	}

	public static EbirdResponse toEbirdsResponseFromWrapper(EbirdResponseWrapper responseWrapper) {
		EbirdResponse response = new EbirdResponse();
		List<EbirdData> data = new ArrayList<>();
		if (responseWrapper.getEbirdData() != null) {
			for (EbirdDataWrapper ebirdDataWrapper : responseWrapper.getEbirdData()) {
				data.add(toEbirdDataFromWrapper(ebirdDataWrapper));
			}
			response.setEbirdData(data);
		}
		return response;
	}

	private static EbirdData toEbirdDataFromWrapper(EbirdDataWrapper ebirdDataWrapper) {
		EbirdData ebirdData = new EbirdData();
		ebirdData.setLatitude(ebirdDataWrapper.getLatitude());
		ebirdData.setLocalityName(ebirdDataWrapper.getLocalityName());
		ebirdData.setLongitude(ebirdDataWrapper.getLongitude());
		ebirdData.setScientificName(ebirdDataWrapper.getScientificName());
		ebirdData.setObservationDate(convertDate(ebirdDataWrapper.getObservationDate()));
		return ebirdData;
	}

	private static String convertDate(Date observationDate) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(observationDate.getTime());
		String stringDate = simpleFormat.format(calendar.getTime());
		StringBuilder stringBuilder = new StringBuilder(stringDate);
		Date date = null;
		if (stringBuilder.charAt(0) == '0') {
			stringBuilder.insert(0, '2');
		}
		return stringBuilder.toString();
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
		ebirdDataEntity.setLatitude(ebirdData.getLatitude());
		ebirdDataEntity.setLongitude(ebirdData.getLongitude());
		ebirdDataEntity.setObservationDate(ebirdData.getObservationDate());
		ebirdDataEntity.setScientificName(ebirdData.getScientificName());
		ebirdDataEntity.setUserDisplayName(ebirdData.getUserDisplayName());
		return ebirdDataEntity;
	}
}
