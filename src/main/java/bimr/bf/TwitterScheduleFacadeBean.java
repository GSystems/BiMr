package bimr.bf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import bimr.bfcl.dto.*;
import org.jboss.logging.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.TwitterScheduleFacade;
import bimr.df.TweetRepo;
import bimr.util.AsyncUtils;
import bimr.util.GeneralConstants;
import bimr.util.StanfordEnum;
import bimr.util.TwitterEnum;

@Singleton
public class TwitterScheduleFacadeBean implements TwitterScheduleFacade {

	private static final Logger log = Logger.getLogger(TwitterScheduleFacadeBean.class.getName());
	private static Integer count;
	private static StanfordCoreNLP pipeline;

	@Inject
    private TweetRepo twitterRepo;

	@PostConstruct
	public void init() {
		count = 0;
//		initializePipeline();
	}

	@Schedule(second = "*", minute = "*/15", hour = "*", persistent = false)
	public void twitterApiCallScheduled() {
		TwitterRequestDTO request = createRequest();
		retrieveTweetsFromApi(request);
	}

	@Override
	public Long retrieveLastTweetId() {
		List<Long> sinceIds = AsyncUtils.getResultFromAsyncTask(twitterRepo.retrieveLastTweetId());
		Long sinceId = GeneralConstants.DEFAULT_SINCE_ID;
		if (sinceIds.get(0) != null) {
			sinceId = sinceIds.get(0);
		}
		return sinceId;
	}

	@Override
	public void retrieveTweetsFromApi(TwitterRequestDTO request) {
		TwitterResponseDTO response = MapTransformer.fromTwitterResponseToDTO(AsyncUtils
				.getResultFromAsyncTask(twitterRepo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request))));
		if (!response.getTweets().isEmpty()) {
			persistTweets(response.getTweets());
			filterTweets(response.getTweets());
		} else {
			log.info("No data from Twitter API");
		}
	}

	private void filterTweets(List<TweetDTO> tweets) {
		List<TweetDTO> filteredTweets = new ArrayList<>();
		for (TweetDTO tweet : tweets) {
			Annotation document = new Annotation(tweet.getTweetMessage());
			// run all Annotators on this text
//			pipeline.annotate(document);
			HotspotDTO hotspot = parseTweet(document, tweet.getTweetId());
			if (hotspot.getBirdSpecies() != null && hotspot.getLatitude() != null
					|| hotspot.getLocationName() != null) {
				filteredTweets.add(tweet);
			}
		}
		if (!filteredTweets.isEmpty()) {
			createRdfModel(filteredTweets);
		}
	}

	/**
	 * parse every sentence from the tweet and annotate it
	 */
	private HotspotDTO parseTweet(Annotation document, Long tweetId) {
		HotspotDTO hotspot = new HotspotDTO();
		hotspot.setInformationSourceId(tweetId.toString());
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// this is the NER label of the token
				String namedEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

				hotspot = extractSensitiveInfoFromTweet(namedEntity, hotspot);

				//TODO remove this block after in the final version
				if (hotspot.getBirdSpecies() != null || hotspot.getLocationName() != null) {
					// this is the text of the token
					String word = token.get(CoreAnnotations.TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
					String text = String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, namedEntity);
					log.info(text);
				}
			}
		}
		return hotspot;
	}

	private HotspotDTO extractSensitiveInfoFromTweet(String namedEntity, HotspotDTO hotspot) {
		if (namedEntity.equals(StanfordEnum.LOCATION.getCode())) {
			hotspot.setLocationName(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.BISP.getCode())) {
			hotspot.setBirdSpecies(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.NUMBER.getCode())) {
			hotspot.setHowMany(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.DATE.getCode())) {
			hotspot.setObservationDate(namedEntity);
		}
		return hotspot;
	}

	private void createRdfModel(List<TweetDTO> filteredTweets) {

	}

	@Override
	public void persistTweets(List<TweetDTO> tweets) {
		twitterRepo.insertTweets(MapTransformer.toTweetsFromDTO(tweets));
	}

	private void initializePipeline() {
		loadFile(StanfordEnum.NER_BISP_MODEL_VALUE.getCode());
		Properties props = new Properties();
		props.put(StanfordEnum.PROPS_KEY.getCode(), StanfordEnum.PROPS_VALUE.getCode());
		props.put(StanfordEnum.NER_MODEL_KEY.getCode(), StanfordEnum.NER_BISP_MODEL_VALUE.getCode());
		pipeline = new StanfordCoreNLP(props);
	}

	private void loadFile(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		classLoader.getResource(fileName);
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
			case 8:
				hashtag = TwitterEnum.BIRDMIGRATING.getCode();
				break;
			case 9:
				hashtag = TwitterEnum.BIRDWATCHING.getCode();
				break;
			case 10:
				hashtag = TwitterEnum.BIRD_WATCHING.getCode();
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

	@Override
	public void persistEbirdData(List<EbirdDataDTO> ebirds) {
		// TODO Auto-generated method stub
	}

}
