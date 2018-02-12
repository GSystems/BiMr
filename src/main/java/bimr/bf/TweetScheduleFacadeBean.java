package bimr.bf;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.BimrOntologyPersistFacade;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.TweetScheduleFacade;
import bimr.bfcl.dto.*;
import bimr.util.GeneralConstants;
import bimr.util.StanfordEnum;
import bimr.util.TwitterEnum;
import bimr.util.rdf.vocabulary.BIMR;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.Model;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Startup
@Singleton
public class TweetScheduleFacadeBean implements TweetScheduleFacade {

	private static final Logger log = Logger.getLogger(TweetScheduleFacadeBean.class.getName());
	private static Integer hashtagIterator;
	private static StanfordCoreNLP pipeline;

	@EJB
	private TweetFacade tweetFacade;

	@EJB
	private BimrOntologyPersistFacade rdfFacade;

	@PostConstruct
	public void init() {
		hashtagIterator = 0;
		initializePipeline();
	}

	@Override
//	@Schedule(minute = "*/15", hour = "*", persistent = false)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void twitterApiCallScheduled() {
//		TweetResponseDTO response = tweetFacade.retrieveTweetsFromApi(createRequest());
//		if (!response.getTweets().isEmpty()) {
//			tweetFacade.persistTweetsInRelationalDb(response.getTweets());

//			List<HotspotDTO> hotspots = filterTweetsAndGenerateHotspots(response.getTweets());

		List<HotspotDTO> hotspots = filterTweetsAndGenerateHotspots(mockTweets());

			if (!hotspots.isEmpty()) {
				createAndPersistModelsForHotspot(hotspots);
			}
//		} else {
//			log.info("No data from Twitter API");
//		}
	}

	private void createAndPersistModelsForHotspot(List<HotspotDTO> hotspots) {
		Map<String, List<Model>> allModelsForHotspot = rdfFacade.createModelsForHotspot(hotspots);
		for (Map.Entry<String, List<Model>> entry : allModelsForHotspot.entrySet()) {
			for (Model model : entry.getValue()) {
				rdfFacade.persistAllModelsForAHotspot(model);
			}
		}
	}

	private List<HotspotDTO> filterTweetsAndGenerateHotspots(List<TweetDTO> tweets) {
		List<HotspotDTO> hotspots = new ArrayList<>();
		for (TweetDTO tweet : tweets) {
			Annotation document = new Annotation(tweet.getTweetMessage());
			// run all Annotators on this text
			pipeline.annotate(document);
			HotspotDTO hotspot = parseTweet(document, tweet.getTweetId());
			hotspot.getLocation().setGeo(Pair.of(tweet.getLatitude(), tweet.getLongitude()));
			if (hotspot.getBirdSpecies() != null && tweet.getLatitude() != null || hotspot.getLocation().getCity() != null) {
				hotspots.add(MapTransformer.addInfoFromTweetToHotspot(tweet, hotspot));
			}
		}
		return hotspots;
	}

	private void initializePipeline() {
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
		LocationDTO location = new LocationDTO();
		hotspot.setLocation(location);

		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// this is the NER label of the token
				String namedEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
				if (!namedEntity.equals(StanfordEnum.DEFAULT_NER.getCode())) {
					String word = token.get(CoreAnnotations.TextAnnotation.class);
					extractSensitiveInfoFromTweet(namedEntity, word, hotspot);

					logInfo(token, namedEntity);
				}
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

	private void extractSensitiveInfoFromTweet(String namedEntity, String word, HotspotDTO hotspot) {
		if (namedEntity.equals(StanfordEnum.LOCATION.getCode())) {
			hotspot.getLocation().setCity(word);
		} else if (namedEntity.equals(StanfordEnum.BISP.getCode())) {
			birdSpeciesController(word, hotspot);
		} else if (namedEntity.equals(StanfordEnum.NUMBER.getCode())) {
//			try {
//				hotspot.setHowMany(Integer.valueOf(word));
//			} catch (Exception e) {
//				log.info("invalid integer format: " + word);
//			}
		} else if (namedEntity.equals(StanfordEnum.DATE.getCode())) {
//			try {
//				hotspot.setObservationDate(getDateFromString(word));
//			} catch (Exception e) {
//				log.info("invalid date format: " + word);
//			}
		} else if (namedEntity.equals(StanfordEnum.LINK.getCode())) {
			hotspot.setLink(word);
		} else if (namedEntity.equals(StanfordEnum.AUTHOR.getCode())) {
			hotspot.setAuthor(word);
		}
	}

	private void birdSpeciesController(String word, HotspotDTO hotspot) {
		List<String> birdSpecies;
		if (hotspot.getBirdSpecies() != null) {
			birdSpecies = hotspot.getBirdSpecies();
			birdSpecies.add(word);
		} else {
			birdSpecies = new ArrayList<>();
			birdSpecies.add(word);
			hotspot.setBirdSpecies(birdSpecies);
		}
	}

	private TweetRequestDTO createRequest() {
		String hashtag;
		switch (hashtagIterator) {
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
		hashtagIterator++;
		if (hashtagIterator > GeneralConstants.MAX_NUMBER_HASHTAGS) {
			hashtagIterator = 0;
		}
		return new TweetRequestDTO(hashtag, tweetFacade.retrieveLastTweetId());
	}

	private void replaceTagNames(Model model) {
		model.setNsPrefix("hotspot", BIMR.getBaseUri(""));
	}

	private LocalDateTime getDateFromString(String sDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(GeneralConstants.XSD_DATE_TIME_FORMAT);
		return LocalDateTime.parse(sDate, formatter);
	}

	public List<TweetDTO> mockTweets() {
		List<TweetDTO> migrationTweets = new ArrayList<>();

		TweetDTO tweetDTO11 = new TweetDTO();
		// #1.1
		tweetDTO11.setLatitude(38.44);
		tweetDTO11.setLongitude(-105.70);
		tweetDTO11.setUser(new TwitterUserDTO(){{setName("Arnold Sheppard");}});
		tweetDTO11.getUser().setId(111L);
		tweetDTO11.setTweetId(11L);
		tweetDTO11.setTweetMessage("Young hawk looking down at me");
		tweetDTO11.setObservationDate(getDateFromString("2017-12-13T09:30:10.123"));
		migrationTweets.add(tweetDTO11);

		TweetDTO tweetDTO12 = new TweetDTO();
		// #1.2
		tweetDTO12.setLatitude(37.26);
		tweetDTO12.setLongitude(-105.60);
		tweetDTO12.setUser(new TwitterUserDTO(){{setName("John Wick");}});
		tweetDTO12.getUser().setId(112L);
		tweetDTO12.setTweetId(12L);
		tweetDTO12.setTweetMessage("You've never seen a hawk do this!");
		tweetDTO12.setObservationDate(getDateFromString("2017-12-13T09:30:10.123"));
		migrationTweets.add(tweetDTO12);

		TweetDTO tweetDTO13 = new TweetDTO();
		// #1.3
		tweetDTO13.setLatitude(36.31);
		tweetDTO13.setLongitude(-108.49);
		tweetDTO13.setUser(new TwitterUserDTO(){{setName("Tobey Marshall");}});
		tweetDTO13.getUser().setId(113L);
		tweetDTO13.setTweetId(13L);
		tweetDTO13.setTweetMessage("You guys! This hawk scared the S**T out of me today. Saw him nearby");
		tweetDTO13.setObservationDate(getDateFromString("2017-12-14T09:30:10.123"));
		migrationTweets.add(tweetDTO13);

		TweetDTO tweetDTO14 = new TweetDTO();
		// #1.4
		tweetDTO14.setLatitude(32.60);
		tweetDTO14.setLongitude(-110.33);
		tweetDTO14.setUser(new TwitterUserDTO(){{setName("Dino Brewster");}});
		tweetDTO14.getUser().setId(114L);
		tweetDTO14.setTweetId(14L);
		tweetDTO14.setTweetMessage("Young hawk claming for food.");
		tweetDTO14.setObservationDate(getDateFromString("2017-12-15T09:30:10.123"));
		migrationTweets.add(tweetDTO14);

		TweetDTO tweetDTO15 = new TweetDTO();
		// #1.5
		tweetDTO15.setLatitude(30.45);
		tweetDTO15.setLongitude(-109.14);
		tweetDTO15.setUser(new TwitterUserDTO(){{setName("Tego Calderon");}});
		tweetDTO15.getUser().setId(115L);
		tweetDTO15.setTweetId(15L);
		tweetDTO15.setTweetMessage("The hawks need our help. Easy winter feeding?");
		tweetDTO15.setObservationDate(getDateFromString("2017-12-16T09:30:10.123"));
		migrationTweets.add(tweetDTO15);

		TweetDTO tweetDTO16 = new TweetDTO();
		// #1.6
		tweetDTO16.setLatitude(28.67);
		tweetDTO16.setLongitude(-107.78);
		tweetDTO16.setUser(new TwitterUserDTO(){{setName("Arturo Braga");}});
		tweetDTO16.getUser().setId(116L);
		tweetDTO16.setTweetId(16L);
		tweetDTO16.setTweetMessage("World's strangest birds - hawk");
		tweetDTO16.setObservationDate(getDateFromString("2017-12-17T09:30:10.123"));
		migrationTweets.add(tweetDTO16);

		TweetDTO tweetDTO17 = new TweetDTO();
		// #1.7
		tweetDTO17.setLatitude(26.76);
		tweetDTO17.setLongitude(-109.05);
		tweetDTO17.setUser(new TwitterUserDTO(){{setName("Roberto Soldado");}});
		tweetDTO17.getUser().setId(117L);
		tweetDTO17.setTweetId(17L);
		tweetDTO17.setTweetMessage("The resident hawk has been enjoying playing around in the snow");
		tweetDTO17.setObservationDate(getDateFromString("2017-12-17T09:30:10.123"));
		migrationTweets.add(tweetDTO17);

		TweetDTO tweetDTO18 = new TweetDTO();
		// #1.8
		tweetDTO18.setLatitude(25.80);
		tweetDTO18.setLongitude(-109.09);
		tweetDTO18.setUser(new TwitterUserDTO(){{setName("Julio Fonseca");}});
		tweetDTO18.getUser().setId(118L);
		tweetDTO18.setTweetId(18L);
		tweetDTO18.setTweetMessage("Wintering - hawk return");
		tweetDTO18.setObservationDate(getDateFromString("2017-12-18T09:30:10.123"));
		migrationTweets.add(tweetDTO18);

		TweetDTO tweetDTO19 = new TweetDTO();
		// #1.9
		tweetDTO19.setLatitude(24.53);
		tweetDTO19.setLongitude(-111.30);
		tweetDTO19.setUser(new TwitterUserDTO(){{setName("Hernan Reyes");}});
		tweetDTO19.getUser().setId(119L);
		tweetDTO19.setTweetId(19L);
		tweetDTO19.setTweetMessage("Some birds of paradise are the hawk");
		tweetDTO19.setObservationDate(getDateFromString("2017-12-20T09:30:10.123"));
		migrationTweets.add(tweetDTO19);

		TweetDTO tweetDTO110 = new TweetDTO();
		// #1.10
		tweetDTO110.setLatitude(23.52);
		tweetDTO110.setLongitude(-109.90);
		tweetDTO110.setUser(new TwitterUserDTO(){{setName("Elena Neves");}});
		tweetDTO110.getUser().setId(1110L);
		tweetDTO110.setTweetId(110L);
		tweetDTO110.setTweetMessage("What kinda bird is that? It is a hawk, right?");
		tweetDTO110.setObservationDate(getDateFromString("2017-12-21T09:30:10.123"));
		migrationTweets.add(tweetDTO110);


		TweetDTO tweetDTO21 = new TweetDTO();
		// #2.1
		tweetDTO21.setLatitude(47.27);
		tweetDTO21.setLongitude(-74.73);
		tweetDTO21.setUser(new TwitterUserDTO(){{setName("Chris Smalling");}});
		tweetDTO21.getUser().setId(121L);
		tweetDTO21.setTweetId(21L);
		tweetDTO21.setTweetMessage("What is like to fly along side a flock of Osprey?");
		tweetDTO21.setObservationDate(getDateFromString("2017-10-24T09:30:10.123"));
		migrationTweets.add(tweetDTO21);

		TweetDTO tweetDTO22 = new TweetDTO();
		// #2.2
		tweetDTO22.setLatitude(45.74);
		tweetDTO22.setLongitude(-75.76);
		tweetDTO22.setUser(new TwitterUserDTO(){{setName("Ross Barkley");}});
		tweetDTO22.getUser().setId(122L);
		tweetDTO22.setTweetId(22L);
		tweetDTO22.setTweetMessage("White-throated Osprey going the distance");
		tweetDTO22.setObservationDate(getDateFromString("2017-10-25T09:30:10.123"));
		migrationTweets.add(tweetDTO22);

		TweetDTO tweetDTO23 = new TweetDTO();
		// #2.3
		tweetDTO23.setLatitude(43.85);
		tweetDTO23.setLongitude(-75.25);
		tweetDTO23.setUser(new TwitterUserDTO(){{setName("Chris Bridges");}});
		tweetDTO23.getUser().setId(123L);
		tweetDTO23.setTweetId(23L);
		tweetDTO23.setTweetMessage("Why these Osprey carry flames in their beaks");
		tweetDTO23.setObservationDate(getDateFromString("2017-10-26T09:30:10.123"));
		migrationTweets.add(tweetDTO23);

		TweetDTO tweetDTO24 = new TweetDTO();
		// #2.4
		tweetDTO24.setLatitude(42.40);
		tweetDTO24.setLongitude(-74.58);
		tweetDTO24.setUser(new TwitterUserDTO(){{setName("Jim Waters");}});
		tweetDTO24.getUser().setId(124L);
		tweetDTO24.setTweetId(24L);
		tweetDTO24.setTweetMessage("Wild Osprey nesting spot");
		tweetDTO24.setObservationDate(getDateFromString("2017-10-26T09:30:10.123"));
		migrationTweets.add(tweetDTO24);

		TweetDTO tweetDTO25 = new TweetDTO();
		// #2.5
		tweetDTO25.setLatitude(40.29);
		tweetDTO25.setLongitude(-77.24);
		tweetDTO25.setUser(new TwitterUserDTO(){{setName("Gary Silverton");}});
		tweetDTO25.getUser().setId(125L);
		tweetDTO25.setTweetId(25L);
		tweetDTO25.setTweetMessage("What a hoot! Just look at this Osprey!");
		tweetDTO25.setObservationDate(getDateFromString("2017-10-27T09:30:10.123"));
		migrationTweets.add(tweetDTO25);

		TweetDTO tweetDTO26 = new TweetDTO();
		// #2.6
		tweetDTO26.setLatitude(38.37);
		tweetDTO26.setLongitude(-77.81);
		tweetDTO26.setUser(new TwitterUserDTO(){{setName("Jamie Vardy");}});
		tweetDTO26.getUser().setId(126L);
		tweetDTO26.setTweetId(26L);
		tweetDTO26.setTweetMessage("Whopper Osprey at Washington outskirts today");
		tweetDTO26.setObservationDate(getDateFromString("2017-10-29T09:30:10.123"));
		migrationTweets.add(tweetDTO26);

		TweetDTO tweetDTO27 = new TweetDTO();
		// #2.7
		tweetDTO27.setLatitude(36.06);
		tweetDTO27.setLongitude(-77.07);
		tweetDTO27.setUser(new TwitterUserDTO(){{setName("Owen Hargreaves");}});
		tweetDTO27.getUser().setId(127L);
		tweetDTO27.setTweetId(27L);
		tweetDTO27.setTweetMessage("What a cutie!!! Do you have a favorite Osprey pic?");
		tweetDTO27.setObservationDate(getDateFromString("2017-10-30T09:30:10.123"));
		migrationTweets.add(tweetDTO27);

		TweetDTO tweetDTO28 = new TweetDTO();
		// #2.8
		tweetDTO28.setLatitude(32.95);
		tweetDTO28.setLongitude(-80.05);
		tweetDTO28.setUser(new TwitterUserDTO(){{setName("Andy Anderson");}});
		tweetDTO28.getUser().setId(128L);
		tweetDTO28.setTweetId(28L);
		tweetDTO28.setTweetMessage("We are officially off to track down a Osprey!");
		tweetDTO28.setObservationDate(getDateFromString("2017-10-31T09:30:10.123"));
		migrationTweets.add(tweetDTO28);

		TweetDTO tweetDTO29 = new TweetDTO();
		// #2.9
		tweetDTO29.setLatitude(30.81);
		tweetDTO29.setLongitude(-83.50);
		tweetDTO29.setUser(new TwitterUserDTO(){{setName("David Duchovny");}});
		tweetDTO29.getUser().setId(129L);
		tweetDTO29.setTweetId(29L);
		tweetDTO29.setTweetMessage("Look at this! Just found a flock of Osprey");
		tweetDTO29.setObservationDate(getDateFromString("2017-11-01T09:30:10.123"));
		migrationTweets.add(tweetDTO29);

		TweetDTO tweetDTO210 = new TweetDTO();
		// #2.10
		tweetDTO210.setLatitude(27.51);
		tweetDTO210.setLongitude(-82.47);
		tweetDTO210.setUser(new TwitterUserDTO(){{setName("Cody Walker");}});
		tweetDTO210.getUser().setId(1101L);
		tweetDTO210.setTweetId(210L);
		tweetDTO210.setTweetMessage("Usually don't see this guy so active - Osprey");
		tweetDTO210.setObservationDate(getDateFromString("2017-11-02T09:30:10.123"));
		migrationTweets.add(tweetDTO210);


		TweetDTO tweetDTO31 = new TweetDTO();
		// #3.1
		tweetDTO31.setLatitude(58.68);
		tweetDTO31.setLongitude(7.17);
		tweetDTO31.setUser(new TwitterUserDTO(){{setName("Ankjell Karlof");}});
		tweetDTO31.getUser().setId(131L);
		tweetDTO31.setTweetId(31L);
		tweetDTO31.setTweetMessage("Urbanized Eagle loitering in front of a KFC");
		tweetDTO31.setObservationDate(getDateFromString("2018-01-03T09:30:10.123"));
		migrationTweets.add(tweetDTO31);

		TweetDTO tweetDTO32 = new TweetDTO();
		// #3.2
		tweetDTO32.setLatitude(57.52);
		tweetDTO32.setLongitude(12.81);
		tweetDTO32.setUser(new TwitterUserDTO(){{setName("Markus Berg");}});
		tweetDTO32.getUser().setId(132L);
		tweetDTO32.setTweetId(32L);
		tweetDTO32.setTweetMessage("Eagle Friday! This pair of eagles are our observation today.");
		tweetDTO32.setObservationDate(getDateFromString("2018-01-05T09:30:10.123"));
		migrationTweets.add(tweetDTO32);

		TweetDTO tweetDTO33 = new TweetDTO();
		// #3.3
		tweetDTO33.setLatitude(55.14);
		tweetDTO33.setLongitude(11.87);
		tweetDTO33.setUser(new TwitterUserDTO(){{setName("Christian Eriksen");}});
		tweetDTO33.getUser().setId(133L);
		tweetDTO33.setTweetId(33L);
		tweetDTO33.setTweetMessage("Where do our Eagle come from and go to?");
		tweetDTO33.setObservationDate(getDateFromString("2018-01-06T09:30:10.123"));
		migrationTweets.add(tweetDTO33);

		TweetDTO tweetDTO34 = new TweetDTO();
		// #3.4
		tweetDTO34.setLatitude(52.87);
		tweetDTO34.setLongitude(8.11);
		tweetDTO34.setUser(new TwitterUserDTO(){{setName("Robert Adler");}});
		tweetDTO34.getUser().setId(134L);
		tweetDTO34.setTweetId(34L);
		tweetDTO34.setTweetMessage("Tweety pie the cute Eagle being showered with love???");
		tweetDTO34.setObservationDate(getDateFromString("2018-01-08T09:30:10.123"));
		migrationTweets.add(tweetDTO34);

		TweetDTO tweetDTO35 = new TweetDTO();
		// #3.5
		tweetDTO35.setLatitude(50.87);
		tweetDTO35.setLongitude(-5.91);
		tweetDTO35.setUser(new TwitterUserDTO(){{setName("Lasse Schone");}});
		tweetDTO35.getUser().setId(135L);
		tweetDTO35.setTweetId(35L);
		tweetDTO35.setTweetMessage("Tufted Eagle spotted");
		tweetDTO35.setObservationDate(getDateFromString("2018-01-09T09:30:10.123"));
		migrationTweets.add(tweetDTO35);

		TweetDTO tweetDTO36 = new TweetDTO();
		// #3.6
		tweetDTO36.setLatitude(50.08);
		tweetDTO36.setLongitude(5.08);
		tweetDTO36.setUser(new TwitterUserDTO(){{setName("Axel Witsel");}});
		tweetDTO36.getUser().setId(136L);
		tweetDTO36.setTweetId(36L);
		tweetDTO36.setTweetMessage("Tricolored eagle Eagle outskirts of the city");
		tweetDTO36.setObservationDate(getDateFromString("2018-01-09T09:30:10.123"));
		migrationTweets.add(tweetDTO36);

		TweetDTO tweetDTO37 = new TweetDTO();
		// #3.7
		tweetDTO37.setLatitude(48.44);
		tweetDTO37.setLongitude(4.76);
		tweetDTO37.setUser(new TwitterUserDTO(){{setName("Alexandre Lacazette");}});
		tweetDTO37.getUser().setId(137L);
		tweetDTO37.setTweetId(37L);
		tweetDTO37.setTweetMessage("Tourism sector to promote Eagle as niche market");
		tweetDTO37.setObservationDate(getDateFromString("2018-01-11T09:30:10.123"));
		migrationTweets.add(tweetDTO37);

		TweetDTO tweetDTO38 = new TweetDTO();
		// #3.8
		tweetDTO38.setLatitude(45.44);
		tweetDTO38.setLongitude(0.48);
		tweetDTO38.setUser(new TwitterUserDTO(){{setName("Hugo Lloris");}});
		tweetDTO38.getUser().setId(138L);
		tweetDTO38.setTweetId(38L);
		tweetDTO38.setTweetMessage("Tiny Eagles try to get something to eat in my backyard");
		tweetDTO38.setObservationDate(getDateFromString("2018-01-12T09:30:10.123"));
		migrationTweets.add(tweetDTO38);

		TweetDTO tweetDTO39 = new TweetDTO();
		// #3.9
		tweetDTO39.setLatitude(42.66);
		tweetDTO39.setLongitude(-4.53);
		tweetDTO39.setUser(new TwitterUserDTO(){{setName("David Silva");}});
		tweetDTO39.getUser().setId(139L);
		tweetDTO39.setTweetId(39L);
		tweetDTO39.setTweetMessage("Through the lens: Eagles sighted!");
		tweetDTO39.setObservationDate(getDateFromString("2018-01-14T09:30:10.123"));
		migrationTweets.add(tweetDTO39);

		TweetDTO tweetDTO310 = new TweetDTO();
		// #3.10
		tweetDTO310.setLatitude(39.68);
		tweetDTO310.setLongitude(-8.81);
		tweetDTO310.setUser(new TwitterUserDTO(){{setName("Andre Gomes");}});
		tweetDTO310.getUser().setId(3110L);
		tweetDTO310.setTweetId(310L);
		tweetDTO310.setTweetMessage("This red-tailed Eagle is so well camouflaged that I came within 6 feet of it before seeing it");
		tweetDTO310.setObservationDate(getDateFromString("2018-01-16T09:30:10.123"));
		migrationTweets.add(tweetDTO310);


		TweetDTO tweetDTO41 = new TweetDTO();
		// #4.1
		tweetDTO41.setLatitude(59.54);
		tweetDTO41.setLongitude(39.36);
		tweetDTO41.setUser(new TwitterUserDTO(){{setName("Andrey Kokorin");}});
		tweetDTO41.getUser().setId(141L);
		tweetDTO41.setTweetId(41L);
		tweetDTO41.setTweetMessage("This Killdeer had quite a bit to say to us this morning.");
		tweetDTO41.setObservationDate(getDateFromString("2018-01-06T09:30:10.123"));
		migrationTweets.add(tweetDTO41);

		TweetDTO tweetDTO42 = new TweetDTO();
		// #4.2
		tweetDTO42.setLatitude(56.21);
		tweetDTO42.setLongitude(34.13);
		tweetDTO42.setUser(new TwitterUserDTO(){{setName("Alan Dzagoev");}});
		tweetDTO42.getUser().setId(142L);
		tweetDTO42.setTweetId(42L);
		tweetDTO42.setTweetMessage("This is one of the  4 subspecies of the spectacular Killdeer!");
		tweetDTO42.setObservationDate(getDateFromString("2018-01-07T09:30:10.123"));
		migrationTweets.add(tweetDTO42);

		TweetDTO tweetDTO43 = new TweetDTO();
		// #4.3
		tweetDTO43.setLatitude(53.44);
		tweetDTO43.setLongitude(31.10);
		tweetDTO43.setUser(new TwitterUserDTO(){{setName("Sergei Gurenko");}});
		tweetDTO43.getUser().setId(143L);
		tweetDTO43.getUser().setScreenName("sergurk");
		tweetDTO43.getUser().setLocation("Dublin");
		tweetDTO43.getUser().setIsGeoEnabled("true");
		tweetDTO43.getUser().setEmail("sergurk@gmail.com");
		tweetDTO43.setTweetId(43L);
		tweetDTO43.setTweetMessage("This adorable Killdeer was making the most of a very overcast day");
		tweetDTO43.setObservationDate(getDateFromString("2018-01-08T09:30:10.123"));
		migrationTweets.add(tweetDTO43);

		TweetDTO tweetDTO44 = new TweetDTO();
		// #4.4
		tweetDTO44.setLatitude(50.54);
		tweetDTO44.setLongitude(27.76);
		tweetDTO44.setUser(new TwitterUserDTO(){{setName("Andryi Shevchenko");}});
		tweetDTO44.getUser().setId(144L);
		tweetDTO44.setTweetId(44L);
		tweetDTO44.setTweetMessage("Think Killdeer watching is something only hardened people enjoy?");
		tweetDTO44.setObservationDate(getDateFromString("2018-01-09T09:30:10.123"));
		migrationTweets.add(tweetDTO44);

		TweetDTO tweetDTO45 = new TweetDTO();
		// #4.5
		tweetDTO45.setLatitude(47.20);
		tweetDTO45.setLongitude(27.28);
		tweetDTO45.setUser(new TwitterUserDTO(){{setName("Alexandru Mocanu");}});
		tweetDTO45.getUser().setId(145L);
		tweetDTO45.setTweetId(45L);
		tweetDTO45.setTweetMessage("The trees filled with little Killdeers");
		tweetDTO45.setObservationDate(getDateFromString("2018-01-11T09:30:10.123"));
		migrationTweets.add(tweetDTO45);

		TweetDTO tweetDTO46 = new TweetDTO();
		// #4.6
		tweetDTO46.setLatitude(44.40);
		tweetDTO46.setLongitude(26.23);
		tweetDTO46.setUser(new TwitterUserDTO(){{setName("Ioan Petrescu");}});
		tweetDTO46.getUser().setId(146L);
		tweetDTO46.setTweetId(46L);
		tweetDTO46.setTweetMessage("The winter months are a perfect time to keep an eye out for beautiful Killdeer");
		tweetDTO46.setObservationDate(getDateFromString("2018-01-11T09:30:10.123"));
		migrationTweets.add(tweetDTO46);

		TweetDTO tweetDTO47 = new TweetDTO();
		// #4.7
		tweetDTO47.setLatitude(37.80);
		tweetDTO47.setLongitude(22.15);
		tweetDTO47.setUser(new TwitterUserDTO(){{setName("Sokratis Papastathopoulos");}});
		tweetDTO47.getUser().setId(147L);
		tweetDTO47.setTweetId(47L);
		tweetDTO47.setTweetMessage("The Killdeer found one of the only open water spaces today in this crack");
		tweetDTO47.setObservationDate(getDateFromString("2018-01-14T09:30:10.123"));
		migrationTweets.add(tweetDTO47);

		TweetDTO tweetDTO48 = new TweetDTO();
		// #4.8
		tweetDTO48.setLatitude(30.05);
		tweetDTO48.setLongitude(26.01);
		tweetDTO48.setUser(new TwitterUserDTO(){{setName("Asim Ramses");}});
		tweetDTO48.getUser().setId(148L);
		tweetDTO48.setTweetId(48L);
		tweetDTO48.setTweetMessage("The other day I spent a long time chasing Killdeer around");
		tweetDTO48.setObservationDate(getDateFromString("2018-01-16T09:30:10.123"));
		migrationTweets.add(tweetDTO48);

		TweetDTO tweetDTO49 = new TweetDTO();
		// #4.9
		tweetDTO49.setLatitude(20.75);
		tweetDTO49.setLongitude(35.21);
		tweetDTO49.setUser(new TwitterUserDTO(){{setName("Bakri Bashir");}});
		tweetDTO49.getUser().setId(149L);
		tweetDTO49.setTweetId(49L);
		tweetDTO49.setTweetMessage("The colourful Killdeer of this earth astound me");
		tweetDTO49.setObservationDate(getDateFromString("2018-01-17T09:30:10.123"));
		migrationTweets.add(tweetDTO49);

		TweetDTO tweetDTO410 = new TweetDTO();
		// #4.10
		tweetDTO410.setLatitude(13.15);
		tweetDTO410.setLongitude(40.43);
		tweetDTO410.setUser(new TwitterUserDTO(){{setName("Jemal Tassew");}});
		tweetDTO410.getUser().setId(1109L);
		tweetDTO410.setTweetId(410L);
		tweetDTO410.setTweetMessage("The Killdeers are busy chasing each other today.");
		tweetDTO410.setObservationDate(getDateFromString("2018-01-19T09:30:10.123"));
		migrationTweets.add(tweetDTO410);

		return migrationTweets;
	}

	public List<HotspotDTO> mockHotspots() {
		List<HotspotDTO> migrationTweets = new ArrayList<>();
		HotspotDTO hotspotDTO1 = new HotspotDTO();
		HotspotDTO hotspotDTO2 = new HotspotDTO();
		LocationDTO locationDTO1 = new LocationDTO();
		LocationDTO locationDTO2 = new LocationDTO();
		hotspotDTO1.setLocation(locationDTO1);
		hotspotDTO2.setLocation(locationDTO2);

		hotspotDTO1.getLocation().setGeo(Pair.of(38.44, -105.70));
		hotspotDTO1.setHowMany(18);
		hotspotDTO1.setAuthor("Phil Jones");
		hotspotDTO1.getLocation().setState("Colorado");
		hotspotDTO1.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO1.setObservationDate(getDateFromString("2017-12-13"));
		hotspotDTO1.setInformationSource("twitter");
		hotspotDTO1.getBirdSpecies().add("duck");
		hotspotDTO1.setTweetMessage("A beautiful Swainson hawk just ate a duck");
		hotspotDTO1.setLink("https://tinyurl.com/ywqaers");
		hotspotDTO1.setTweetId(12524524213L);
		hotspotDTO1.setHotspotId("508d6sadgadsgfadf7s");
		hotspotDTO1.getLocation().setCity("Ildaho");
		hotspotDTO1.getLocation().setState("Colorado");
		hotspotDTO1.getLocation().setCountry("USA");
		TwitterUserDTO user1 = new TwitterUserDTO();
		user1.setEmail("phil.jones@gmail.com");
		user1.setIsGeoEnabled("false");
		user1.setLocation("Colorado");
		user1.setScreenName("jonny");
		user1.setName("Phil Jones");
		user1.setId(1251231232L);
		hotspotDTO1.setUser(user1);
		migrationTweets.add(hotspotDTO1);

		hotspotDTO1.getLocation().setGeo(Pair.of(18.44, -105.70));
		hotspotDTO2.setHowMany(12);
		hotspotDTO2.setAuthor("Gabriel Garcia");
		hotspotDTO2.getLocation().setState("California");
		hotspotDTO2.setBirdSpecies(new ArrayList<>(Collections.singletonList("Red Woodpecker")));
		hotspotDTO2.setObservationDate(getDateFromString("2018-01-13"));
		hotspotDTO2.setInformationSource("twitter");
		hotspotDTO2.getBirdSpecies().add("swan");
		hotspotDTO2.setTweetMessage("I just saw a Red Woodpecker playing with a swan.");
		hotspotDTO2.setLink("https://tinyurl.com/ldghpcvcv");
		hotspotDTO2.setTweetId(895472934L);
		hotspotDTO2.setHotspotId("ehngb2v59t4o");
		hotspotDTO2.getLocation().setCity("Cupertino");
		hotspotDTO2.getLocation().setState("California");
		hotspotDTO2.getLocation().setCountry("USA");
		TwitterUserDTO user2 = new TwitterUserDTO();
		user2.setEmail("gabriel.garcia@gmail.com");
		user2.setIsGeoEnabled("true");
		user2.setLocation("California");
		user2.setScreenName("garc");
		user2.setName("Gabriel Garcia");
		user2.setId(20345230465L);
		hotspotDTO2.setUser(user2);
		migrationTweets.add(hotspotDTO2);

		return migrationTweets;
	}
}
