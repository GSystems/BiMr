package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import main.java.bfcl.TweetFacade;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient TweetFacade tweetFacade;

	@PostConstruct
	public void init() {
//		Properties props = new Properties();
//		props.put(StandfordEnum.PROPS_KEY.getCode(), StandfordEnum.PROPS_VALUE.getCode());
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		tweetFacade.twitterApiCallScheduled();
	}

	public TweetFacade getTweetFacade() {
		return tweetFacade;
	}

	public void setTweetFacade(TweetFacade tweetFacade) {
		this.tweetFacade = tweetFacade;
	}

}
