package com.gsys.bimr.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gsys.bimr.bf.dto.TwitterRequestDTO;
import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.util.GeneralConstants;

@ManagedBean
@ViewScoped
public class MapBean implements Serializable  {

	private static final long serialVersionUID = 1L;

	private MapModel mapModel;
	
	@EJB
	private MapFacade mapFacade;

	@PostConstruct
	public void init() {
		mapModel = new MapModel();
		retrieveTweets();
	}

	public void retrieveTweets() {
		TwitterRequestDTO request = new TwitterRequestDTO();
		request.setHashtag(GeneralConstants.TWITTER_BIRDMIGRATION);
		mapModel.setTweets(mapFacade.retrieveTweets(request).getTweets());
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public MapFacade getMapFacade() {
		return mapFacade;
	}

	public void setMapFacade(MapFacade mapFacade) {
		this.mapFacade = mapFacade;
	}

}
