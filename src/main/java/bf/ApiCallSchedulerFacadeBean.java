package main.java.bf;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import main.java.bfcl.ApiCallSchedulerFacade;
import main.java.bfcl.MapFacade;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.util.GeneralConstants;

@Singleton
public class ApiCallSchedulerFacadeBean implements ApiCallSchedulerFacade {

	@EJB
	private MapFacade mapFacade;

	@Override
	@Schedule(second = "*", minute = "*/15", hour = "*", persistent = false)
	public void twitterApiCallScheduler() {
		TwitterRequestDTO request = new TwitterRequestDTO(GeneralConstants.TWITTER_BIRDMIGRATION);
		mapFacade.retrieveTweetsFromApi(request);
	}

}
