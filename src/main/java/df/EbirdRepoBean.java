package main.java.df;

import javax.inject.Inject;

import main.java.df.mapper.MapMapper;
import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;
import main.java.rf.ebird.EbirdsServiceClient;
import main.java.rf.ebird.EbirdsServiceClientBean;

public class EbirdRepoBean implements EbirdRepo {

	@Inject
	private EbirdsServiceClient ebirdsService;

	@Override
	public EBirdResponse retrieveEBirdData(EBirdRequest request) {
		ebirdsService = new EbirdsServiceClientBean();
		return MapMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEBirdData(MapMapper.fromEBirdRequestToWrapper(request)));
	}

}
