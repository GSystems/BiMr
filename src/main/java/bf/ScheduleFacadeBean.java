package main.java.bf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.ScheduleFacade;
import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.bfcl.dto.TwitterResponseDTO;
import main.java.df.TweetRepo;
import main.java.util.AsyncUtils;
import main.java.util.GeneralConstants;
import main.java.util.StandfordEnum;
import main.java.util.TwitterEnum;

@Singleton
public class ScheduleFacadeBean implements ScheduleFacade {

	private static final Logger log = Logger.getLogger(ScheduleFacadeBean.class.getName());
	private static Integer count;
	private static StanfordCoreNLP pipeline;;

	@Inject
	private TweetRepo repo;

	@PostConstruct
	public void init() {
		count = 0;
		initializePipeline();
	}

	@Schedule(second = "*", minute = "*/15", hour = "*", persistent = false)
	public void twitterApiCallScheduled() {
		TwitterRequestDTO request = createRequest();
		retrieveTweetsFromApi(request);
	}

	@Override
	public Long retrieveLastTweetId() {
		List<Long> sinceIds = AsyncUtils.getResultFromAsyncTask(repo.retrieveLastTweetId());
		Long sinceId = GeneralConstants.DEFAULT_SINCE_ID;
		if (sinceIds.get(0) != null) {
			sinceId = sinceIds.get(0);
		}
		return sinceId;
	}

	@Override
	public void retrieveTweetsFromApi(TwitterRequestDTO request) {
		TwitterResponseDTO response = MapTransformer.fromTwitterResponseToDTO(
				AsyncUtils.getResultFromAsyncTask(repo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request))));
		if (!response.getTweets().isEmpty()) {
			persistTweets(filterTweets(response.getTweets()));
		} else {
			log.info("No data from Twitter API");
		}
	}

	private List<TweetDTO> filterTweets(List<TweetDTO> tweets) {
		List<TweetDTO> filteredTweets = new ArrayList<>();
		for (TweetDTO tweet : tweets) {
			Annotation document = new Annotation(tweet.getTweetMessage());
			// run all Annotators on this text
			pipeline.annotate(document);
			if (parseText(document)) {
				filteredTweets.add(tweet);
			}
		}
		return filteredTweets;
	}

	/**
	 * parse every sentence from the tweet and annotate it
	 */
	private boolean parseText(Annotation document) {
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// this is the NER label of the token
				String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

				if (ne.equals(StandfordEnum.LOCATION.getCode())) {
					// this is the text of the token
					String word = token.get(CoreAnnotations.TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
					String text = String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, ne);
					log.info(text);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void persistTweets(List<TweetDTO> tweets) {
		repo.insertTweets(MapTransformer.toTweetsFromDTO(tweets));
	}

	private TwitterRequestDTO createRequest() {
		String hashtag;
		switch (count) {
		case 1:
			hashtag = TwitterEnum.BIRDSMIGRATION.getCode();
			break;
		case 2:
			hashtag = TwitterEnum.BIRDMIG.getCode();
			break;
		case 3:
			hashtag = TwitterEnum.BIRDSMIG.getCode();
			break;
		case 4:
			hashtag = TwitterEnum.ORNITHOLOGY.getCode();
			break;
		case 5:
			hashtag = TwitterEnum.BIRD.getCode();
			break;
		case 6:
			hashtag = TwitterEnum.BIRDS.getCode();
			break;
		case 7:
			hashtag = TwitterEnum.BIRDING.getCode();
			break;
		default:
			hashtag = TwitterEnum.BIRDMIGRATION.getCode();
		}
		count++;
		if (count > GeneralConstants.MAX_NUMBER_HASHTAGS) {
			count = 0;
		}
		return new TwitterRequestDTO(hashtag, retrieveLastTweetId());
	}

	private static void initializePipeline() {
		Properties props = new Properties();
		props.put(StandfordEnum.PROPS_KEY.getCode(), StandfordEnum.PROPS_VALUE.getCode());
		props.put(StandfordEnum.NER_MODEL_KEY.getCode(), StandfordEnum.NER_MODEL_VALUE.getCode());
		pipeline = new StanfordCoreNLP(props);
	}

	@SuppressWarnings("unused")
	private TwitterResponseDTO generateTweets() {
		TwitterResponseDTO response = new TwitterResponseDTO();
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
