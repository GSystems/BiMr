package bimr.bf;

import bimr.bf.transformer.OntologyTransformer;
import bimr.bfcl.BimrOntologyFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import bimr.util.BimrOntologyEnum;
import bimr.util.GeneralConstants;
import bimr.util.rdf.vocabulary.BIMR;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Startup
@Singleton
public class BimrOntologyFacadeBean implements BimrOntologyFacade {

	private Dataset hotspotsDataset;
	private Dataset migrationDataset;
	private DatasetAccessor hotspotsDatasetAccesor;
	private DatasetAccessor migrationsDatasetAccesor;

	@PostConstruct
	public void init() {
		hotspotsDataset = TDBFactory.createDataset();
		migrationDataset = TDBFactory.createDataset();
		hotspotsDatasetAccesor = DatasetAccessorFactory.createHTTP(GeneralConstants.MOCKED_HOTSPOTS_DATASET_ADDR);
		migrationsDatasetAccesor = DatasetAccessorFactory.createHTTP(GeneralConstants.MOCKED_MIGRATIONS_DATASET_ADDR);
	}

	@PreDestroy
	public void cleanup() {
		hotspotsDataset.close();
		migrationDataset.close();
	}

	@Override
	public List<Model> generateHotspotModels(List<HotspotDTO> hotspots) {
		List<Model> models = new ArrayList<>();
		for (HotspotDTO hotspot : hotspots) {
			Model model = hotspotsDataset.getNamedModel(getUUID());
			createHotspotResource(model, hotspot);
			models.add(model);
		}
		return models;
	}

	@Override
	public void persistHotspotModels(List<Model> models) {
		for (Model model : models) {
			hotspotsDatasetAccesor.add(model);
		}
	}

	@Override
	public List<HotspotDTO> getAllHotspots() {
		QueryExecution allTweetsQuery = QueryExecutionFactory
				.sparqlService(GeneralConstants.MOCKED_HOTSPOTS_DATASET_QRY_ADDR,
						BimrOntologyEnum.GET_ALL_HOTSPOTS_QRY.getCode());

		ResultSet results = allTweetsQuery.execSelect();

		return OntologyTransformer.fromRdfToHotspotDTOList(results);
	}

	private void createHotspotResource(Model model, HotspotDTO hotspotDTO) {
		Resource hotspotResource = model.createResource(BIMR.getBaseUri(getUUID()));

		if (hotspotDTO.getUser() != null) {
			Resource user = createUserResource(model, hotspotDTO);
			hotspotResource.addProperty(BIMR.user, user);
		}
		Resource observation = createObservationResource(model, hotspotDTO);
		hotspotResource.addProperty(BIMR.observation, observation);
	}

	private static Resource createUserResource(Model model, HotspotDTO hotspotDTO) {
		Resource user = model.createResource(BIMR.getUserUri(getUUID()));
		TwitterUserDTO userDTO = hotspotDTO.getUser();

		checkAndAddProperty(user, VCARD.NICKNAME, userDTO.getScreenName());
		checkAndAddProperty(user, VCARD.ADR, userDTO.getLocation());
		checkAndAddProperty(user, VCARD.NAME, userDTO.getName());
		checkAndAddProperty(user, VCARD.UID, userDTO.getId());
		checkAndAddProperty(user, VCARD.EMAIL, userDTO.getEmail());
		checkAndAddProperty(user, VCARD.GEO, userDTO.isGeoEnabled());

		return user;
	}

	private static Resource createObservationResource(Model model, HotspotDTO hotspotDTO) {
		Resource observation = model.createResource(BIMR.getObservationUri(getUUID()));

		for (String species : hotspotDTO.getBirdSpecies()) {
			checkAndAddProperty(observation, BIMR.birdSpecies, species);
		}
		checkAndAddProperty(observation, BIMR.howMany, hotspotDTO.getHowMany());
		checkAndAddProperty(observation, BIMR.date, hotspotDTO.getObservationDate());
		checkAndAddProperty(observation, BIMR.informationSourceId, GeneralConstants.TWITTER_SOURCE);

		Resource tweet = createTweetResource(model, hotspotDTO);
		Resource location = createLocationResource(model, hotspotDTO);

		observation.addProperty(BIMR.tweet, tweet);
		observation.addProperty(BIMR.location, location);

		return observation;
	}


	private static Resource createLocationResource(Model model, HotspotDTO hotspotDTO) {
		Resource location = model.createResource(BIMR.getLocationUri(getUUID()));

		checkAndAddProperty(location, BIMR.latitude, hotspotDTO.getLatitude());
		checkAndAddProperty(location, BIMR.longitude, hotspotDTO.getLongitude());
		checkAndAddProperty(location, BIMR.name, hotspotDTO.getLocationName());
		checkAndAddProperty(location, BIMR.country, hotspotDTO.getCountry());
		checkAndAddProperty(location, BIMR.state, hotspotDTO.getState());

		return location;
	}

	private static Resource createTweetResource(Model model, HotspotDTO hotspotDTO) {
		Resource tweet = model.createResource(BIMR.getTweetUri(getUUID()));

		checkAndAddProperty(tweet, BIMR.tweetId, hotspotDTO.getTweetId());
		checkAndAddProperty(tweet, BIMR.language, GeneralConstants.EN_LANGUAGE);
		checkAndAddProperty(tweet, BIMR.text, hotspotDTO.getTweetMessage());
		if (hotspotDTO.getUser() != null) {
			checkAndAddProperty(tweet, VCARD.UID, hotspotDTO.getUser().getId());
		}
		checkAndAddProperty(tweet, BIMR.link, hotspotDTO.getLink());
		checkAndAddProperty(tweet, BIMR.author, hotspotDTO.getAuthor());

		return tweet;
	}

	private static void checkAndAddProperty(Resource resource, Property property, String value) {
		if (value != null) {
			resource.addProperty(property, value);
		}
	}

	private static String getUUID() {
		return UUID.randomUUID().toString();
	}

}