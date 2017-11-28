package main.java.pf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import main.java.bfcl.MapFacade;

@ManagedBean
@ApplicationScoped
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private transient MapFacade mapFacade;

	@PostConstruct
	public void init() {
//		mapFacade.twitterApiCallScheduler();
	}

	public MapFacade getMapFacade() {
		return mapFacade;
	}

	public void setMapFacade(MapFacade mapFacade) {
		this.mapFacade = mapFacade;
	}

}
