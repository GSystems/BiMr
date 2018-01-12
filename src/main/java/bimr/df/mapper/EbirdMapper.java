package bimr.df.mapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

	private static Date extractDate(Date observationDate) {
		String dateStr = observationDate.toString();
		// Sun Jul 10 00:00:00 EET 12
		DateFormat readFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yy");

		DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = writeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private static Date convertDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatedDate =
				cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1);
		Date newFormat = null;
		try {
			newFormat = writeFormat.parse(formatedDate);
			if (newFormat.toString().startsWith("00")) {

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newFormat;
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
