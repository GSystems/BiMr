package com.gsys.bimr.df;

import com.gsys.bimr.df.mapper.MapMapper;
import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;
import com.gsys.bimr.rf.twitter.TwitterServiceClientBean;

public class MapRepositoryBean implements MapRepository {

	@Override
	public TwitterResponse retrieveTweets(TwitterRequest request) {
		TwitterServiceClientBean service = new TwitterServiceClientBean();
		return MapMapper.fromTwitterResponseWrapperToResponse(
				service.retrieveTweets(MapMapper.fromTwitterRequestToWrapper(request)));
	}

}
