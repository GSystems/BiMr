package main.java.df;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import main.java.df.mapper.EbirdMapper;
import main.java.df.model.EbirdData;
import main.java.df.model.EbirdRequest;
import main.java.df.model.EbirdResponse;
import main.java.rf.ebird.EbirdsServiceClient;
import main.java.rf.ebird.EbirdsServiceClientBean;

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
