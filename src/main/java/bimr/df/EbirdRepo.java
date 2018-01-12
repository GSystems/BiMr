package bimr.df;

import java.util.List;
import java.util.concurrent.Future;

import bimr.df.model.EbirdData;
import bimr.df.model.EbirdRequest;
import bimr.df.model.EbirdResponse;

public interface EbirdRepo {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param requests
	 * @return
	 */
	Future<EbirdResponse> retrieveEbirdData(EbirdRequest requests);

	/**
	 * Insert a list of Ebird API data into the database
	 * 
	 * @param ebirds
	 */
	void insertEbirdData(List<EbirdData> ebirds);
	
	/**
	 * Retrieve Ebird Data from the database
	 * 
	 * @return
	 */
	Future<List<EbirdData>> retrieveEbirdDataFromDb();
}
