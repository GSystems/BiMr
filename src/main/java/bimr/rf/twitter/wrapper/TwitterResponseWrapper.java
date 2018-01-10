package bimr.rf.twitter.wrapper;

import java.util.List;

/**
 * @author GLK
 */
public class TwitterResponseWrapper {

	private List<TweetWrapper> tweets;

	public List<TweetWrapper> getTweets() {
		return tweets;
	}

	public void setTweets(List<TweetWrapper> tweets) {
		this.tweets = tweets;
	}


}
