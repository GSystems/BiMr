package main.java.df;

import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;

public interface EbirdRepo {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	EBirdResponse retrieveEBirdData(EBirdRequest requests);

}
