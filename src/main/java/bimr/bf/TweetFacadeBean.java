package bimr.bf;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TweetRequestDTO;
import bimr.bfcl.dto.TweetResponseDTO;
import bimr.df.TweetRepo;
import bimr.util.AsyncUtils;
import bimr.util.GeneralConstants;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

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
	public void persistTweetsInRelationalDb(List<TweetDTO> tweets) {
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
}
