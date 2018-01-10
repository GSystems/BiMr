package bimr.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import bimr.bfcl.ScheduleFacade;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
@Singleton
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient ScheduleFacade globalFacade;

	@PostConstruct
	public void init() {
		globalFacade.twitterApiCallScheduled();
	}

	public ScheduleFacade getGlobalFacade() {
		return globalFacade;
	}

	public void setGlobalFacade(ScheduleFacade globalFacade) {
		this.globalFacade = globalFacade;
	}

}
