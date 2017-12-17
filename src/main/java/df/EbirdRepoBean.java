package main.java.df;

import javax.inject.Inject;

import main.java.df.mapper.EbirdMapper;
import main.java.df.mapper.TweetMapper;
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
		return EbirdMapper.toEbirdsResponseFromWrapper(
				ebirdsService.retrieveEBirdData(TweetMapper.fromEBirdRequestToWrapper(request)));
	}

}
