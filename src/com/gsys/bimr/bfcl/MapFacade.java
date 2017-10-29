package com.gsys.bimr.bfcl;

import com.gsys.bimr.bf.dto.TwitterRequestDTO;
import com.gsys.bimr.bf.dto.TwitterResponseDTO;

public interface MapFacade {

	TwitterResponseDTO retrieveTweets(TwitterRequestDTO request);

}
