package bimr.bf;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TweetRequestDTO;
import bimr.bfcl.dto.TweetResponseDTO;
import bimr.df.TweetRepo;
import bimr.util.AsyncUtils;
import bimr.util.GeneralConstants;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;

/**
 * @author GLK
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TweetFacadeBean implements TweetFacade {

	@Inject
	private TweetRepo tweetRepo;

	@Override
	public List<TweetDTO> retrieveTweetsFromDB() {
		return MapTransformer.fromTweetsToDTO(AsyncUtils.getResultFromAsyncTask(tweetRepo.retrieveTweetsFromDB()));
	}

	@Override
	public TweetResponseDTO retrieveTweetsFromApi(TweetRequestDTO request) {
		return MapTransformer.fromTwitterResponseToDTO(tweetRepo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request)));
	}

	@Override
	public Long retrieveLastTweetId() {
		List<Long> sinceIds = AsyncUtils.getResultFromAsyncTask(tweetRepo.retrieveLastTweetId());
		Long sinceId = GeneralConstants.DEFAULT_SINCE_ID;
		if (sinceIds.get(0) != null) {
			sinceId = sinceIds.get(0);
		}
		return sinceId;
	}

	@Override
	public void persistTweets(List<TweetDTO> tweets) {
		tweetRepo.insertTweets(MapTransformer.toTweetsFromDTO(tweets));
	}

	@Override
	public Date retrieveMinDateOfTweets() {
		List<Date> dateList = AsyncUtils.getResultFromAsyncTask(tweetRepo.retrieveMinDate());
		Date date = new Date();
		if (dateList.get(0) != null) {
			date = dateList.get(0);
		}
		return date;
	}

	private TweetResponseDTO generateTweets() {
		TweetResponseDTO response = new TweetResponseDTO();
		List<TweetDTO> tweets = new ArrayList<>();
		TweetDTO tweet1 = new TweetDTO();
		TweetDTO tweet2 = new TweetDTO();
		TweetDTO tweet3 = new TweetDTO();

		tweet1.setLatitude("1234");
		tweet1.setLongitude("1234");
		tweet1.setObservationDate(new Date());
		tweet1.setTweetId(1L);
		tweet1.setTweetMessage("Some pigeons near Washington");

		tweet2.setLatitude("2345");
		tweet2.setLongitude("2345");
		tweet2.setObservationDate(new Date());
		tweet2.setTweetId(2L);
		tweet2.setTweetMessage("I saw a flock of ducks at Salt Lake");

		tweet3.setLatitude("3456");
		tweet3.setLongitude("3456");
		tweet3.setObservationDate(new Date());
		tweet3.setTweetId(3L);
		tweet3.setTweetMessage("Beautiful flocks of ravens in Yosemite Park");

		tweets.add(tweet1);
		tweets.add(tweet2);
		tweets.add(tweet3);

		response.setTweets(tweets);
		return response;
	}
}
