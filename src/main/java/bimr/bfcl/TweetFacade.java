package bimr.bfcl;

import java.util.List;

import bimr.bfcl.dto.TweetDTO;

/**
 * @author GLK
 */
public interface TweetFacade {

	/**
	 * Retrieve tweets from database
	 * 
	 * @return
	 */
	List<TweetDTO> retrieveTweetsFromDB();

}
