package bimr.bf;

import bimr.bfcl.RdfModelFacade;
import bimr.bfcl.dto.HotspotDTO;
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
import java.util.ArrayList;
import java.util.Collections;
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

	private List<HotspotDTO> mockMigrationData() {
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