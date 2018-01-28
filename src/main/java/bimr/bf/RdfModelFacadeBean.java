package bimr.bf;

import bimr.bfcl.RdfModelFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import bimr.util.GeneralConstants;
import bimr.util.RdfEnum;
import bimr.util.rdf.ontology.Bisp;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Startup
@Singleton
public class RdfModelFacadeBean implements RdfModelFacade {

	private Dataset dataset;

	@PostConstruct
	public void init() {
		dataset = TDBFactory.createDataset(RdfEnum.DIRECTORY_NAME.getCode());
	}

	@PreDestroy
	public void cleanup() {
		dataset.close();
	}

	@Override
	public void generateRdfModel(List<HotspotDTO> hotspots) {
		int id = 0;
		for (HotspotDTO hotspot : hotspots) {
			try {
				dataset.begin(ReadWrite.WRITE);

				Model model = dataset.getNamedModel("myHot" + id);

				createResources(model, hotspot, id);
				model.close();
				dataset.commit();
				id++;
			} finally {
				dataset.end();
			}
		}
	}

	@Override
	public void persistModel(Model model) {
		dataset.begin(ReadWrite.WRITE);
		model.commit();
		dataset.commit();
		dataset.end();
	}

	private Model getModel() {
		// open write transaction
		dataset.begin(ReadWrite.WRITE);
		Model model = dataset.getDefaultModel();
		return model;
	}

	private void createResources(Model model, HotspotDTO hotspotDTO, int id) {
		Resource hotspotResource = model.createResource(Bisp.getUri(RdfEnum.HOTSPOT.getCode() + id));

		if (hotspotDTO.getUser() != null) {
			Resource user = createUserResource(model, hotspotDTO);
			hotspotResource.addProperty(Bisp.user, user);
		}
		Resource observation = createObservationResource(model, hotspotDTO);
		hotspotResource.addProperty(Bisp.observation, observation);
	}

	private Resource createUserResource(Model model, HotspotDTO hotspotDTO) {
		Resource user = model.createResource(Bisp.getUri(RdfEnum.USER.getCode()));
		if (hotspotDTO.getUser().getScreenName() != null) {
			user.addProperty(VCARD.NICKNAME, hotspotDTO.getUser().getScreenName());
		}
		user.addProperty(VCARD.NAME, hotspotDTO.getUser().getName());
		user.addProperty(VCARD.UID, hotspotDTO.getUser().getId());
		user.addProperty(VCARD.EMAIL, hotspotDTO.getUser().getEmail());
		user.addProperty(VCARD.ADR, hotspotDTO.getUser().getLocation());
		user.addProperty(VCARD.GEO, hotspotDTO.getUser().isGeoEnabled());
		return user;
	}

	private Resource createObservationResource(Model model, HotspotDTO hotspotDTO) {
		Resource observation = model.createResource(Bisp.getUri(RdfEnum.OBSERVATION.getCode()));
		for (String species : hotspotDTO.getBirdSpecies()) {
			observation.addProperty(Bisp.birdSpecies, species);
		}
		if (hotspotDTO.getHowMany() != null) {
			observation.addProperty(Bisp.howMany, hotspotDTO.getHowMany());
		}
		observation.addProperty(Bisp.date, hotspotDTO.getObservationDate());
		observation.addProperty(Bisp.informationSourceId, GeneralConstants.TWITTER_SOURCE);

		Resource tweet = createTweetResource(model, hotspotDTO);
		Resource location = createLocationResource(model, hotspotDTO);

		observation.addProperty(Bisp.tweet, tweet);
		observation.addProperty(Bisp.location, location);
		return observation;
	}

	private Resource createLocationResource(Model model, HotspotDTO hotspotDTO) {
		Resource location = model.createResource(Bisp.getUri(RdfEnum.LOCATION.getCode()));
		if (hotspotDTO.getLatitude() != null) {
			location.addProperty(Bisp.latitude, hotspotDTO.getLatitude());
			location.addProperty(Bisp.longitude, hotspotDTO.getLongitude());
		}
		if (hotspotDTO.getLocationName() != null) {
			location.addProperty(Bisp.name, hotspotDTO.getLocationName());
		}
		if (hotspotDTO.getCountry() != null) {
			location.addProperty(Bisp.country, hotspotDTO.getCountry());
		}
		if (hotspotDTO.getState() != null) {
			location.addProperty(Bisp.state, hotspotDTO.getState());
		}
		return location;
	}

	private Resource createTweetResource(Model model, HotspotDTO hotspotDTO) {
		Resource tweet = model.createResource(Bisp.getUri(RdfEnum.TWEET.getCode()));
		tweet.addProperty(Bisp.tweetId, hotspotDTO.getTweetId());
		tweet.addProperty(Bisp.language, GeneralConstants.EN_LANGUAGE);
		tweet.addProperty(Bisp.text, hotspotDTO.getTweetMessage());
		if (hotspotDTO.getUser() != null) {
			tweet.addProperty(VCARD.UID, hotspotDTO.getUser().getId());
		}
		tweet.addProperty(Bisp.link, hotspotDTO.getLink());
		if (hotspotDTO.getAuthor() != null) {
			tweet.addProperty(Bisp.author, hotspotDTO.getAuthor());
		}
		return tweet;
	}

	private void writeRdfModelInFile(Model model) {
		replaceTagNames(model);
		model.write(System.out, RdfEnum.RDF_XML_FORMAT.getCode());
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(RdfEnum.FILENAME.getCode()), true));
			model.write(writer, RdfEnum.RDF_XML_FORMAT.getCode());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void replaceTagNames(Model model) {
		model.setNsPrefix("hotspot", Bisp.getUri(""));
	}

	public List<TweetDTO> mockTweets() {
		List<TweetDTO> migrationTweets = new ArrayList<>();

		TweetDTO tweetDTO11 = new TweetDTO();
		// #1.1
		tweetDTO11.setLatitude("38.44");
		tweetDTO11.setLongitude("-105.70");
		tweetDTO11.setUser(new TwitterUserDTO(){{setName("Arnold Sheppard");}});
		tweetDTO11.setTweetId(11L);
		tweetDTO11.setTweetMessage("Swainson hawk");
		tweetDTO11.setObservationDate(getDateFromString("2017-12-13"));
		migrationTweets.add(tweetDTO11);

		TweetDTO tweetDTO12 = new TweetDTO();
		// #1.2
		tweetDTO12.setLatitude("37.26");
		tweetDTO12.setLongitude("-105.60");
		tweetDTO12.setUser(new TwitterUserDTO(){{setName("John Wick");}});
		tweetDTO12.setTweetId(12L);
		tweetDTO12.setTweetMessage("Swainson hawk");
		tweetDTO12.setObservationDate(getDateFromString("2017-12-13"));
		migrationTweets.add(tweetDTO12);

		TweetDTO tweetDTO13 = new TweetDTO();
		// #1.3
		tweetDTO13.setLatitude("36.31");
		tweetDTO13.setLongitude("-108.49");
		tweetDTO13.setUser(new TwitterUserDTO(){{setName("Tobey Marshall");}});
		tweetDTO13.setTweetId(13L);
		tweetDTO13.setTweetMessage("Swainson hawk");
		tweetDTO13.setObservationDate(getDateFromString("2017-12-14"));
		migrationTweets.add(tweetDTO13);

		TweetDTO tweetDTO14 = new TweetDTO();
		// #1.4
		tweetDTO14.setLatitude("32.60");
		tweetDTO14.setLongitude("-110.33");
		tweetDTO14.setUser(new TwitterUserDTO(){{setName("Dino Brewster");}});
		tweetDTO14.setTweetId(14L);
		tweetDTO14.setTweetMessage("Swainson hawk");
		tweetDTO14.setObservationDate(getDateFromString("2017-12-15"));
		migrationTweets.add(tweetDTO14);

		TweetDTO tweetDTO15 = new TweetDTO();
		// #1.5
		tweetDTO15.setLatitude("30.45");
		tweetDTO15.setLongitude("-109.14");
		tweetDTO15.setUser(new TwitterUserDTO(){{setName("Tego Calderon");}});
		tweetDTO15.setTweetId(15L);
		tweetDTO15.setTweetMessage("Swainson hawk");
		tweetDTO15.setObservationDate(getDateFromString("2017-12-16"));
		migrationTweets.add(tweetDTO15);

		TweetDTO tweetDTO16 = new TweetDTO();
		// #1.6
		tweetDTO16.setLatitude("28.67");
		tweetDTO16.setLongitude("-107.78");
		tweetDTO16.setUser(new TwitterUserDTO(){{setName("Arturo Braga");}});
		tweetDTO16.setTweetId(16L);
		tweetDTO16.setTweetMessage("Swainson hawk");
		tweetDTO16.setObservationDate(getDateFromString("2017-12-17"));
		migrationTweets.add(tweetDTO16);

		TweetDTO tweetDTO17 = new TweetDTO();
		// #1.7
		tweetDTO17.setLatitude("26.76");
		tweetDTO17.setLongitude("-109.05");
		tweetDTO17.setUser(new TwitterUserDTO(){{setName("Roberto Soldado");}});
		tweetDTO17.setTweetId(17L);
		tweetDTO17.setTweetMessage("Swainson hawk");
		tweetDTO17.setObservationDate(getDateFromString("2017-12-17"));
		migrationTweets.add(tweetDTO17);

		TweetDTO tweetDTO18 = new TweetDTO();
		// #1.8
		tweetDTO18.setLatitude("25.80");
		tweetDTO18.setLongitude("-109.09");
		tweetDTO18.setUser(new TwitterUserDTO(){{setName("Julio Fonseca");}});
		tweetDTO18.setTweetId(18L);
		tweetDTO18.setTweetMessage("Swainson hawk");
		tweetDTO18.setObservationDate(getDateFromString("2017-12-18"));
		migrationTweets.add(tweetDTO18);

		TweetDTO tweetDTO19 = new TweetDTO();
		// #1.9
		tweetDTO19.setLatitude("24.53");
		tweetDTO19.setLongitude("-111.30");
		tweetDTO19.setUser(new TwitterUserDTO(){{setName("Hernan Reyes");}});
		tweetDTO19.setTweetId(19L);
		tweetDTO19.setTweetMessage("Swainson hawk");
		tweetDTO19.setObservationDate(getDateFromString("2017-12-20"));
		migrationTweets.add(tweetDTO19);

		TweetDTO tweetDTO110 = new TweetDTO();
		// #1.10
		tweetDTO110.setLatitude("23.52");
		tweetDTO110.setLongitude("-109.90");
		tweetDTO110.setUser(new TwitterUserDTO(){{setName("Elena Neves");}});
		tweetDTO110.setTweetId(110L);
		tweetDTO110.setTweetMessage("Swainson hawk");
		tweetDTO110.setObservationDate(getDateFromString("2017-12-21"));
		migrationTweets.add(tweetDTO110);


		TweetDTO tweetDTO21 = new TweetDTO();
		// #2.1
		tweetDTO21.setLatitude("47.27");
		tweetDTO21.setLongitude("-74.73");
		tweetDTO21.setUser(new TwitterUserDTO(){{setName("Chris Smalling");}});
		tweetDTO21.setTweetId(21L);
		tweetDTO21.setTweetMessage("Osprey");
		tweetDTO21.setObservationDate(getDateFromString("2017-10-24"));
		migrationTweets.add(tweetDTO21);

		TweetDTO tweetDTO22 = new TweetDTO();
		// #2.2
		tweetDTO22.setLatitude("45.74");
		tweetDTO22.setLongitude("-75.76");
		tweetDTO22.setUser(new TwitterUserDTO(){{setName("Ross Barkley");}});
		tweetDTO22.setTweetId(22L);
		tweetDTO22.setTweetMessage("Osprey");
		tweetDTO22.setObservationDate(getDateFromString("2017-10-25"));
		migrationTweets.add(tweetDTO22);

		TweetDTO tweetDTO23 = new TweetDTO();
		// #2.3
		tweetDTO23.setLatitude("43.85");
		tweetDTO23.setLongitude("-75.25");
		tweetDTO23.setUser(new TwitterUserDTO(){{setName("Chris Bridges");}});
		tweetDTO23.setTweetId(23L);
		tweetDTO23.setTweetMessage("Osprey");
		tweetDTO23.setObservationDate(getDateFromString("2017-10-26"));
		migrationTweets.add(tweetDTO23);

		TweetDTO tweetDTO24 = new TweetDTO();
		// #2.4
		tweetDTO24.setLatitude("42.40");
		tweetDTO24.setLongitude("-74.58");
		tweetDTO24.setUser(new TwitterUserDTO(){{setName("Jim Waters");}});
		tweetDTO24.setTweetId(24L);
		tweetDTO24.setTweetMessage("Osprey");
		tweetDTO24.setObservationDate(getDateFromString("2017-10-26"));
		migrationTweets.add(tweetDTO24);

		TweetDTO tweetDTO25 = new TweetDTO();
		// #2.5
		tweetDTO25.setLatitude("40.29");
		tweetDTO25.setLongitude("-77.24");
		tweetDTO25.setUser(new TwitterUserDTO(){{setName("Gary Silverton");}});
		tweetDTO25.setTweetId(25L);
		tweetDTO25.setTweetMessage("Osprey");
		tweetDTO25.setObservationDate(getDateFromString("2017-10-27"));
		migrationTweets.add(tweetDTO25);

		TweetDTO tweetDTO26 = new TweetDTO();
		// #2.6
		tweetDTO26.setLatitude("38.37");
		tweetDTO26.setLongitude("-77.81");
		tweetDTO26.setUser(new TwitterUserDTO(){{setName("Jamie Vardy");}});
		tweetDTO26.setTweetId(26L);
		tweetDTO26.setTweetMessage("Osprey");
		tweetDTO26.setObservationDate(getDateFromString("2017-10-29"));
		migrationTweets.add(tweetDTO26);

		TweetDTO tweetDTO27 = new TweetDTO();
		// #2.7
		tweetDTO27.setLatitude("36.06");
		tweetDTO27.setLongitude("-77.07");
		tweetDTO27.setUser(new TwitterUserDTO(){{setName("Owen Hargreaves");}});
		tweetDTO27.setTweetId(27L);
		tweetDTO27.setTweetMessage("Osprey");
		tweetDTO27.setObservationDate(getDateFromString("2017-10-30"));
		migrationTweets.add(tweetDTO27);

		TweetDTO tweetDTO28 = new TweetDTO();
		// #2.8
		tweetDTO28.setLatitude("32.95");
		tweetDTO28.setLongitude("-80.05");
		tweetDTO28.setUser(new TwitterUserDTO(){{setName("Andy Anderson");}});
		tweetDTO28.setTweetId(28L);
		tweetDTO28.setTweetMessage("Osprey");
		tweetDTO28.setObservationDate(getDateFromString("2017-10-31"));
		migrationTweets.add(tweetDTO28);

		TweetDTO tweetDTO29 = new TweetDTO();
		// #2.9
		tweetDTO29.setLatitude("30.81");
		tweetDTO29.setLongitude("-83.50");
		tweetDTO29.setUser(new TwitterUserDTO(){{setName("David Duchovny");}});
		tweetDTO29.setTweetId(29L);
		tweetDTO29.setTweetMessage("Osprey");
		tweetDTO29.setObservationDate(getDateFromString("2017-11-01"));
		migrationTweets.add(tweetDTO29);

		TweetDTO tweetDTO210 = new TweetDTO();
		// #2.10
		tweetDTO210.setLatitude("27.51");
		tweetDTO210.setLongitude("-82.47");
		tweetDTO210.setUser(new TwitterUserDTO(){{setName("Cody Walker");}});
		tweetDTO210.setTweetId(210L);
		tweetDTO210.setTweetMessage("Osprey");
		tweetDTO210.setObservationDate(getDateFromString("2017-11-02"));
		migrationTweets.add(tweetDTO210);


		TweetDTO tweetDTO31 = new TweetDTO();
		// #3.1
		tweetDTO31.setLatitude("58.68");
		tweetDTO31.setLongitude("7.17");
		tweetDTO31.setUser(new TwitterUserDTO(){{setName("Ankjell Karlof");}});
		tweetDTO31.setTweetId(31L);
		tweetDTO31.setTweetMessage("Bank Swallow");
		tweetDTO31.setObservationDate(getDateFromString("2018-01-03"));
		migrationTweets.add(tweetDTO31);

		TweetDTO tweetDTO32 = new TweetDTO();
		// #3.2
		tweetDTO32.setLatitude("57.52");
		tweetDTO32.setLongitude("12.81");
		tweetDTO32.setUser(new TwitterUserDTO(){{setName("Markus Berg");}});
		tweetDTO32.setTweetId(32L);
		tweetDTO32.setTweetMessage("Bank Swallow");
		tweetDTO32.setObservationDate(getDateFromString("2018-01-05"));
		migrationTweets.add(tweetDTO32);

		TweetDTO tweetDTO33 = new TweetDTO();
		// #3.3
		tweetDTO33.setLatitude("55.14");
		tweetDTO33.setLongitude("11.87");
		tweetDTO33.setUser(new TwitterUserDTO(){{setName("Christian Eriksen");}});
		tweetDTO33.setTweetId(33L);
		tweetDTO33.setTweetMessage("Bank Swallow");
		tweetDTO33.setObservationDate(getDateFromString("2018-01-06"));
		migrationTweets.add(tweetDTO33);

		TweetDTO tweetDTO34 = new TweetDTO();
		// #3.4
		tweetDTO34.setLatitude("52.87");
		tweetDTO34.setLongitude("8.11");
		tweetDTO34.setUser(new TwitterUserDTO(){{setName("Robert Adler");}});
		tweetDTO34.setTweetId(34L);
		tweetDTO34.setTweetMessage("Bank Swallow");
		tweetDTO34.setObservationDate(getDateFromString("2018-01-08"));
		migrationTweets.add(tweetDTO34);

		TweetDTO tweetDTO35 = new TweetDTO();
		// #3.5
		tweetDTO35.setLatitude("50.87");
		tweetDTO35.setLongitude("-5.91");
		tweetDTO35.setUser(new TwitterUserDTO(){{setName("Lasse Schone");}});
		tweetDTO35.setTweetId(35L);
		tweetDTO35.setTweetMessage("Bank Swallow");
		tweetDTO35.setObservationDate(getDateFromString("2018-01-09"));
		migrationTweets.add(tweetDTO35);

		TweetDTO tweetDTO36 = new TweetDTO();
		// #3.6
		tweetDTO36.setLatitude("50.08");
		tweetDTO36.setLongitude("5.08");
		tweetDTO36.setUser(new TwitterUserDTO(){{setName("Axel Witsel");}});
		tweetDTO36.setTweetId(36L);
		tweetDTO36.setTweetMessage("Bank Swallow");
		tweetDTO36.setObservationDate(getDateFromString("2018-01-09"));
		migrationTweets.add(tweetDTO36);

		TweetDTO tweetDTO37 = new TweetDTO();
		// #3.7
		tweetDTO37.setLatitude("48.44");
		tweetDTO37.setLongitude("4.76");
		tweetDTO37.setUser(new TwitterUserDTO(){{setName("Alexandre Lacazette");}});
		tweetDTO37.setTweetId(37L);
		tweetDTO37.setTweetMessage("Bank Swallow");
		tweetDTO37.setObservationDate(getDateFromString("2018-01-11"));
		migrationTweets.add(tweetDTO37);

		TweetDTO tweetDTO38 = new TweetDTO();
		// #3.8
		tweetDTO38.setLatitude("45.44");
		tweetDTO38.setLongitude("0.48");
		tweetDTO38.setUser(new TwitterUserDTO(){{setName("Hugo Lloris");}});
		tweetDTO38.setTweetId(38L);
		tweetDTO38.setTweetMessage("Bank Swallow");
		tweetDTO38.setObservationDate(getDateFromString("2018-01-12"));
		migrationTweets.add(tweetDTO38);

		TweetDTO tweetDTO39 = new TweetDTO();
		// #3.9
		tweetDTO39.setLatitude("42.66");
		tweetDTO39.setLongitude("-4.53");
		tweetDTO39.setUser(new TwitterUserDTO(){{setName("David Silva");}});
		tweetDTO39.setTweetId(39L);
		tweetDTO39.setTweetMessage("Bank Swallow");
		tweetDTO39.setObservationDate(getDateFromString("2018-01-14"));
		migrationTweets.add(tweetDTO39);

		TweetDTO tweetDTO310 = new TweetDTO();
		// #3.10
		tweetDTO310.setLatitude("39.68");
		tweetDTO310.setLongitude("-8.81");
		tweetDTO310.setUser(new TwitterUserDTO(){{setName("Andre Gomes");}});
		tweetDTO310.setTweetId(310L);
		tweetDTO310.setTweetMessage("Bank Swallow");
		tweetDTO310.setObservationDate(getDateFromString("2018-01-16"));
		migrationTweets.add(tweetDTO310);


		TweetDTO tweetDTO41 = new TweetDTO();
		// #4.1
		tweetDTO41.setLatitude("59.54");
		tweetDTO41.setLongitude("39.36");
		tweetDTO41.setUser(new TwitterUserDTO(){{setName("Andrey Kokorin");}});
		tweetDTO41.setTweetId(41L);
		tweetDTO41.setTweetMessage("Sandhill Crane");
		tweetDTO41.setObservationDate(getDateFromString("2018-01-06"));
		migrationTweets.add(tweetDTO41);

		TweetDTO tweetDTO42 = new TweetDTO();
		// #4.2
		tweetDTO42.setLatitude("56.21");
		tweetDTO42.setLongitude("34.13");
		tweetDTO42.setUser(new TwitterUserDTO(){{setName("Alan Dzagoev");}});
		tweetDTO42.setTweetId(42L);
		tweetDTO42.setTweetMessage("Sandhill Crane");
		tweetDTO42.setObservationDate(getDateFromString("2018-01-07"));
		migrationTweets.add(tweetDTO42);

		TweetDTO tweetDTO43 = new TweetDTO();
		// #4.3
		tweetDTO43.setLatitude("53.44");
		tweetDTO43.setLongitude("31.10");
		tweetDTO43.setUser(new TwitterUserDTO(){{setName("Sergei Gurenko");}});
		tweetDTO43.setTweetId(43L);
		tweetDTO43.setTweetMessage("Sandhill Crane");
		tweetDTO43.setObservationDate(getDateFromString("2018-01-08"));
		migrationTweets.add(tweetDTO43);

		TweetDTO tweetDTO44 = new TweetDTO();
		// #4.4
		tweetDTO44.setLatitude("50.54");
		tweetDTO44.setLongitude("27.76");
		tweetDTO44.setUser(new TwitterUserDTO(){{setName("Andryi Shevchenko");}});
		tweetDTO44.setTweetId(44L);
		tweetDTO44.setTweetMessage("Sandhill Crane");
		tweetDTO44.setObservationDate(getDateFromString("2018-01-09"));
		migrationTweets.add(tweetDTO44);

		TweetDTO tweetDTO45 = new TweetDTO();
		// #4.5
		tweetDTO45.setLatitude("47.20");
		tweetDTO45.setLongitude("27.28");
		tweetDTO45.setUser(new TwitterUserDTO(){{setName("Alexandru Mocanu");}});
		tweetDTO45.setTweetId(45L);
		tweetDTO45.setTweetMessage("Sandhill Crane");
		tweetDTO45.setObservationDate(getDateFromString("2018-01-11"));
		migrationTweets.add(tweetDTO45);

		TweetDTO tweetDTO46 = new TweetDTO();
		// #4.6
		tweetDTO46.setLatitude("44.40");
		tweetDTO46.setLongitude("26.23");
		tweetDTO46.setUser(new TwitterUserDTO(){{setName("Ioan Petrescu");}});
		tweetDTO46.setTweetId(46L);
		tweetDTO46.setTweetMessage("Sandhill Crane");
		tweetDTO46.setObservationDate(getDateFromString("2018-01-11"));
		migrationTweets.add(tweetDTO46);

		TweetDTO tweetDTO47 = new TweetDTO();
		// #4.7
		tweetDTO47.setLatitude("37.80");
		tweetDTO47.setLongitude("22.15");
		tweetDTO47.setUser(new TwitterUserDTO(){{setName("Sokratis Papastathopoulos");}});
		tweetDTO47.setTweetId(47L);
		tweetDTO47.setTweetMessage("Sandhill Crane");
		tweetDTO47.setObservationDate(getDateFromString("2018-01-14"));
		migrationTweets.add(tweetDTO47);

		TweetDTO tweetDTO48 = new TweetDTO();
		// #4.8
		tweetDTO48.setLatitude("30.05");
		tweetDTO48.setLongitude("26.01");
		tweetDTO48.setUser(new TwitterUserDTO(){{setName("Asim Ramses");}});
		tweetDTO48.setTweetId(48L);
		tweetDTO48.setTweetMessage("Sandhill Crane");
		tweetDTO48.setObservationDate(getDateFromString("2018-01-16"));
		migrationTweets.add(tweetDTO48);

		TweetDTO tweetDTO49 = new TweetDTO();
		// #4.9
		tweetDTO49.setLatitude("20.75");
		tweetDTO49.setLongitude("35.21");
		tweetDTO49.setUser(new TwitterUserDTO(){{setName("Bakri Bashir");}});
		tweetDTO49.setTweetId(49L);
		tweetDTO49.setTweetMessage("Sandhill Crane");
		tweetDTO49.setObservationDate(getDateFromString("2018-01-17"));
		migrationTweets.add(tweetDTO49);

		TweetDTO tweetDTO410 = new TweetDTO();
		// #4.10
		tweetDTO410.setLatitude("13.15");
		tweetDTO410.setLongitude("40.43");
		tweetDTO410.setUser(new TwitterUserDTO(){{setName("Jemal Tassew");}});
		tweetDTO410.setTweetId(410L);
		tweetDTO410.setTweetMessage("Sandhill Crane");
		tweetDTO410.setObservationDate(getDateFromString("2018-01-19"));
		migrationTweets.add(tweetDTO410);

		return migrationTweets;
	}

	private Date getDateFromString(String sDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private List<HotspotDTO> mockHotspots() {
		List<HotspotDTO> migrationTweets = new ArrayList<>();
		HotspotDTO hotspotDTO = new HotspotDTO();

		// #1.1
		hotspotDTO.setLatitude("38.44");
		hotspotDTO.setLongitude("-105.70");
		hotspotDTO.setHowMany("18");
		hotspotDTO.setAuthor("Phil Jones");
		hotspotDTO.setState("Colorado");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-13");
		migrationTweets.add(hotspotDTO);

		// #1.2
		hotspotDTO.setLatitude("37.26");
		hotspotDTO.setLongitude("-105.60");
		hotspotDTO.setHowMany("18");
		hotspotDTO.setAuthor("John Wick");
		hotspotDTO.setState("Colorado");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-13");
		migrationTweets.add(hotspotDTO);

		// #1.3
		hotspotDTO.setLatitude("36.31");
		hotspotDTO.setLongitude("-108.49");
		hotspotDTO.setHowMany("22");
		hotspotDTO.setAuthor("Tobey Marshall");
		hotspotDTO.setState("New Mexico");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-14");
		migrationTweets.add(hotspotDTO);

		// #1.4
		hotspotDTO.setLatitude("32.60");
		hotspotDTO.setLongitude("-110.33");
		hotspotDTO.setHowMany("24");
		hotspotDTO.setAuthor("Dino Brewster");
		hotspotDTO.setState("Arizona");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-15");
		migrationTweets.add(hotspotDTO);

		// #1.5
		hotspotDTO.setLatitude("30.45");
		hotspotDTO.setLongitude("-109.14");
		hotspotDTO.setHowMany("25");
		hotspotDTO.setAuthor("Tego Calderon");
		hotspotDTO.setState("Sonora");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-16");
		migrationTweets.add(hotspotDTO);

		// #1.6
		hotspotDTO.setLatitude("28.67");
		hotspotDTO.setLongitude("-107.78");
		hotspotDTO.setHowMany("23");
		hotspotDTO.setAuthor("Arturo Braga");
		hotspotDTO.setState("Chihuahua");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-17");
		migrationTweets.add(hotspotDTO);

		// #1.7
		hotspotDTO.setLatitude("26.76");
		hotspotDTO.setLongitude("-109.05");
		hotspotDTO.setHowMany("23");
		hotspotDTO.setAuthor("Roberto Soldado");
		hotspotDTO.setState("Sonora");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-17");
		migrationTweets.add(hotspotDTO);

		// #1.8
		hotspotDTO.setLatitude("25.80");
		hotspotDTO.setLongitude("-109.09");
		hotspotDTO.setHowMany("21");
		hotspotDTO.setAuthor("Julio Fonseca");
		hotspotDTO.setState("Sinaloa");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-18");
		migrationTweets.add(hotspotDTO);

		// #1.9
		hotspotDTO.setLatitude("24.53");
		hotspotDTO.setLongitude("-111.30");
		hotspotDTO.setHowMany("20");
		hotspotDTO.setAuthor("Hernan Reyes");
		hotspotDTO.setState("Baja California Sur");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-20");
		migrationTweets.add(hotspotDTO);

		// #1.10
		hotspotDTO.setLatitude("23.52");
		hotspotDTO.setLongitude("-109.90");
		hotspotDTO.setHowMany("20");
		hotspotDTO.setAuthor("Elena Neves");
		hotspotDTO.setState("Baja California Sur");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Swainson hawk")));
		hotspotDTO.setObservationDate("2017-12-21");
		migrationTweets.add(hotspotDTO);


		// #2.1
		hotspotDTO.setLatitude("47.27");
		hotspotDTO.setLongitude("-74.73");
		hotspotDTO.setHowMany("10");
		hotspotDTO.setAuthor("Chris Smalling");
		hotspotDTO.setState("Quebec");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-24");
		migrationTweets.add(hotspotDTO);

		// #2.2
		hotspotDTO.setLatitude("45.74");
		hotspotDTO.setLongitude("-75.76");
		hotspotDTO.setHowMany("12");
		hotspotDTO.setAuthor("Ross Barkley");
		hotspotDTO.setState("Quebec");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-25");
		migrationTweets.add(hotspotDTO);

		// #2.3
		hotspotDTO.setLatitude("43.85");
		hotspotDTO.setLongitude("-75.25");
		hotspotDTO.setHowMany("11");
		hotspotDTO.setAuthor("Chris Bridges");
		hotspotDTO.setState("New York");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-26");
		migrationTweets.add(hotspotDTO);

		// #2.4
		hotspotDTO.setLatitude("42.40");
		hotspotDTO.setLongitude("-74.58");
		hotspotDTO.setHowMany("14");
		hotspotDTO.setAuthor("Jim Waters");
		hotspotDTO.setState("New York");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-26");
		migrationTweets.add(hotspotDTO);

		// #2.5
		hotspotDTO.setLatitude("40.29");
		hotspotDTO.setLongitude("-77.24");
		hotspotDTO.setHowMany("12");
		hotspotDTO.setAuthor("Gary Silverton");
		hotspotDTO.setState("Pennsylvania");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-27");
		migrationTweets.add(hotspotDTO);

		// #2.6
		hotspotDTO.setLatitude("38.37");
		hotspotDTO.setLongitude("-77.81");
		hotspotDTO.setHowMany("15");
		hotspotDTO.setAuthor("Jamie Vardy");
		hotspotDTO.setState("Virginia");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-29");
		migrationTweets.add(hotspotDTO);

		// #2.7
		hotspotDTO.setLatitude("36.06");
		hotspotDTO.setLongitude("-77.07");
		hotspotDTO.setHowMany("10");
		hotspotDTO.setAuthor("Owen Hargreaves");
		hotspotDTO.setState("North Carolina");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-30");
		migrationTweets.add(hotspotDTO);

		// #2.8
		hotspotDTO.setLatitude("32.95");
		hotspotDTO.setLongitude("-80.05");
		hotspotDTO.setHowMany("17");
		hotspotDTO.setAuthor("Andy Anderson");
		hotspotDTO.setState("South Carolina");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-10-31");
		migrationTweets.add(hotspotDTO);

		// #2.9
		hotspotDTO.setLatitude("30.81");
		hotspotDTO.setLongitude("-83.50");
		hotspotDTO.setHowMany("16");
		hotspotDTO.setAuthor("David Duchovny");
		hotspotDTO.setState("Georgia");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-11-01");
		migrationTweets.add(hotspotDTO);

		// #2.10
		hotspotDTO.setLatitude("27.51");
		hotspotDTO.setLongitude("-82.47");
		hotspotDTO.setHowMany("16");
		hotspotDTO.setAuthor("Cody Walker");
		hotspotDTO.setState("Florida");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Osprey")));
		hotspotDTO.setObservationDate("2017-11-02");
		migrationTweets.add(hotspotDTO);


		// #3.1
		hotspotDTO.setLatitude("58.68");
		hotspotDTO.setLongitude("7.17");
		hotspotDTO.setHowMany("28");
		hotspotDTO.setAuthor("Ankjell Karlof");
		hotspotDTO.setCountry("Norway");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-03");
		migrationTweets.add(hotspotDTO);

		// #3.2
		hotspotDTO.setLatitude("57.52");
		hotspotDTO.setLongitude("12.81");
		hotspotDTO.setHowMany("27");
		hotspotDTO.setAuthor("Markus Berg");
		hotspotDTO.setCountry("Sweden");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-05");
		migrationTweets.add(hotspotDTO);

		// #3.3
		hotspotDTO.setLatitude("55.14");
		hotspotDTO.setLongitude("11.87");
		hotspotDTO.setHowMany("28");
		hotspotDTO.setAuthor("Christian Eriksen");
		hotspotDTO.setCountry("Denmark");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-06");
		migrationTweets.add(hotspotDTO);

		// #3.4
		hotspotDTO.setLatitude("52.87");
		hotspotDTO.setLongitude("8.11");
		hotspotDTO.setHowMany("29");
		hotspotDTO.setAuthor("Robert Adler");
		hotspotDTO.setCountry("Germany");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-08");
		migrationTweets.add(hotspotDTO);

		// #3.5
		hotspotDTO.setLatitude("50.87");
		hotspotDTO.setLongitude("-5.91");
		hotspotDTO.setHowMany("26");
		hotspotDTO.setAuthor("Lasse Schone");
		hotspotDTO.setCountry("Netherlands");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO);

		// #3.6
		hotspotDTO.setLatitude("50.08");
		hotspotDTO.setLongitude("5.08");
		hotspotDTO.setHowMany("26");
		hotspotDTO.setAuthor("Axel Witsel");
		hotspotDTO.setCountry("Belgium");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO);

		// #3.7
		hotspotDTO.setLatitude("48.44");
		hotspotDTO.setLongitude("4.76");
		hotspotDTO.setHowMany("24");
		hotspotDTO.setAuthor("Alexandre Lacazette");
		hotspotDTO.setCountry("France");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO);

		// #3.8
		hotspotDTO.setLatitude("45.44");
		hotspotDTO.setLongitude("0.48");
		hotspotDTO.setHowMany("25");
		hotspotDTO.setAuthor("Hugo Lloris");
		hotspotDTO.setCountry("France");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-12");
		migrationTweets.add(hotspotDTO);

		// #3.9
		hotspotDTO.setLatitude("42.66");
		hotspotDTO.setLongitude("-4.53");
		hotspotDTO.setHowMany("25");
		hotspotDTO.setAuthor("David Silva");
		hotspotDTO.setCountry("Spain");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-14");
		migrationTweets.add(hotspotDTO);

		// #3.10
		hotspotDTO.setLatitude("39.68");
		hotspotDTO.setLongitude("-8.81");
		hotspotDTO.setHowMany("22");
		hotspotDTO.setAuthor("Andre Gomes");
		hotspotDTO.setCountry("Portugal");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Bank Swallow")));
		hotspotDTO.setObservationDate("2018-01-16");
		migrationTweets.add(hotspotDTO);


		// #4.1
		hotspotDTO.setLatitude("59.54");
		hotspotDTO.setLongitude("39.36");
		hotspotDTO.setHowMany("25");
		hotspotDTO.setAuthor("Andrey Kokorin");
		hotspotDTO.setCountry("Russia");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-06");
		migrationTweets.add(hotspotDTO);

		// #4.2
		hotspotDTO.setLatitude("56.21");
		hotspotDTO.setLongitude("34.13");
		hotspotDTO.setHowMany("26");
		hotspotDTO.setAuthor("Alan Dzagoev");
		hotspotDTO.setCountry("Russia");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-07");
		migrationTweets.add(hotspotDTO);

		// #4.3
		hotspotDTO.setLatitude("53.44");
		hotspotDTO.setLongitude("31.10");
		hotspotDTO.setHowMany("30");
		hotspotDTO.setAuthor("Sergei Gurenko");
		hotspotDTO.setCountry("Belarus");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-08");
		migrationTweets.add(hotspotDTO);

		// #4.4
		hotspotDTO.setLatitude("50.54");
		hotspotDTO.setLongitude("27.76");
		hotspotDTO.setHowMany("32");
		hotspotDTO.setAuthor("Andryi Shevchenko");
		hotspotDTO.setCountry("Ukraine");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO);

		// #4.5
		hotspotDTO.setLatitude("47.20");
		hotspotDTO.setLongitude("27.28");
		hotspotDTO.setHowMany("32");
		hotspotDTO.setAuthor("Alexandru Mocanu");
		hotspotDTO.setCountry("Romania");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO);

		// #4.6
		hotspotDTO.setLatitude("44.40");
		hotspotDTO.setLongitude("26.23");
		hotspotDTO.setHowMany("32");
		hotspotDTO.setAuthor("Ioan Petrescu");
		hotspotDTO.setCountry("Romania");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO);

		// #4.7
		hotspotDTO.setLatitude("37.80");
		hotspotDTO.setLongitude("22.15");
		hotspotDTO.setHowMany("35");
		hotspotDTO.setAuthor("Sokratis Papastathopoulos");
		hotspotDTO.setCountry("Greece");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-14");
		migrationTweets.add(hotspotDTO);

		// #4.8
		hotspotDTO.setLatitude("30.05");
		hotspotDTO.setLongitude("26.01");
		hotspotDTO.setHowMany("33");
		hotspotDTO.setAuthor("Asim Ramses");
		hotspotDTO.setCountry("Egypt");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-16");
		migrationTweets.add(hotspotDTO);

		// #4.9
		hotspotDTO.setLatitude("20.75");
		hotspotDTO.setLongitude("35.21");
		hotspotDTO.setHowMany("28");
		hotspotDTO.setAuthor("Bakri Bashir");
		hotspotDTO.setCountry("Sudan");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-17");
		migrationTweets.add(hotspotDTO);

		// #4.10
		hotspotDTO.setLatitude("13.15");
		hotspotDTO.setLongitude("40.43");
		hotspotDTO.setHowMany("24");
		hotspotDTO.setAuthor("Jemal Tassew");
		hotspotDTO.setCountry("Ethiopia");
		hotspotDTO.setBirdSpecies(new ArrayList<>(Collections.singletonList("Sandhill crane")));
		hotspotDTO.setObservationDate("2018-01-19");
		migrationTweets.add(hotspotDTO);

		return migrationTweets;
	}
}