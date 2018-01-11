package bimr.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.TweetScheduleFacade;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
@Singleton
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient TweetScheduleFacade tweetScheduleFacade;

	@EJB
	private transient EbirdScheduleFacade ebirdScheduleFacade;

	@PostConstruct
	public void init() {
		tweetScheduleFacade.twitterApiCallScheduled();
		ebirdScheduleFacade.ebirdApiCallScheduled();
	}

	public TweetScheduleFacade getTweetScheduleFacade() {
		return tweetScheduleFacade;
	}

	public void setTweetScheduleFacade(TweetScheduleFacade tweetScheduleFacade) {
		this.tweetScheduleFacade = tweetScheduleFacade;
	}

	public EbirdScheduleFacade getEbirdScheduleFacade() {
		return ebirdScheduleFacade;
	}

	public void setEbirdScheduleFacade(EbirdScheduleFacade ebirdScheduleFacade) {
		this.ebirdScheduleFacade = ebirdScheduleFacade;
	}
}
