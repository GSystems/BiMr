package main.java.pf;

import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import main.java.bfcl.TweetFacade;
import main.java.util.StandfordEnum;

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
		Properties props = new Properties();
		props.put(StandfordEnum.PROPS_KEY.getCode(), StandfordEnum.PROPS_VALUE.getCode());
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		tweetFacade.twitterApiCallScheduled(pipeline);
	}

	public TweetFacade getTweetFacade() {
		return tweetFacade;
	}

	public void setTweetFacade(TweetFacade tweetFacade) {
		this.tweetFacade = tweetFacade;
	}

}
