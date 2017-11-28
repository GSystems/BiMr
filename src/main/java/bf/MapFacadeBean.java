package main.java.bf;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.MapFacade;
import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.EBirdResponseDTO;
import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.bfcl.dto.TwitterResponseDTO;
import main.java.df.MapRepository;
import main.java.util.GeneralConstants;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MapFacadeBean implements MapFacade {

	@Inject
	private MapRepository repo;

	@Override
	public void retrieveTweetsFromApi(TwitterRequestDTO request) {
		TwitterResponseDTO response = MapTransformer
				.fromTwitterResponseToDTO(repo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request)));
		if (!response.getTweets().isEmpty()) {
			persistTweets(response.getTweets());
		}
	}

	private void persistTweets(List<TweetDTO> tweets) {
		repo.insertTweets(MapTransformer.toTweetsFromDTO(tweets));
	}

	@Override
	public List<TweetDTO> retrieveTweetsFromDB() {
		return MapTransformer.fromTweetsToDTO(repo.retrieveTweetsFromDB());
	}

	@Override
	public EBirdResponseDTO retrieveEBirdData(EBirdRequestDTO request) {
		return MapTransformer
				.fromEBirdResponseToDTO(repo.retrieveEBirdData(MapTransformer.toEbirdRequestFromDTO(request)));
	}

	@Override
	public void twitterApiCallScheduler() {
		Thread thread = new TwitterApiCall();
		thread.start();
	}

	class TwitterApiCall extends Thread {
		@Override
		public void run() {
			while (true) {
				TwitterRequestDTO request = new TwitterRequestDTO(GeneralConstants.TWITTER_BIRDMIGRATION);
				retrieveTweetsFromApi(request);
				try {
					Thread.sleep(1000 * 60 * 15l);
				} catch (InterruptedException e) {
					// Restore interrupted state
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
