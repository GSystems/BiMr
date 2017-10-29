package com.gsys.bimr.rf.twitter;

import com.gsys.bimr.rf.model.TwitterRequestWrapper;
import com.gsys.bimr.rf.model.TwitterResponseWrapper;

/**
 * @author GLK
 */
public interface TwitterServiceClient {
	
	TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request);

}
