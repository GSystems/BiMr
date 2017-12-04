package main.java.bfcl;

import java.util.List;

import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.EBirdResponseDTO;
import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;

/**
 * @author GLK
 */
public interface MapFacade {

	/**
	 * Retrieves tweets from twitter api and persist them into database
	 * 
	 * @param request
	 * @return
	 */
	void retrieveTweetsFromApi(TwitterRequestDTO request);

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	EBirdResponseDTO retrieveEBirdData(EBirdRequestDTO request);

	/**
	 * Retrieve tweets from database
	 * 
	 * @return
	 */
	List<TweetDTO> retrieveTweetsFromDB();

	/**
	 * Retrieve tweets from API automatically by calling retrieveTweetsFromApi
	 * method at every 15 minutes
	 * 
	 */
	void twitterApiCallScheduled();

}
