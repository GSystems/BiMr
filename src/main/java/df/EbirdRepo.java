package main.java.df;

import java.util.List;
import java.util.concurrent.Future;

import main.java.df.model.EBirdData;
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

	/**
	 * Insert a list of Ebird API data into the database
	 * 
	 * @param ebirds
	 */
	void insertEbirdData(List<EBirdData> ebirds);
	
	/**
	 * Retrieve Ebird Data from the database
	 * 
	 * @return
	 */
	Future<List<EBirdData>> retrieveEbirdDataFromDb();
}
