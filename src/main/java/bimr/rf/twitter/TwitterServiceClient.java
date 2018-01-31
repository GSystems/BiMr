package bimr.rf.twitter;

import bimr.rf.twitter.wrapper.TwitterRequestWrapper;
import bimr.rf.twitter.wrapper.TwitterResponseWrapper;

/**
 * @author GLK
 */
public interface TwitterServiceClient {

	TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request);

}
