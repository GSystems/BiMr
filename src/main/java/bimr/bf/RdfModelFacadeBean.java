package bimr.bf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

import bimr.bfcl.RdfModelFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TweetDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import bimr.util.GeneralConstants;
import bimr.util.RdfEnum;
import bimr.util.rdf.ontology.Bisp;

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
		for (HotspotDTO hotspotDTO : hotspots) {
			Model model = dataset.getDefaultModel();
			createResources(model, hotspotDTO, id);
			id++;
			writeRdfModelInFile(model);
			persistModel(model);
			model.close();
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
		Resource hotspotResource = model.createResource(RdfEnum.BASE_URI.getCode() + "hotspot" + id);

		if (hotspotDTO.getUser() != null) {
			Resource user = createUserResource(model, hotspotDTO);
			hotspotResource.addProperty(Bisp.user, user);
		}
		Resource observation = createObservationResource(model, hotspotDTO);
		hotspotResource.addProperty(Bisp.observation, observation);
	}

	private Resource createUserResource(Model model, HotspotDTO hotspotDTO) {
		Resource user = model.createResource(RdfEnum.USER_URI.getCode());
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
		Resource observation = model.createResource(RdfEnum.OBSERVATION_URI.getCode());
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
		Resource location = model.createResource(RdfEnum.LOCATION_URI.getCode());
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
		Resource tweet = model.createResource(RdfEnum.TWEET_URI.getCode());
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

	public List<HotspotDTO> mockMigrationDataHotspot() {
		List<HotspotDTO> migrationTweets = new ArrayList<>();
		
		HotspotDTO hotspotDTO11 = new HotspotDTO();
		// #1.1
		hotspotDTO11.setLatitude("38.44");
		hotspotDTO11.setLongitude("-105.70");
		hotspotDTO11.setHowMany("18");
		hotspotDTO11.setAuthor("Phil Jones");
		hotspotDTO11.setState("Colorado");
		hotspotDTO11.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO11.setObservationDate("2017-12-13");
		migrationTweets.add(hotspotDTO11);
		
		HotspotDTO hotspotDTO12 = new HotspotDTO();
		// #1.2
		hotspotDTO12.setLatitude("37.26");
		hotspotDTO12.setLongitude("-105.60");
		hotspotDTO12.setHowMany("18");
		hotspotDTO12.setAuthor("John Wick");
		hotspotDTO12.setState("Colorado");
		hotspotDTO12.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO12.setObservationDate("2017-12-13");
		migrationTweets.add(hotspotDTO12);
		
		HotspotDTO hotspotDTO13 = new HotspotDTO();
		// #1.3
		hotspotDTO13.setLatitude("36.31");
		hotspotDTO13.setLongitude("-108.49");
		hotspotDTO13.setHowMany("22");
		hotspotDTO13.setAuthor("Tobey Marshall");
		hotspotDTO13.setState("New Mexico");
		hotspotDTO13.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO13.setObservationDate("2017-12-14");
		migrationTweets.add(hotspotDTO13);
		
		HotspotDTO hotspotDTO14 = new HotspotDTO();
		// #1.4
		hotspotDTO14.setLatitude("32.60");
		hotspotDTO14.setLongitude("-110.33");
		hotspotDTO14.setHowMany("24");
		hotspotDTO14.setAuthor("Dino Brewster");
		hotspotDTO14.setState("Arizona");
		hotspotDTO14.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO14.setObservationDate("2017-12-15");
		migrationTweets.add(hotspotDTO14);

		HotspotDTO hotspotDTO15 = new HotspotDTO();
		// #1.5
		hotspotDTO15.setLatitude("30.45");
		hotspotDTO15.setLongitude("-109.14");
		hotspotDTO15.setHowMany("25");
		hotspotDTO15.setAuthor("Tego Calderon");
		hotspotDTO15.setState("Sonora");
		hotspotDTO15.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO15.setObservationDate("2017-12-16");
		migrationTweets.add(hotspotDTO15);

		HotspotDTO hotspotDTO16 = new HotspotDTO();
		// #1.6
		hotspotDTO16.setLatitude("28.67");
		hotspotDTO16.setLongitude("-107.78");
		hotspotDTO16.setHowMany("23");
		hotspotDTO16.setAuthor("Arturo Braga");
		hotspotDTO16.setState("Chihuahua");
		hotspotDTO16.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO16.setObservationDate("2017-12-17");
		migrationTweets.add(hotspotDTO16);

		HotspotDTO hotspotDTO17 = new HotspotDTO();
		// #1.7
		hotspotDTO17.setLatitude("26.76");
		hotspotDTO17.setLongitude("-109.05");
		hotspotDTO17.setHowMany("23");
		hotspotDTO17.setAuthor("Roberto Soldado");
		hotspotDTO17.setState("Sonora");
		hotspotDTO17.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO17.setObservationDate("2017-12-17");
		migrationTweets.add(hotspotDTO17);

		HotspotDTO hotspotDTO18 = new HotspotDTO();
		// #1.8
		hotspotDTO18.setLatitude("25.80");
		hotspotDTO18.setLongitude("-109.09");
		hotspotDTO18.setHowMany("21");
		hotspotDTO18.setAuthor("Julio Fonseca");
		hotspotDTO18.setState("Sinaloa");
		hotspotDTO18.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO18.setObservationDate("2017-12-18");
		migrationTweets.add(hotspotDTO18);

		HotspotDTO hotspotDTO19 = new HotspotDTO();
		// #1.9
		hotspotDTO19.setLatitude("24.53");
		hotspotDTO19.setLongitude("-111.30");
		hotspotDTO19.setHowMany("20");
		hotspotDTO19.setAuthor("Hernan Reyes");
		hotspotDTO19.setState("Baja California Sur");
		hotspotDTO19.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO19.setObservationDate("2017-12-20");
		migrationTweets.add(hotspotDTO19);

		HotspotDTO hotspotDTO110 = new HotspotDTO();
		// #1.10
		hotspotDTO110.setLatitude("23.52");
		hotspotDTO110.setLongitude("-109.90");
		hotspotDTO110.setHowMany("20");
		hotspotDTO110.setAuthor("Elena Neves");
		hotspotDTO110.setState("Baja California Sur");
		hotspotDTO110.setBirdSpecies(new ArrayList<String>(Arrays.asList("Swainson hawk")));
		hotspotDTO110.setObservationDate("2017-12-21");
		migrationTweets.add(hotspotDTO110);
		

		HotspotDTO hotspotDTO21 = new HotspotDTO();
		// #2.1
		hotspotDTO21.setLatitude("47.27");
		hotspotDTO21.setLongitude("-74.73");
		hotspotDTO21.setHowMany("10");
		hotspotDTO21.setAuthor("Chris Smalling");
		hotspotDTO21.setState("Quebec");
		hotspotDTO21.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO21.setObservationDate("2017-10-24");
		migrationTweets.add(hotspotDTO21);

		HotspotDTO hotspotDTO22 = new HotspotDTO();
		// #2.2
		hotspotDTO22.setLatitude("45.74");
		hotspotDTO22.setLongitude("-75.76");
		hotspotDTO22.setHowMany("12");
		hotspotDTO22.setAuthor("Ross Barkley");
		hotspotDTO22.setState("Quebec");
		hotspotDTO22.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO22.setObservationDate("2017-10-25");
		migrationTweets.add(hotspotDTO22);

		HotspotDTO hotspotDTO23 = new HotspotDTO();
		// #2.3
		hotspotDTO23.setLatitude("43.85");
		hotspotDTO23.setLongitude("-75.25");
		hotspotDTO23.setHowMany("11");
		hotspotDTO23.setAuthor("Chris Bridges");
		hotspotDTO23.setState("New York");
		hotspotDTO23.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO23.setObservationDate("2017-10-26");
		migrationTweets.add(hotspotDTO23);

		HotspotDTO hotspotDTO24 = new HotspotDTO();
		// #2.4
		hotspotDTO24.setLatitude("42.40");
		hotspotDTO24.setLongitude("-74.58");
		hotspotDTO24.setHowMany("14");
		hotspotDTO24.setAuthor("Jim Waters");
		hotspotDTO24.setState("New York");
		hotspotDTO24.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO24.setObservationDate("2017-10-26");
		migrationTweets.add(hotspotDTO24);

		HotspotDTO hotspotDTO25 = new HotspotDTO();
		// #2.5
		hotspotDTO25.setLatitude("40.29");
		hotspotDTO25.setLongitude("-77.24");
		hotspotDTO25.setHowMany("12");
		hotspotDTO25.setAuthor("Gary Silverton");
		hotspotDTO25.setState("Pennsylvania");
		hotspotDTO25.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO25.setObservationDate("2017-10-27");
		migrationTweets.add(hotspotDTO25);

		HotspotDTO hotspotDTO26 = new HotspotDTO();
		// #2.6
		hotspotDTO26.setLatitude("38.37");
		hotspotDTO26.setLongitude("-77.81");
		hotspotDTO26.setHowMany("15");
		hotspotDTO26.setAuthor("Jamie Vardy");
		hotspotDTO26.setState("Virginia");
		hotspotDTO26.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO26.setObservationDate("2017-10-29");
		migrationTweets.add(hotspotDTO26);

		HotspotDTO hotspotDTO27 = new HotspotDTO();
		// #2.7
		hotspotDTO27.setLatitude("36.06");
		hotspotDTO27.setLongitude("-77.07");
		hotspotDTO27.setHowMany("10");
		hotspotDTO27.setAuthor("Owen Hargreaves");
		hotspotDTO27.setState("North Carolina");
		hotspotDTO27.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO27.setObservationDate("2017-10-30");
		migrationTweets.add(hotspotDTO27);

		HotspotDTO hotspotDTO28 = new HotspotDTO();
		// #2.8
		hotspotDTO28.setLatitude("32.95");
		hotspotDTO28.setLongitude("-80.05");
		hotspotDTO28.setHowMany("17");
		hotspotDTO28.setAuthor("Andy Anderson");
		hotspotDTO28.setState("South Carolina");
		hotspotDTO28.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO28.setObservationDate("2017-10-31");
		migrationTweets.add(hotspotDTO28);

		HotspotDTO hotspotDTO29 = new HotspotDTO();
		// #2.9
		hotspotDTO29.setLatitude("30.81");
		hotspotDTO29.setLongitude("-83.50");
		hotspotDTO29.setHowMany("16");
		hotspotDTO29.setAuthor("David Duchovny");
		hotspotDTO29.setState("Georgia");
		hotspotDTO29.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO29.setObservationDate("2017-11-01");
		migrationTweets.add(hotspotDTO29);

		HotspotDTO hotspotDTO210 = new HotspotDTO();
		// #2.10
		hotspotDTO210.setLatitude("27.51");
		hotspotDTO210.setLongitude("-82.47");
		hotspotDTO210.setHowMany("16");
		hotspotDTO210.setAuthor("Cody Walker");
		hotspotDTO210.setState("Florida");
		hotspotDTO210.setBirdSpecies(new ArrayList<String>(Arrays.asList("Osprey")));
		hotspotDTO210.setObservationDate("2017-11-02");
		migrationTweets.add(hotspotDTO210);
		

		HotspotDTO hotspotDTO31 = new HotspotDTO();
		// #3.1
		hotspotDTO31.setLatitude("58.68");
		hotspotDTO31.setLongitude("7.17");
		hotspotDTO31.setHowMany("28");
		hotspotDTO31.setAuthor("Ankjell Karlof");
		hotspotDTO31.setCountry("Norway");
		hotspotDTO31.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO31.setObservationDate("2018-01-03");
		migrationTweets.add(hotspotDTO31);

		HotspotDTO hotspotDTO32 = new HotspotDTO();
		// #3.2
		hotspotDTO32.setLatitude("57.52");
		hotspotDTO32.setLongitude("12.81");
		hotspotDTO32.setHowMany("27");
		hotspotDTO32.setAuthor("Markus Berg");
		hotspotDTO32.setCountry("Sweden");
		hotspotDTO32.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO32.setObservationDate("2018-01-05");
		migrationTweets.add(hotspotDTO32);

		HotspotDTO hotspotDTO33 = new HotspotDTO();
		// #3.3
		hotspotDTO33.setLatitude("55.14");
		hotspotDTO33.setLongitude("11.87");
		hotspotDTO33.setHowMany("28");
		hotspotDTO33.setAuthor("Christian Eriksen");
		hotspotDTO33.setCountry("Denmark");
		hotspotDTO33.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO33.setObservationDate("2018-01-06");
		migrationTweets.add(hotspotDTO33);

		HotspotDTO hotspotDTO34 = new HotspotDTO();
		// #3.4
		hotspotDTO34.setLatitude("52.87");
		hotspotDTO34.setLongitude("8.11");
		hotspotDTO34.setHowMany("29");
		hotspotDTO34.setAuthor("Robert Adler");
		hotspotDTO34.setCountry("Germany");
		hotspotDTO34.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO34.setObservationDate("2018-01-08");
		migrationTweets.add(hotspotDTO34);

		HotspotDTO hotspotDTO35 = new HotspotDTO();
		// #3.5
		hotspotDTO35.setLatitude("50.87");
		hotspotDTO35.setLongitude("-5.91");
		hotspotDTO35.setHowMany("26");
		hotspotDTO35.setAuthor("Lasse Schone");
		hotspotDTO35.setCountry("Netherlands");
		hotspotDTO35.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO35.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO35);

		HotspotDTO hotspotDTO36 = new HotspotDTO();
		// #3.6
		hotspotDTO36.setLatitude("50.08");
		hotspotDTO36.setLongitude("5.08");
		hotspotDTO36.setHowMany("26");
		hotspotDTO36.setAuthor("Axel Witsel");
		hotspotDTO36.setCountry("Belgium");
		hotspotDTO36.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO36.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO36);

		HotspotDTO hotspotDTO37 = new HotspotDTO();
		// #3.7
		hotspotDTO37.setLatitude("48.44");
		hotspotDTO37.setLongitude("4.76");
		hotspotDTO37.setHowMany("24");
		hotspotDTO37.setAuthor("Alexandre Lacazette");
		hotspotDTO37.setCountry("France");
		hotspotDTO37.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO37.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO37);

		HotspotDTO hotspotDTO38 = new HotspotDTO();
		// #3.8
		hotspotDTO38.setLatitude("45.44");
		hotspotDTO38.setLongitude("0.48");
		hotspotDTO38.setHowMany("25");
		hotspotDTO38.setAuthor("Hugo Lloris");
		hotspotDTO38.setCountry("France");
		hotspotDTO38.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO38.setObservationDate("2018-01-12");
		migrationTweets.add(hotspotDTO38);

		HotspotDTO hotspotDTO39 = new HotspotDTO();
		// #3.9
		hotspotDTO39.setLatitude("42.66");
		hotspotDTO39.setLongitude("-4.53");
		hotspotDTO39.setHowMany("25");
		hotspotDTO39.setAuthor("David Silva");
		hotspotDTO39.setCountry("Spain");
		hotspotDTO39.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO39.setObservationDate("2018-01-14");
		migrationTweets.add(hotspotDTO39);

		HotspotDTO hotspotDTO310 = new HotspotDTO();
		// #3.10
		hotspotDTO310.setLatitude("39.68");
		hotspotDTO310.setLongitude("-8.81");
		hotspotDTO310.setHowMany("22");
		hotspotDTO310.setAuthor("Andre Gomes");
		hotspotDTO310.setCountry("Portugal");
		hotspotDTO310.setBirdSpecies(new ArrayList<String>(Arrays.asList("Bank Swallow")));
		hotspotDTO310.setObservationDate("2018-01-16");
		migrationTweets.add(hotspotDTO310);
		

		HotspotDTO hotspotDTO41 = new HotspotDTO();
		// #4.1
		hotspotDTO41.setLatitude("59.54");
		hotspotDTO41.setLongitude("39.36");
		hotspotDTO41.setHowMany("25");
		hotspotDTO41.setAuthor("Andrey Kokorin");
		hotspotDTO41.setCountry("Russia");
		hotspotDTO41.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO41.setObservationDate("2018-01-06");
		migrationTweets.add(hotspotDTO41);

		HotspotDTO hotspotDTO42 = new HotspotDTO();
		// #4.2
		hotspotDTO42.setLatitude("56.21");
		hotspotDTO42.setLongitude("34.13");
		hotspotDTO42.setHowMany("26");
		hotspotDTO42.setAuthor("Alan Dzagoev");
		hotspotDTO42.setCountry("Russia");
		hotspotDTO42.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO42.setObservationDate("2018-01-07");
		migrationTweets.add(hotspotDTO42);

		HotspotDTO hotspotDTO43 = new HotspotDTO();
		// #4.3
		hotspotDTO43.setLatitude("53.44");
		hotspotDTO43.setLongitude("31.10");
		hotspotDTO43.setHowMany("30");
		hotspotDTO43.setAuthor("Sergei Gurenko");
		hotspotDTO43.setCountry("Belarus");
		hotspotDTO43.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO43.setObservationDate("2018-01-08");
		migrationTweets.add(hotspotDTO43);

		HotspotDTO hotspotDTO44 = new HotspotDTO();
		// #4.4
		hotspotDTO44.setLatitude("50.54");
		hotspotDTO44.setLongitude("27.76");
		hotspotDTO44.setHowMany("32");
		hotspotDTO44.setAuthor("Andryi Shevchenko");
		hotspotDTO44.setCountry("Ukraine");
		hotspotDTO44.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO44.setObservationDate("2018-01-09");
		migrationTweets.add(hotspotDTO44);

		HotspotDTO hotspotDTO45 = new HotspotDTO();
		// #4.5
		hotspotDTO45.setLatitude("47.20");
		hotspotDTO45.setLongitude("27.28");
		hotspotDTO45.setHowMany("32");
		hotspotDTO45.setAuthor("Alexandru Mocanu");
		hotspotDTO45.setCountry("Romania");
		hotspotDTO45.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO45.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO45);

		HotspotDTO hotspotDTO46 = new HotspotDTO();
		// #4.6
		hotspotDTO46.setLatitude("44.40");
		hotspotDTO46.setLongitude("26.23");
		hotspotDTO46.setHowMany("32");
		hotspotDTO46.setAuthor("Ioan Petrescu");
		hotspotDTO46.setCountry("Romania");
		hotspotDTO46.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO46.setObservationDate("2018-01-11");
		migrationTweets.add(hotspotDTO46);

		HotspotDTO hotspotDTO47 = new HotspotDTO();
		// #4.7
		hotspotDTO47.setLatitude("37.80");
		hotspotDTO47.setLongitude("22.15");
		hotspotDTO47.setHowMany("35");
		hotspotDTO47.setAuthor("Sokratis Papastathopoulos");
		hotspotDTO47.setCountry("Greece");
		hotspotDTO47.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO47.setObservationDate("2018-01-14");
		migrationTweets.add(hotspotDTO47);

		HotspotDTO hotspotDTO48 = new HotspotDTO();
		// #4.8
		hotspotDTO48.setLatitude("30.05");
		hotspotDTO48.setLongitude("26.01");
		hotspotDTO48.setHowMany("33");
		hotspotDTO48.setAuthor("Asim Ramses");
		hotspotDTO48.setCountry("Egypt");
		hotspotDTO48.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO48.setObservationDate("2018-01-16");
		migrationTweets.add(hotspotDTO48);

		HotspotDTO hotspotDTO49 = new HotspotDTO();
		// #4.9
		hotspotDTO49.setLatitude("20.75");
		hotspotDTO49.setLongitude("35.21");
		hotspotDTO49.setHowMany("28");
		hotspotDTO49.setAuthor("Bakri Bashir");
		hotspotDTO49.setCountry("Sudan");
		hotspotDTO49.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO49.setObservationDate("2018-01-17");
		migrationTweets.add(hotspotDTO49);

		HotspotDTO hotspotDTO410 = new HotspotDTO();
		// #4.10
		hotspotDTO410.setLatitude("13.15");
		hotspotDTO410.setLongitude("40.43");
		hotspotDTO410.setHowMany("24");
		hotspotDTO410.setAuthor("Jemal Tassew");
		hotspotDTO410.setCountry("Ethiopia");
		hotspotDTO410.setBirdSpecies(new ArrayList<String>(Arrays.asList("Sandhill crane")));
		hotspotDTO410.setObservationDate("2018-01-19");
		migrationTweets.add(hotspotDTO410);
		
		return migrationTweets;
	}
	
	@SuppressWarnings("deprecation")
	public List<TweetDTO> mockMigrationDataTwitter() {
		List<TweetDTO> migrationTweets = new ArrayList<>();
		
		TweetDTO tweetDTO11 = new TweetDTO();
		// #1.1
		tweetDTO11.setLatitude("38.44");
		tweetDTO11.setLongitude("-105.70");
		tweetDTO11.setUser(new TwitterUserDTO(){{setName("Arnold Sheppard");}});
		tweetDTO11.setTweetId(11L);
		tweetDTO11.setTweetMessage("Swainson hawk");
		tweetDTO11.setObservationDate(new Date("2017-12-13"));
		migrationTweets.add(tweetDTO11);
		
		TweetDTO tweetDTO12 = new TweetDTO();
		// #1.2
		tweetDTO12.setLatitude("37.26");
		tweetDTO12.setLongitude("-105.60");
		tweetDTO12.setUser(new TwitterUserDTO(){{setName("John Wick");}});
		tweetDTO12.setTweetId(12L);
		tweetDTO12.setTweetMessage("Swainson hawk");
		tweetDTO12.setObservationDate(new Date("2017-12-13"));
		migrationTweets.add(tweetDTO12);
		
		TweetDTO tweetDTO13 = new TweetDTO();
		// #1.3
		tweetDTO13.setLatitude("36.31");
		tweetDTO13.setLongitude("-108.49");
		tweetDTO13.setUser(new TwitterUserDTO(){{setName("Tobey Marshall");}});
		tweetDTO13.setTweetId(13L);
		tweetDTO13.setTweetMessage("Swainson hawk");
		tweetDTO13.setObservationDate(new Date("2017-12-14"));
		migrationTweets.add(tweetDTO13);
		
		TweetDTO tweetDTO14 = new TweetDTO();
		// #1.4
		tweetDTO14.setLatitude("32.60");
		tweetDTO14.setLongitude("-110.33");
		tweetDTO14.setUser(new TwitterUserDTO(){{setName("Dino Brewster");}});
		tweetDTO14.setTweetId(14L);
		tweetDTO14.setTweetMessage("Swainson hawk");
		tweetDTO14.setObservationDate(new Date("2017-12-15"));
		migrationTweets.add(tweetDTO14);

		TweetDTO tweetDTO15 = new TweetDTO();
		// #1.5
		tweetDTO15.setLatitude("30.45");
		tweetDTO15.setLongitude("-109.14");
		tweetDTO15.setUser(new TwitterUserDTO(){{setName("Tego Calderon");}});
		tweetDTO15.setTweetId(15L);
		tweetDTO15.setTweetMessage("Swainson hawk");
		tweetDTO15.setObservationDate(new Date("2017-12-16"));
		migrationTweets.add(tweetDTO15);

		TweetDTO tweetDTO16 = new TweetDTO();
		// #1.6
		tweetDTO16.setLatitude("28.67");
		tweetDTO16.setLongitude("-107.78");
		tweetDTO16.setUser(new TwitterUserDTO(){{setName("Arturo Braga");}});
		tweetDTO16.setTweetId(16L);
		tweetDTO16.setTweetMessage("Swainson hawk");
		tweetDTO16.setObservationDate(new Date("2017-12-17"));
		migrationTweets.add(tweetDTO16);

		TweetDTO tweetDTO17 = new TweetDTO();
		// #1.7
		tweetDTO17.setLatitude("26.76");
		tweetDTO17.setLongitude("-109.05");
		tweetDTO17.setUser(new TwitterUserDTO(){{setName("Roberto Soldado");}});
		tweetDTO17.setTweetId(17L);
		tweetDTO17.setTweetMessage("Swainson hawk");
		tweetDTO17.setObservationDate(new Date("2017-12-17"));
		migrationTweets.add(tweetDTO17);

		TweetDTO tweetDTO18 = new TweetDTO();
		// #1.8
		tweetDTO18.setLatitude("25.80");
		tweetDTO18.setLongitude("-109.09");
		tweetDTO18.setUser(new TwitterUserDTO(){{setName("Julio Fonseca");}});
		tweetDTO18.setTweetId(18L);
		tweetDTO18.setTweetMessage("Swainson hawk");
		tweetDTO18.setObservationDate(new Date("2017-12-18"));
		migrationTweets.add(tweetDTO18);

		TweetDTO tweetDTO19 = new TweetDTO();
		// #1.9
		tweetDTO19.setLatitude("24.53");
		tweetDTO19.setLongitude("-111.30");
		tweetDTO19.setUser(new TwitterUserDTO(){{setName("Hernan Reyes");}});
		tweetDTO19.setTweetId(19L);
		tweetDTO19.setTweetMessage("Swainson hawk");
		tweetDTO19.setObservationDate(new Date("2017-12-20"));
		migrationTweets.add(tweetDTO19);

		TweetDTO tweetDTO110 = new TweetDTO();
		// #1.10
		tweetDTO110.setLatitude("23.52");
		tweetDTO110.setLongitude("-109.90");
		tweetDTO110.setUser(new TwitterUserDTO(){{setName("Elena Neves");}});
		tweetDTO110.setTweetId(110L);
		tweetDTO110.setTweetMessage("Swainson hawk");
		tweetDTO110.setObservationDate(new Date("2017-12-21"));
		migrationTweets.add(tweetDTO110);
		

		TweetDTO tweetDTO21 = new TweetDTO();
		// #2.1
		tweetDTO21.setLatitude("47.27");
		tweetDTO21.setLongitude("-74.73");
		tweetDTO21.setUser(new TwitterUserDTO(){{setName("Chris Smalling");}});
		tweetDTO21.setTweetId(21L);
		tweetDTO21.setTweetMessage("Osprey");
		tweetDTO21.setObservationDate(new Date("2017-10-24"));
		migrationTweets.add(tweetDTO21);

		TweetDTO tweetDTO22 = new TweetDTO();
		// #2.2
		tweetDTO22.setLatitude("45.74");
		tweetDTO22.setLongitude("-75.76");
		tweetDTO22.setUser(new TwitterUserDTO(){{setName("Ross Barkley");}});
		tweetDTO22.setTweetId(22L);
		tweetDTO22.setTweetMessage("Osprey");
		tweetDTO22.setObservationDate(new Date("2017-10-25"));
		migrationTweets.add(tweetDTO22);

		TweetDTO tweetDTO23 = new TweetDTO();
		// #2.3
		tweetDTO23.setLatitude("43.85");
		tweetDTO23.setLongitude("-75.25");
		tweetDTO23.setUser(new TwitterUserDTO(){{setName("Chris Bridges");}});
		tweetDTO23.setTweetId(23L);
		tweetDTO23.setTweetMessage("Osprey");
		tweetDTO23.setObservationDate(new Date("2017-10-26"));
		migrationTweets.add(tweetDTO23);

		TweetDTO tweetDTO24 = new TweetDTO();
		// #2.4
		tweetDTO24.setLatitude("42.40");
		tweetDTO24.setLongitude("-74.58");
		tweetDTO24.setUser(new TwitterUserDTO(){{setName("Jim Waters");}});
		tweetDTO24.setTweetId(24L);
		tweetDTO24.setTweetMessage("Osprey");
		tweetDTO24.setObservationDate(new Date("2017-10-26"));
		migrationTweets.add(tweetDTO24);

		TweetDTO tweetDTO25 = new TweetDTO();
		// #2.5
		tweetDTO25.setLatitude("40.29");
		tweetDTO25.setLongitude("-77.24");
		tweetDTO25.setUser(new TwitterUserDTO(){{setName("Gary Silverton");}});
		tweetDTO25.setTweetId(25L);
		tweetDTO25.setTweetMessage("Osprey");
		tweetDTO25.setObservationDate(new Date("2017-10-27"));
		migrationTweets.add(tweetDTO25);

		TweetDTO tweetDTO26 = new TweetDTO();
		// #2.6
		tweetDTO26.setLatitude("38.37");
		tweetDTO26.setLongitude("-77.81");
		tweetDTO26.setUser(new TwitterUserDTO(){{setName("Jamie Vardy");}});
		tweetDTO26.setTweetId(26L);
		tweetDTO26.setTweetMessage("Osprey");
		tweetDTO26.setObservationDate(new Date("2017-10-29"));
		migrationTweets.add(tweetDTO26);

		TweetDTO tweetDTO27 = new TweetDTO();
		// #2.7
		tweetDTO27.setLatitude("36.06");
		tweetDTO27.setLongitude("-77.07");
		tweetDTO27.setUser(new TwitterUserDTO(){{setName("Owen Hargreaves");}});
		tweetDTO27.setTweetId(27L);
		tweetDTO27.setTweetMessage("Osprey");
		tweetDTO27.setObservationDate(new Date("2017-10-30"));
		migrationTweets.add(tweetDTO27);

		TweetDTO tweetDTO28 = new TweetDTO();
		// #2.8
		tweetDTO28.setLatitude("32.95");
		tweetDTO28.setLongitude("-80.05");
		tweetDTO28.setUser(new TwitterUserDTO(){{setName("Andy Anderson");}});
		tweetDTO28.setTweetId(28L);
		tweetDTO28.setTweetMessage("Osprey");
		tweetDTO28.setObservationDate(new Date("2017-10-31"));
		migrationTweets.add(tweetDTO28);

		TweetDTO tweetDTO29 = new TweetDTO();
		// #2.9
		tweetDTO29.setLatitude("30.81");
		tweetDTO29.setLongitude("-83.50");
		tweetDTO29.setUser(new TwitterUserDTO(){{setName("David Duchovny");}});
		tweetDTO29.setTweetId(29L);
		tweetDTO29.setTweetMessage("Osprey");
		tweetDTO29.setObservationDate(new Date("2017-11-01"));
		migrationTweets.add(tweetDTO29);

		TweetDTO tweetDTO210 = new TweetDTO();
		// #2.10
		tweetDTO210.setLatitude("27.51");
		tweetDTO210.setLongitude("-82.47");
		tweetDTO210.setUser(new TwitterUserDTO(){{setName("Cody Walker");}});
		tweetDTO210.setTweetId(210L);
		tweetDTO210.setTweetMessage("Osprey");
		tweetDTO210.setObservationDate(new Date("2017-11-02"));
		migrationTweets.add(tweetDTO210);
		

		TweetDTO tweetDTO31 = new TweetDTO();
		// #3.1
		tweetDTO31.setLatitude("58.68");
		tweetDTO31.setLongitude("7.17");
		tweetDTO31.setUser(new TwitterUserDTO(){{setName("Ankjell Karlof");}});
		tweetDTO31.setTweetId(31L);
		tweetDTO31.setTweetMessage("Bank Swallow");
		tweetDTO31.setObservationDate(new Date("2018-01-03"));
		migrationTweets.add(tweetDTO31);

		TweetDTO tweetDTO32 = new TweetDTO();
		// #3.2
		tweetDTO32.setLatitude("57.52");
		tweetDTO32.setLongitude("12.81");
		tweetDTO32.setUser(new TwitterUserDTO(){{setName("Markus Berg");}});
		tweetDTO32.setTweetId(32L);
		tweetDTO32.setTweetMessage("Bank Swallow");
		tweetDTO32.setObservationDate(new Date("2018-01-05"));
		migrationTweets.add(tweetDTO32);

		TweetDTO tweetDTO33 = new TweetDTO();
		// #3.3
		tweetDTO33.setLatitude("55.14");
		tweetDTO33.setLongitude("11.87");
		tweetDTO33.setUser(new TwitterUserDTO(){{setName("Christian Eriksen");}});
		tweetDTO33.setTweetId(33L);
		tweetDTO33.setTweetMessage("Bank Swallow");
		tweetDTO33.setObservationDate(new Date("2018-01-06"));
		migrationTweets.add(tweetDTO33);

		TweetDTO tweetDTO34 = new TweetDTO();
		// #3.4
		tweetDTO34.setLatitude("52.87");
		tweetDTO34.setLongitude("8.11");
		tweetDTO34.setUser(new TwitterUserDTO(){{setName("Robert Adler");}});
		tweetDTO34.setTweetId(34L);
		tweetDTO34.setTweetMessage("Bank Swallow");
		tweetDTO34.setObservationDate(new Date("2018-01-08"));
		migrationTweets.add(tweetDTO34);

		TweetDTO tweetDTO35 = new TweetDTO();
		// #3.5
		tweetDTO35.setLatitude("50.87");
		tweetDTO35.setLongitude("-5.91");
		tweetDTO35.setUser(new TwitterUserDTO(){{setName("Lasse Schone");}});
		tweetDTO35.setTweetId(35L);
		tweetDTO35.setTweetMessage("Bank Swallow");
		tweetDTO35.setObservationDate(new Date("2018-01-09"));
		migrationTweets.add(tweetDTO35);

		TweetDTO tweetDTO36 = new TweetDTO();
		// #3.6
		tweetDTO36.setLatitude("50.08");
		tweetDTO36.setLongitude("5.08");
		tweetDTO36.setUser(new TwitterUserDTO(){{setName("Axel Witsel");}});
		tweetDTO36.setTweetId(36L);
		tweetDTO36.setTweetMessage("Bank Swallow");
		tweetDTO36.setObservationDate(new Date("2018-01-09"));
		migrationTweets.add(tweetDTO36);

		TweetDTO tweetDTO37 = new TweetDTO();
		// #3.7
		tweetDTO37.setLatitude("48.44");
		tweetDTO37.setLongitude("4.76");
		tweetDTO37.setUser(new TwitterUserDTO(){{setName("Alexandre Lacazette");}});
		tweetDTO37.setTweetId(37L);
		tweetDTO37.setTweetMessage("Bank Swallow");
		tweetDTO37.setObservationDate(new Date("2018-01-11"));
		migrationTweets.add(tweetDTO37);

		TweetDTO tweetDTO38 = new TweetDTO();
		// #3.8
		tweetDTO38.setLatitude("45.44");
		tweetDTO38.setLongitude("0.48");
		tweetDTO38.setUser(new TwitterUserDTO(){{setName("Hugo Lloris");}});
		tweetDTO38.setTweetId(38L);
		tweetDTO38.setTweetMessage("Bank Swallow");
		tweetDTO38.setObservationDate(new Date("2018-01-12"));
		migrationTweets.add(tweetDTO38);

		TweetDTO tweetDTO39 = new TweetDTO();
		// #3.9
		tweetDTO39.setLatitude("42.66");
		tweetDTO39.setLongitude("-4.53");
		tweetDTO39.setUser(new TwitterUserDTO(){{setName("David Silva");}});
		tweetDTO39.setTweetId(39L);
		tweetDTO39.setTweetMessage("Bank Swallow");
		tweetDTO39.setObservationDate(new Date("2018-01-14"));
		migrationTweets.add(tweetDTO39);

		TweetDTO tweetDTO310 = new TweetDTO();
		// #3.10
		tweetDTO310.setLatitude("39.68");
		tweetDTO310.setLongitude("-8.81");
		tweetDTO310.setUser(new TwitterUserDTO(){{setName("Andre Gomes");}});
		tweetDTO310.setTweetId(310L);
		tweetDTO310.setTweetMessage("Bank Swallow");
		tweetDTO310.setObservationDate(new Date("2018-01-16"));
		migrationTweets.add(tweetDTO310);
		

		TweetDTO tweetDTO41 = new TweetDTO();
		// #4.1
		tweetDTO41.setLatitude("59.54");
		tweetDTO41.setLongitude("39.36");
		tweetDTO41.setUser(new TwitterUserDTO(){{setName("Andrey Kokorin");}});
		tweetDTO41.setTweetId(41L);
		tweetDTO41.setTweetMessage("Sandhill Crane");
		tweetDTO41.setObservationDate(new Date("2018-01-06"));
		migrationTweets.add(tweetDTO41);

		TweetDTO tweetDTO42 = new TweetDTO();
		// #4.2
		tweetDTO42.setLatitude("56.21");
		tweetDTO42.setLongitude("34.13");
		tweetDTO42.setUser(new TwitterUserDTO(){{setName("Alan Dzagoev");}});
		tweetDTO42.setTweetId(42L);
		tweetDTO42.setTweetMessage("Sandhill Crane");
		tweetDTO42.setObservationDate(new Date("2018-01-07"));
		migrationTweets.add(tweetDTO42);

		TweetDTO tweetDTO43 = new TweetDTO();
		// #4.3
		tweetDTO43.setLatitude("53.44");
		tweetDTO43.setLongitude("31.10");
		tweetDTO43.setUser(new TwitterUserDTO(){{setName("Sergei Gurenko");}});
		tweetDTO43.setTweetId(43L);
		tweetDTO43.setTweetMessage("Sandhill Crane");
		tweetDTO43.setObservationDate(new Date("2018-01-08"));
		migrationTweets.add(tweetDTO43);

		TweetDTO tweetDTO44 = new TweetDTO();
		// #4.4
		tweetDTO44.setLatitude("50.54");
		tweetDTO44.setLongitude("27.76");
		tweetDTO44.setUser(new TwitterUserDTO(){{setName("Andryi Shevchenko");}});
		tweetDTO44.setTweetId(44L);
		tweetDTO44.setTweetMessage("Sandhill Crane");
		tweetDTO44.setObservationDate(new Date("2018-01-09"));
		migrationTweets.add(tweetDTO44);

		TweetDTO tweetDTO45 = new TweetDTO();
		// #4.5
		tweetDTO45.setLatitude("47.20");
		tweetDTO45.setLongitude("27.28");
		tweetDTO45.setUser(new TwitterUserDTO(){{setName("Alexandru Mocanu");}});
		tweetDTO45.setTweetId(45L);
		tweetDTO45.setTweetMessage("Sandhill Crane");
		tweetDTO45.setObservationDate(new Date("2018-01-11"));
		migrationTweets.add(tweetDTO45);

		TweetDTO tweetDTO46 = new TweetDTO();
		// #4.6
		tweetDTO46.setLatitude("44.40");
		tweetDTO46.setLongitude("26.23");
		tweetDTO46.setUser(new TwitterUserDTO(){{setName("Ioan Petrescu");}});
		tweetDTO46.setTweetId(46L);
		tweetDTO46.setTweetMessage("Sandhill Crane");
		tweetDTO46.setObservationDate(new Date("2018-01-11"));
		migrationTweets.add(tweetDTO46);

		TweetDTO tweetDTO47 = new TweetDTO();
		// #4.7
		tweetDTO47.setLatitude("37.80");
		tweetDTO47.setLongitude("22.15");
		tweetDTO47.setUser(new TwitterUserDTO(){{setName("Sokratis Papastathopoulos");}});
		tweetDTO47.setTweetId(47L);
		tweetDTO47.setTweetMessage("Sandhill Crane");
		tweetDTO47.setObservationDate(new Date("2018-01-14"));
		migrationTweets.add(tweetDTO47);

		TweetDTO tweetDTO48 = new TweetDTO();
		// #4.8
		tweetDTO48.setLatitude("30.05");
		tweetDTO48.setLongitude("26.01");
		tweetDTO48.setUser(new TwitterUserDTO(){{setName("Asim Ramses");}});
		tweetDTO48.setTweetId(48L);
		tweetDTO48.setTweetMessage("Sandhill Crane");
		tweetDTO48.setObservationDate(new Date("2018-01-16"));
		migrationTweets.add(tweetDTO48);

		TweetDTO tweetDTO49 = new TweetDTO();
		// #4.9
		tweetDTO49.setLatitude("20.75");
		tweetDTO49.setLongitude("35.21");
		tweetDTO49.setUser(new TwitterUserDTO(){{setName("Bakri Bashir");}});
		tweetDTO49.setTweetId(49L);
		tweetDTO49.setTweetMessage("Sandhill Crane");
		tweetDTO49.setObservationDate(new Date("2018-01-17"));
		migrationTweets.add(tweetDTO49);

		TweetDTO tweetDTO410 = new TweetDTO();
		// #4.10
		tweetDTO410.setLatitude("13.15");
		tweetDTO410.setLongitude("40.43");
		tweetDTO410.setUser(new TwitterUserDTO(){{setName("Jemal Tassew");}});
		tweetDTO410.setTweetId(410L);
		tweetDTO410.setTweetMessage("Sandhill Crane");
		tweetDTO410.setObservationDate(new Date("2018-01-19"));
		migrationTweets.add(tweetDTO410);
		
		return migrationTweets;
	}
	
	private void replaceTagNames(Model model) {
		model.setNsPrefix("hotspot", RdfEnum.BASE_URI.getCode());
		model.setNsPrefix("location", RdfEnum.LOCATION_URI.getCode());
		model.setNsPrefix("tweet", RdfEnum.TWEET_URI.getCode());
		model.setNsPrefix("observation", RdfEnum.OBSERVATION_URI.getCode());
	}
}