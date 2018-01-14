package bimr.bf;

import java.util.*;

import javax.ejb.Stateless;
import javax.inject.Inject;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.TweetFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TweetRequestDTO;
import bimr.bfcl.dto.TweetResponseDTO;
import bimr.df.TweetRepo;
import bimr.util.AsyncUtils;
import bimr.util.GeneralConstants;
import bimr.util.RdfEnum;
import bimr.util.rdf.ontology.Bisp;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

/**
 * @author GLK
 */
@Stateless
public class TweetFacadeBean implements TweetFacade {

	@Inject
	private TweetRepo tweetRepo;

	@Override
	public List<TweetDTO> retrieveTweetsFromDB() {
		return MapTransformer.fromTweetsToDTO(AsyncUtils.getResultFromAsyncTask(tweetRepo.retrieveTweetsFromDB()));
	}

	@Override
	public TweetResponseDTO retrieveTweetsFromApi(TweetRequestDTO request) {
		return MapTransformer.fromTwitterResponseToDTO(AsyncUtils
				.getResultFromAsyncTask(tweetRepo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request))));
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

	@Override
	public void createRdfModel(List<HotspotDTO> tweets) {
		Model model = ModelFactory.createDefaultModel();

		for (HotspotDTO hotspotDTO : tweets) {

			Resource hotspotResource = model.createResource(RdfEnum.URI.getCode() + "1");

//			Map<Map<Resource, String>, Map<Property, String>> propertiesForResource = new HashMap<>();

			Map<Property, String> generalProperties = new HashMap<>();
			generalProperties.put(Bisp.informationSourceId, GeneralConstants.TWITTER_SOURCE);
			generalProperties.put(Bisp.birdSpecies, hotspotDTO.getBirdSpecies());
			generalProperties.put(Bisp.observationDate, hotspotDTO.getObservationDate());
			if (hotspotDTO.getHowMany() != null) {
				generalProperties.put(Bisp.howMany, hotspotDTO.getHowMany());
			}

			Map<Property, String> locationProperties = new HashMap<>();
			locationProperties.put(Bisp.latitude, hotspotDTO.getLatitude());
			locationProperties.put(Bisp.longitude, hotspotDTO.getLongitude());

			Map<Property, String> tweetProperties = new HashMap<>();
			tweetProperties.put(Bisp.id, hotspotDTO.getTweetId());
			tweetProperties.put(Bisp.language, GeneralConstants.EN_LANGUAGE);
			tweetProperties.put(Bisp.text, hotspotDTO.getTweetMessage());

			Map<Property, String> userProperties = new HashMap<>();
			userProperties.put(FOAF.accountName, hotspotDTO.getUser().getScreenName());
			userProperties.put(FOAF.name, hotspotDTO.getUser().getName());
//			userProperties.put(FOAF., hotspotDTO.getUser().getId());
//			userProperties.put(FOAF. , hotspotDTO.getUser().getEmail());
//			userProperties.put(FOAF. , hotspotDTO.getUser().getLocation());

			hotspotResource = addProperties(generalProperties, hotspotResource);

//			hotspotResource.addProperty(Bisp.informationSourceId, GeneralConstants.TWITTER_SOURCE)
//					.addProperty(Bisp.birdSpecies, hotspotDTO.getBirdSpecies())
//					.addProperty(Bisp.howMany, hotspotDTO.getHowMany())
//					.addProperty(Bisp.observationDate, hotspotDTO.getHowMany()).addProperty(
//					Bisp.location, model.createResource("http://xmlns.com/bisp/location")
//							.addProperty(Bisp.latitude, hotspotDTO.getLatitude())
//							.addProperty(Bisp.longitude, hotspotDTO.getLongitude()))
//					.addProperty(Bisp.informationSourceId, "twitter")
//					.addProperty(Bisp.tweet,
//					model.createResource(RdfEnum.URI.getCode() + "tweet")
//							.addProperty(Bisp.id, hotspotDTO.getTweetId())
//							.addProperty(Bisp.author, "@SomeUSer")
//							.addProperty(Bisp.language, GeneralConstants.EN_LANGUAGE)
//							.addProperty(Bisp.text, hotspotDTO.getTweetMessage())
//							.addProperty(Bisp.link, "")
//							.addProperty(Bisp.user,
//			 						model.createResource(RdfEnum.URI.getCode() + "tweet#user")
//											.addProperty(FOAF.accountName, "Lester Daniel")
//											.addProperty(FOAF.name, "Lester Daniel")
//											.addProperty(FOAF.firstName, "Lester")
//											.addProperty(FOAF.lastName, "Daniel")
//											.addProperty(FOAF.gender, "M")));

			model.setNsPrefix("location", "http://xmlns.com/bisp/location#");
			model.setNsPrefix("hotspot", "http://xmlns.com/bisp/");
			model.setNsPrefix("tweet", "http://xmlns.com/bisp/tweet#");
			model.setNsPrefix("geo", "http://xmlns.com/bisp/geo#");
			model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
			model.write(System.out, "N-TRIPLES");
		}
	}

	private Resource addProperties(Map<Property, String> properties, Resource resource) {
//		resource.addProperty()
		return resource;
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
