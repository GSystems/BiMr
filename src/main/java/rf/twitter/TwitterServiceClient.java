package main.java.rf.twitter;

import main.java.rf.twitter.wrapper.TwitterRequestWrapper;
import main.java.rf.twitter.wrapper.TwitterResponseWrapper;

/**
 * @author GLK
 */
public interface TwitterServiceClient {

	TwitterResponseWrapper retrieveTweets(TwitterRequestWrapper request);

}
