package bimr.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import bimr.bfcl.EbirdFacade;
import bimr.bfcl.TweetFacade;

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
