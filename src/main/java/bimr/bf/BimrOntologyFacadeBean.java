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
import java.util.*;

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
	public Map<String, List<Model>> createModelsForHotspots(List<HotspotDTO> hotspots) {
		Map<String, List<Model>> allModelsForAHotspot = new HashMap<>();
		for (HotspotDTO hotspot : hotspots) {

			hotspot.setHotspotId(getUUID());

			List<Model> models = createAllModelsForAHotspot(hotspot);
			allModelsForAHotspot.put(hotspot.getHotspotId(), models);
		}
		return allModelsForAHotspot;
	}

	@Override
	public void persistAllModelsForAHotspot(List<Model> models) {
		for (Model model : models) {
			hotspotsDatasetAccesor.add(model);
		}
	}

	@Override
	public void printAllModelsForAHotspot(List<Model> models) {
		for (Model model : models) {
			model.write(System.out, BimrOntologyEnum.RDF_XML_FORMAT.getCode());
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

	private List<Model> createAllModelsForAHotspot(HotspotDTO hotspot) {
		List<Model> models = new ArrayList<>();

		if (hotspot.getUser() != null) {
			models.add(createUserModel(hotspot));
		}
		models.add(createHotspotModel(hotspot));

		return models;
	}

	private Model createHotspotModel(HotspotDTO hotspot) {
		Model hotspotModel = hotspotsDataset.getNamedModel(hotspot.getHotspotId());
		Resource hotspotResource = hotspotModel.createResource(BIMR.getHotspotUri(""));

		if (hotspot.getUser() != null) {
			checkAndAddProperty(hotspotResource, VCARD.UID, hotspot.getUser().getId());
		}
		checkAndAddProperty(hotspotResource, BIMR.hotspotId, hotspot.getHotspotId());

		hotspotResource.addProperty(BIMR.observation, createObservationResource(hotspotModel, hotspot));

		return hotspotModel;
	}

	private Model createUserModel(HotspotDTO hotspot) {
		TwitterUserDTO userDTO = hotspot.getUser();
		Model userModel = hotspotsDataset.getNamedModel(getUUID());
		Resource user = userModel.createResource(BIMR.getUserUri(""));

		checkAndAddProperty(user, VCARD.NICKNAME, userDTO.getScreenName());
		checkAndAddProperty(user, VCARD.ADR, userDTO.getLocation());
		checkAndAddProperty(user, VCARD.NAME, userDTO.getName());
		checkAndAddProperty(user, VCARD.UID, userDTO.getId());
		checkAndAddProperty(user, VCARD.EMAIL, userDTO.getEmail());
		checkAndAddProperty(user, BIMR.hasGeoEnabled, userDTO.isGeoEnabled());

		return userModel;
	}

	private Resource createObservationResource(Model model, HotspotDTO hotspot) {
		Resource observation = model.createResource(BIMR.getObservationUri(""));

		for (String species : hotspot.getBirdSpecies()) {
			checkAndAddProperty(observation, BIMR.birdSpecies, species);
		}
		checkAndAddProperty(observation, BIMR.howMany, hotspot.getHowMany());
		checkAndAddProperty(observation, BIMR.date, hotspot.getObservationDate());
		checkAndAddProperty(observation, BIMR.informationSourceId, hotspot.getInformationSourceId());
		checkAndAddProperty(observation, BIMR.tweetId, hotspot.getTweetId());

		observation.addProperty(BIMR.tweet, createTweetResource(model, hotspot));
		observation.addProperty(BIMR.location, createLocationResource(model, hotspot));

		return observation;
	}


	private Resource createLocationResource(Model model, HotspotDTO hotspot) {
		Resource location = model.createResource(BIMR.getLocationUri(""));

		checkAndAddProperty(location, BIMR.latitude, hotspot.getLatitude());
		checkAndAddProperty(location, BIMR.longitude, hotspot.getLongitude());
		checkAndAddProperty(location, BIMR.city, hotspot.getCity());
		checkAndAddProperty(location, BIMR.country, hotspot.getCountry());
		checkAndAddProperty(location, BIMR.state, hotspot.getState());

		return location;
	}

	private Resource createTweetResource(Model model, HotspotDTO hotspot) {
		Resource tweet = model.createResource(BIMR.getTweetUri(""));

		checkAndAddProperty(tweet, BIMR.tweetId, hotspot.getTweetId());
		checkAndAddProperty(tweet, BIMR.language, GeneralConstants.EN_LANGUAGE);
		checkAndAddProperty(tweet, BIMR.text, hotspot.getTweetMessage());
		checkAndAddProperty(tweet, BIMR.link, hotspot.getLink());
		checkAndAddProperty(tweet, BIMR.author, hotspot.getAuthor());

		return tweet;
	}

	private void checkAndAddProperty(Resource resource, Property property, String value) {
		if (value != null) {
			resource.addProperty(property, value);
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}

}