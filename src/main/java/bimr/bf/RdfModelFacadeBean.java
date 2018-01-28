package bimr.bf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

	public List<HotspotDTO> mockMigrationData() {
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
	
	private void replaceTagNames(Model model) {
		model.setNsPrefix("hotspot", RdfEnum.BASE_URI.getCode());
		model.setNsPrefix("location", RdfEnum.LOCATION_URI.getCode());
		model.setNsPrefix("tweet", RdfEnum.TWEET_URI.getCode());
		model.setNsPrefix("observation", RdfEnum.OBSERVATION_URI.getCode());
	}
}