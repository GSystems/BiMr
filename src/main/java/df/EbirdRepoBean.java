package main.java.df;

import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import main.java.df.mapper.EbirdMapper;
import main.java.df.model.EBirdData;
import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;
import main.java.rf.ebird.EbirdsServiceClient;
import main.java.rf.ebird.EbirdsServiceClientBean;

public class EbirdRepoBean implements EbirdRepo {

	@Inject
	private EbirdsServiceClient ebirdsService;

	@Override
	public Future<EBirdResponse> retrieveEBirdData(EBirdRequest request) {
		ebirdsService = new EbirdsServiceClientBean();
		return new AsyncResult<>(EbirdMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEBirdData(EbirdMapper.fromEBirdRequestToWrapper(request))));
	}

	@Override
	public void insertEbirdData(List<EBirdData> ebirds) {
		
	}

	@Override
	public Future<List<EBirdData>> retrieveEbirdDataFromDb() {
		// TODO Auto-generated method stub
		return null;
	}

}
