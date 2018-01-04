package main.java.bfcl;

import java.util.List;

import main.java.bfcl.dto.TweetDTO;

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
