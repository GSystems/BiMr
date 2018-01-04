package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import main.java.bfcl.EbirdFacade;
import main.java.bfcl.TweetFacade;

/**
 * @author GLK
 */
@ManagedBean
@ViewScoped
public class MapBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient MapModel mapModel;

	@EJB
	private transient EbirdFacade ebirdFacade;

	@EJB
	private transient TweetFacade tweetFacade;

	@PostConstruct
	public void init() {
		mapModel = new MapModel();
		mapModel.setTweets(tweetFacade.retrieveTweetsFromDB());
	}

	// TODO solve this errors
	// public void retrieveEbirdApiData() {
	// for (String EBIRDS_API_REQUEST_URI:
	// GeneralConstants.EBIRDS_API_REQUEST_URIS){
	// EBirdRequestDTO request = new EBirdRequestDTO();
	// request.setRequestUriPattern(EBIRDS_API_REQUEST_URI);
	// mapModel.setEbirdData(mapFacade.retrieveEBirdData(request).getEbirdData());
	// }
	// }

	public EbirdFacade getEbirdFacade() {
		return ebirdFacade;
	}

	public void setEbirdFacade(EbirdFacade ebirdFacade) {
		this.ebirdFacade = ebirdFacade;
	}

	public TweetFacade getTweetFacade() {
		return tweetFacade;
	}

	public void setTweetFacade(TweetFacade tweetFacade) {
		this.tweetFacade = tweetFacade;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

}
