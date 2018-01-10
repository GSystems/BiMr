package bimr.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import bimr.bfcl.TwitterScheduleFacade;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
@Singleton
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient TwitterScheduleFacade globalFacade;

	@PostConstruct
	public void init() {
		globalFacade.twitterApiCallScheduled();
	}

	public TwitterScheduleFacade getGlobalFacade() {
		return globalFacade;
	}

	public void setGlobalFacade(TwitterScheduleFacade globalFacade) {
		this.globalFacade = globalFacade;
	}

}
