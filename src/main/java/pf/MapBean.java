package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import main.java.bfcl.MapFacade;
import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.util.GeneralConstants;

@ManagedBean
@ViewScoped
public class MapBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient MapModel mapModel;

	@EJB
	private transient MapFacade mapFacade;

	@PostConstruct
	public void init() {
		mapModel = new MapModel();
		retrieveTweetsFromDB();
//		retrieveTweetsFromApi();
//		retrieveEbirdApiData();
	}

	public void retrieveTweetsFromDB() {
		mapModel.setTweets(mapFacade.retrieveTweetsFromDB());
	}

	public void retrieveEbirdApiData() {
		EBirdRequestDTO request = new EBirdRequestDTO();
		request.setRequestUriPattern(GeneralConstants.EBIRDS_API_REQUEST_URI);
		mapModel.setEbirdData(mapFacade.retrieveEBirdData(request).getEbirdData());
	}

	public void retrieveTweetsFromApi() {
		mapFacade.retrieveTweetsFromApi(generateRequest());
	}

	public TwitterRequestDTO generateRequest() {
		return new TwitterRequestDTO(GeneralConstants.TWITTER_BIRDMIGRATION);
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
