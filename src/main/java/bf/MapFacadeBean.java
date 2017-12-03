package main.java.bf;

import java.util.ArrayList;
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
	
	private List<TweetDTO> filterTweets(List<TweetDTO> tweets) {
		List<TweetDTO> filteredTweets = new ArrayList<>();
		for (TweetDTO tweet : tweets) {
			
		}
		return filteredTweets; 
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

}
