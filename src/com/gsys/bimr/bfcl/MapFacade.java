package com.gsys.bimr.bfcl;

import com.gsys.bimr.bfcl.dto.TwitterRequestDTO;
import com.gsys.bimr.bfcl.dto.TwitterResponseDTO;

public interface MapFacade {

	/**
	 * Retrieves tweets
	 * @param request
	 * @return
	 */
	TwitterResponseDTO retrieveTweets(TwitterRequestDTO request);

}
