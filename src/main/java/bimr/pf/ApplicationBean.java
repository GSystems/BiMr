package bimr.pf;

import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.TweetScheduleFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 * @author GLK
 */
@ManagedBean(eager = true)
@ApplicationScoped
@Singleton
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
	}

}
