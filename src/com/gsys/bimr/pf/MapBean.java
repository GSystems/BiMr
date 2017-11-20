package com.gsys.bimr.pf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.bfcl.dto.EBirdDataDTO;
import com.gsys.bimr.bfcl.dto.EBirdRequestDTO;
import com.gsys.bimr.bfcl.dto.EBirdResponseDTO;
import com.gsys.bimr.bfcl.dto.TwitterRequestDTO;
import com.gsys.bimr.util.GeneralConstants;

@ManagedBean
@ViewScoped
public class MapBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private MapModel mapModel;

	@EJB
	private transient MapFacade mapFacade;

	@PostConstruct
	public void init() {
		mapModel = new MapModel();
		retrieveTweets();
		retrieveEbirdApiData();
	}

	public void retrieveEbirdApiData() {
		EBirdRequestDTO request = new EBirdRequestDTO();
		request.setRequestUriPattern(GeneralConstants.EBIRDS_API_REQUEST_URI);
		EBirdResponseDTO response = mapFacade.retrieveEBirdData(request);
		List<EBirdDataDTO> list = response.getEbirdData();
		mapModel.setEbirdData(list);
	}

	public void retrieveTweets() {
		TwitterRequestDTO request = generateRequest();
		mapModel.setTweets(mapFacade.retrieveTweets(request).getTweets());
	}

	private TwitterRequestDTO generateRequest() {
		TwitterRequestDTO request = new TwitterRequestDTO();
		List<String> hashtags = new ArrayList<>();
		hashtags.add(GeneralConstants.TWITTER_BIRDMIGRATION);
		hashtags.add(GeneralConstants.TWITTER_BIRDMIG);
		request.setHashtags(hashtags);
		return request;
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
