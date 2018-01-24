package bimr.bf;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.RdfFacade;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.TweetScheduleFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TweetRequestDTO;
import bimr.bfcl.dto.TweetResponseDTO;
import bimr.util.GeneralConstants;
import bimr.util.StanfordEnum;
import bimr.util.TwitterEnum;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Singleton
@Startup
public class TweetScheduleFacadeBean implements TweetScheduleFacade {

	private static final Logger log = Logger.getLogger(TweetScheduleFacadeBean.class.getName());
	private static Integer count;
	private static StanfordCoreNLP pipeline;

	@EJB
	TweetFacade tweetFacade;

	@EJB
	RdfFacade rdfFacade;

	@PostConstruct
	public void init() {
		count = 0;
		// initializePipeline();
	}

	@Override
	@Schedule(minute = "*/15", hour = "*", persistent = false)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void twitterApiCallScheduled() {
		TweetResponseDTO response = tweetFacade.retrieveTweetsFromApi(createRequest());
		if (!response.getTweets().isEmpty()) {
			tweetFacade.persistTweets(response.getTweets());
			//			filterTweets(response.getTweets());
		} else {
			log.info("No data from Twitter API");
		}
	}

	private void filterTweets(List<TweetDTO> tweets) {
		List<HotspotDTO> hotspots = new ArrayList<>();
		for (TweetDTO tweet : tweets) {
			Annotation document = new Annotation(tweet.getTweetMessage());
			// run all Annotators on this text
			pipeline.annotate(document);
			HotspotDTO hotspot = parseTweet(document, tweet.getTweetId());
			hotspot.setLatitude(tweet.getLatitude());
			hotspot.setLongitude(tweet.getLongitude());
			if (hotspot.getBirdSpecies() != null && tweet.getLatitude() != null
					|| hotspot.getLocationName() != null) {
				hotspots.add(MapTransformer.toHotspotDTOFromTweetDTO(tweet));
			}
		}
		if (!hotspots.isEmpty()) {
			rdfFacade.generateRdfModel(hotspots);
		}
	}

	private static void initializePipeline() {
		Properties props = new Properties();
		props.put(StanfordEnum.PROPS_KEY.getCode(), StanfordEnum.PROPS_VALUE.getCode());
		props.put(StanfordEnum.NER_MODEL_KEY.getCode(), StanfordEnum.NER_BISP_MODEL_VALUE.getCode());
		pipeline = new StanfordCoreNLP(props);
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

				//TODO solve this issue
				HotspotDTO freshHotspot = extractSensitiveInfoFromTweet(namedEntity);
				hotspot.setBirdSpecies(freshHotspot.getBirdSpecies());
				hotspot.setHowMany(freshHotspot.getHowMany());
				hotspot.setObservationDate(freshHotspot.getObservationDate());
				hotspot.setLocationName(freshHotspot.getLocationName());

				//TODO remove this method in the final version
				logInfo(token, namedEntity);
			}
		}
		return hotspot;
	}

	private void logInfo(CoreLabel token, String namedEntity) {
		// this is the text of the token
		String word = token.get(CoreAnnotations.TextAnnotation.class);
		// this is the POS tag of the token
		String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
		String text = String.format("Print: word: [%s] pos: [%s] ne: [%s]", word, pos, namedEntity);
		log.info(text);
	}

	private HotspotDTO extractSensitiveInfoFromTweet(String namedEntity) {
		HotspotDTO hotspot = new HotspotDTO();
		List<String> birdSpecies = new ArrayList<>();
		if (namedEntity.equals(StanfordEnum.LOCATION.getCode())) {
			hotspot.setLocationName(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.BISP.getCode())) {
			birdSpecies.add(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.NUMBER.getCode())) {
			hotspot.setHowMany(namedEntity);
		} else if (namedEntity.equals(StanfordEnum.DATE.getCode())) {
			hotspot.setObservationDate(namedEntity);
		}
		hotspot.setBirdSpecies(birdSpecies);
		return hotspot;
	}

	private TweetRequestDTO createRequest() {
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
		return new TweetRequestDTO(hashtag, tweetFacade.retrieveLastTweetId());
	}
}
