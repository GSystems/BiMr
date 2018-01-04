package bimr.df;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.inject.Inject;

import bimr.df.mapper.EbirdMapper;
import bimr.df.model.EBirdRequest;
import bimr.df.model.EBirdResponse;
import bimr.rf.ebird.EbirdsServiceClient;
import bimr.rf.ebird.EbirdsServiceClientBean;

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
