package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import main.java.bfcl.EbirdFacade;
import main.java.bfcl.TweetFacade;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.util.TwitterEnum;

/**
 * @author GLK
 */
@ManagedBean
@RequestScoped
public class MapBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient MapModel mapModel;

	@EJB
	private transient EbirdFacade mapFacade;

	@EJB
	private transient TweetFacade tweetFacade;

	@PostConstruct
	public void init() {
		mapModel = new MapModel();
		mapModel.setTweets(tweetFacade.retrieveTweetsFromDB());
	}

	public TwitterRequestDTO generateRequest() {
		return new TwitterRequestDTO(TwitterEnum.BIRDMIGRATION.getCode());
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

	public EbirdFacade getMapFacade() {
		return mapFacade;
	}

	public void setMapFacade(EbirdFacade mapFacade) {
		this.mapFacade = mapFacade;
	}

	public TweetFacade getTweetFacade() {
		return tweetFacade;
	}

	public void setTweetFacade(TweetFacade tweetFacade) {
		this.tweetFacade = tweetFacade;
	}

}
