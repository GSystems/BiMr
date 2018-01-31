package bimr.df;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import bimr.df.mapper.EbirdMapper;
import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;
import bimr.rf.ebird.EbirdServiceClient;
import bimr.rf.ebird.dao.EbirdDAO;

public class EbirdRepoBean implements EbirdRepo {

	@Inject
	private EbirdServiceClient ebirdsService;

	@Inject
	private EbirdDAO ebirdDAO;

	@Override
	public EbirdResponse retrieveEbirdData(EbirdRequest request) {
		return EbirdMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEbirdData(EbirdMapper.fromEbirdRequestToWrapper(request)));
	}

	@Override
	public void insertEbirdData(List<EbirdData> ebirdDataList) {
		ebirdDAO.insertDataList(EbirdMapper.fromEbirdDataListToEntity(ebirdDataList));
	}

	@Override
	public Future<List<EbirdData>> retrieveEbirdDataFromDb() {
		return new AsyncResult<>(EbirdMapper.toEbirdDataListFromEntity(ebirdDAO.findAllEbirdData()));
	}

}
