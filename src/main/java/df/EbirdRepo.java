package main.java.df;

import java.util.concurrent.Future;

import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;

public interface EbirdRepo {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	Future<EBirdResponse> retrieveEBirdData(EBirdRequest requests);

}
