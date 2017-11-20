package com.gsys.bimr.df;

import com.gsys.bimr.df.mapper.MapMapper;
import com.gsys.bimr.df.model.EBirdRequest;
import com.gsys.bimr.df.model.EBirdResponse;
import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;
import com.gsys.bimr.rf.eBird.EbirdsServiceClient;
import com.gsys.bimr.rf.eBird.EbirdsServiceClientBean;
import com.gsys.bimr.rf.twitter.TwitterServiceClientBean;

public class MapRepositoryBean implements MapRepository {

	@Override
	public TwitterResponse retrieveTweets(TwitterRequest request) {
		TwitterServiceClientBean service = new TwitterServiceClientBean();
		return MapMapper.fromTwitterResponseWrapperToResponse(
				service.retrieveTweets(MapMapper.fromTwitterRequestToWrapper(request)));
	}

	@Override
	public EBirdResponse retrieveEBirdData(EBirdRequest request) {
		EbirdsServiceClient service = new EbirdsServiceClientBean();
		return MapMapper.fromEBirdResponseWrapperToResponse(
				service.retrieveEBirdData(MapMapper.fromEBirdRequestToWrapper(request)));
	}

}
