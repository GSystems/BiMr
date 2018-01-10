package main.java.df;

import java.util.List;
import java.util.concurrent.Future;

import main.java.df.model.EbirdData;
import main.java.df.model.EbirdRequest;
import main.java.df.model.EbirdResponse;

public interface EbirdRepo {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	Future<EbirdResponse> retrieveEBirdData(EbirdRequest requests);

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
