package com.gsys.bimr.pf;

import javax.faces.bean.ManagedBean;

import com.gsys.bimr.bf.dto.TwitterRequestDTO;
import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.util.GeneralConstants;

@ManagedBean
public class MapBean {

	private MapModel mapModel;
	private MapFacade mapFacade;
	
	public void retrieveTweets() {
		TwitterRequestDTO request = new TwitterRequestDTO();
		request.setHashtag(GeneralConstants.TWITTER_BIRDMIGRATION);
		mapModel.setTweets(mapFacade.retrieveTweets(request).getTweets());
	}
	
}
