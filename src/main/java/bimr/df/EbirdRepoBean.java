package bimr.df;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import bimr.df.mapper.EbirdMapper;
import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;
import bimr.rf.ebird.EbirdsServiceClient;
import bimr.rf.ebird.EbirdsServiceClientBean;

public class EbirdRepoBean implements EbirdRepo {

	@Inject
	private EbirdsServiceClient ebirdsService;

	@Override
	public Future<EbirdResponse> retrieveEBirdData(EbirdRequest request) {
		ebirdsService = new EbirdsServiceClientBean();
		return new AsyncResult<>(EbirdMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEBirdData(EbirdMapper.fromEBirdRequestToWrapper(request))));
	}

	@Override
	public void insertEbirdData(List<EbirdData> ebirds) {
		
	}

	@Override
	public Future<List<EbirdData>> retrieveEbirdDataFromDb() {
		// TODO Auto-generated method stub
		return null;
	}

}
