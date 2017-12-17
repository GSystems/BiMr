package main.java.bf;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.inject.Inject;

import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.TweetFacade;
import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.bfcl.dto.TwitterResponseDTO;
import main.java.df.TweetRepo;
import main.java.util.TwitterEnum;

public class TweetFacadeBean implements TweetFacade {

	@Inject
	private TweetRepo repo;

	@Override
	public void retrieveTweetsFromApi(TwitterRequestDTO request) {
		TwitterResponseDTO response = MapTransformer
				.fromTwitterResponseToDTO(repo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request)));
		if (!response.getTweets().isEmpty()) {
			persistTweets(response.getTweets());
		}
	}

	@Override
	public void persistTweets(List<TweetDTO> tweets) {
		repo.insertTweets(MapTransformer.toTweetsFromDTO(tweets));
	}

	@Override
	public List<TweetDTO> retrieveTweetsFromDB() {
		return MapTransformer.fromTweetsToDTO(repo.retrieveTweetsFromDB());
	}

	@Override
	@Schedule(second = "*", minute = "15", hour = "*", persistent = false)
	public void twitterApiCallScheduled() {
		TwitterRequestDTO request = new TwitterRequestDTO(TwitterEnum.BIRDMIGRATION.getCode());
		 retrieveTweetsFromApi(request);
	}
	
	private List<TweetDTO> filterTweets(List<TweetDTO> tweets) {
		List<TweetDTO> filteredTweets = new ArrayList<>();
		for (TweetDTO tweet : tweets) {

		}
		return filteredTweets;
	}
}
