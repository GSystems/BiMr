package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import main.java.bfcl.GlobalFacade;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
@Singleton
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient GlobalFacade globalFacade;

	@PostConstruct
	public void init() {
		globalFacade.twitterApiCallScheduled();
	}

	public GlobalFacade getGlobalFacade() {
		return globalFacade;
	}

	public void setGlobalFacade(GlobalFacade globalFacade) {
		this.globalFacade = globalFacade;
	}

}
