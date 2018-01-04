package bimr.df;

import java.util.concurrent.Future;

import bimr.df.model.EBirdRequest;
import bimr.df.model.EBirdResponse;

public interface EbirdRepo {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	Future<EBirdResponse> retrieveEBirdData(EBirdRequest requests);

}
