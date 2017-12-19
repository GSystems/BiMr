package main.java.bf;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import main.java.bfcl.GlobalFacade;
import main.java.bfcl.TweetFacade;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.util.TwitterEnum;

@Singleton
public class GlobalFacadeBean implements GlobalFacade {

	@EJB
	TweetFacade tweetFacade;

	@Schedule(second = "*", minute = "*/15", hour = "*", persistent = false)
	public void twitterApiCallScheduled() {
		TwitterRequestDTO request = createRequest();
		tweetFacade.retrieveTweetsFromApi(request);
	}

	private TwitterRequestDTO createRequest() {
		return new TwitterRequestDTO(TwitterEnum.BIRDMIGRATION.getCode(), tweetFacade.retrieveLastTweetId());
	}

}
