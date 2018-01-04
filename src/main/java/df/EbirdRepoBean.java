package main.java.df;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import main.java.df.mapper.EbirdMapper;
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

}
