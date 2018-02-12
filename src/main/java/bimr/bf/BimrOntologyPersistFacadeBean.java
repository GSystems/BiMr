package bimr.bf;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.BimrOntologyPersistFacade;
import bimr.bfcl.BimrOntologyReadFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.LocationDTO;
import bimr.bfcl.dto.MigrationDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import bimr.util.BimrOntologyEnum;
import bimr.util.GeneralConstants;
import bimr.util.rdf.vocabulary.BIMR;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.valueOf;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdateTime;

/**
 * @author: GLK
 */

@Startup
@Singleton
public class BimrOntologyPersistFacadeBean implements BimrOntologyPersistFacade {

	private Dataset hotspotsDataset;
	private Dataset migrationDataset;
	private DatasetAccessor hotspotsDatasetAccesor;
	private DatasetAccessor migrationsDatasetAccesor;

	@EJB
	private BimrOntologyReadFacade bimrOntologyReadFacade;

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
	public Map<String, List<Model>> createModelsForHotspot(List<HotspotDTO> hotspots) {
		Map<String, List<Model>> allModelsForAHotspot = new HashMap<>();
		for (HotspotDTO hotspot : hotspots) {

			hotspot.setHotspotId(getUUID());

			List<Model> models = createAllModelsForAHotspot(hotspot);
			allModelsForAHotspot.put(hotspot.getHotspotId(), models);
		}
		return allModelsForAHotspot;
	}

	@Override
	public void persistAllModelsForAHotspot(Model model) {
		hotspotsDatasetAccesor.add(model);
	}

	@Override
	public void persistMigrationModel(Model model) {
		migrationsDatasetAccesor.add(model);
	}

	@Override
	public void printModel(Model model) {
		model.write(System.out, BimrOntologyEnum.RDF_XML_FORMAT.getCode());
	}

	@Override
	public void checkAndSaveMigrations() {
		List<MigrationDTO> migrations = groupHotspotsAndGetMigrations(bimrOntologyReadFacade.getAllHotspots());

		if (!migrations.isEmpty()) {
			for(MigrationDTO migration : migrations) {
				migration.setMigrationId(getUUID());
				Model migrationModel = createMigrationModel(migration);
				persistMigrationModel(migrationModel);
			}
		}
	}

	private List<MigrationDTO> groupHotspotsAndGetMigrations(List<HotspotDTO> hotspots) {
		Map<String, List<HotspotDTO>> hotspotsMapGroupedBySpecies = new HashMap<>();
		List <HotspotDTO> temp = new ArrayList<>();

		for (HotspotDTO hotspot : hotspots) {
			for (String species : hotspot.getBirdSpecies()) {

				if (hotspotsMapGroupedBySpecies.containsKey(species)) {
					hotspotsMapGroupedBySpecies.get(species).add(hotspot);
				} else {
					temp = new ArrayList<>();
					temp.add(hotspot);
					hotspotsMapGroupedBySpecies.put(species, temp);
				}
			}
		}

		return MapTransformer.fromHotspotsMapToMigrationsList(hotspotsMapGroupedBySpecies);
	}

//	private Map<String, List<MigrationDTO>> searchForMigrations(Map<String, List<HotspotDTO>> speciesGroupedByLocations) {
//		final double delta = 10.0;
//
//		Map<String, List<MigrationDTO>> migrationsMap = new HashMap<>();
//		List<MigrationDTO> migrations;
//
//		for (Map.Entry<String, List<HotspotDTO>> entry : speciesGroupedByLocations.entrySet()) {
//			if (entry.getValue().size() == 1) {
//				continue;
//			} else {
//				Double meanLatitude = 0D;
//				Double meanLongitude = 0D;
//
//				for (HotspotDTO hotspot : entry.getValue()) {
//
//				}
//			}
//		}
//
//		return null;
//	}

	private List<Model> createAllModelsForAHotspot(HotspotDTO hotspot) {
		List<Model> models = new ArrayList<>();

		if (hotspot.getUser() != null) {
			models.add(createUserModel(hotspot));
		}
		models.add(createHotspotModel(hotspot));

		return models;
	}

	private Model createHotspotModel(HotspotDTO hotspot) {
		Model hotspotModel = hotspotsDataset.getNamedModel(valueOf(hotspot.getHotspotId()));

		createHotspotResource(hotspotModel, hotspot);

		return hotspotModel;
	}

	private Resource createHotspotResource(Model hotspotModel, HotspotDTO hotspot) {
		Resource hotspotResource = hotspotModel.createResource(BIMR.getHotspotUri(getUUID()));

		if (hotspot.getUser() != null) {
			checkAndAddPropertyLong(hotspotResource, VCARD.UID, hotspot.getUser().getId());
		}
		checkAndAddPropertyString(hotspotResource, BIMR.id, hotspot.getHotspotId());
		hotspotResource.addProperty(BIMR.observation, createObservationResource(hotspotModel, hotspot));

		return hotspotResource;
	}

	private Model createUserModel(HotspotDTO hotspot) {
		TwitterUserDTO userDTO = hotspot.getUser();
		Model userModel = hotspotsDataset.getNamedModel(getUUID());
		Resource user = userModel.createResource(BIMR.getUserUri(getUUID()));

		checkAndAddPropertyString(user, VCARD.NICKNAME, userDTO.getScreenName());
		checkAndAddPropertyString(user, VCARD.ADR, userDTO.getLocation());
		checkAndAddPropertyString(user, VCARD.NAME, userDTO.getName());
		checkAndAddPropertyLong(user, VCARD.UID, userDTO.getId());
		checkAndAddPropertyString(user, VCARD.EMAIL, userDTO.getEmail());
		checkAndAddPropertyString(user, BIMR.hasGeoEnabled, userDTO.isGeoEnabled());

		return userModel;
	}

	private Resource createObservationResource(Model model, HotspotDTO hotspot) {
		Resource observationResource = model.createResource(BIMR.getObservationUri(getUUID()));

		for (String birdSpecies : hotspot.getBirdSpecies()) {
			checkAndAddPropertyString(observationResource, BIMR.birdSpecies, birdSpecies);
		}
		checkAndAddPropertyInteger(observationResource, BIMR.howMany, hotspot.getHowMany());
		checkAndAddPropertyDate(observationResource, BIMR.date, hotspot.getObservationDate());
		checkAndAddPropertyString(observationResource, BIMR.informationSourceId, hotspot.getInformationSource());
		checkAndAddPropertyLong(observationResource, BIMR.tweetId, hotspot.getTweetId());

		observationResource.addProperty(BIMR.location, createLocationResource(model, hotspot.getLocation()));
		observationResource.addProperty(BIMR.tweet, createTweetResource(model, hotspot));

		return observationResource;
	}

	private Resource createLocationResource(Model model, LocationDTO location) {
		Resource locationResource = model.createResource(BIMR.getLocationUri(getUUID()));

		checkAndAddPropertyDouble(locationResource, BIMR.latitude, location.getGeo().getLeft());
		checkAndAddPropertyDouble(locationResource, BIMR.longitude, location.getGeo().getRight());
		checkAndAddPropertyString(locationResource, BIMR.city, location.getCity());
		checkAndAddPropertyString(locationResource, BIMR.country, location.getCountry());
		checkAndAddPropertyString(locationResource, BIMR.state, location.getState());

		return locationResource;
	}

	private Resource createTweetResource(Model model, HotspotDTO hotspot) {
		Resource tweet = model.createResource(BIMR.getTweetUri(getUUID()));

		checkAndAddPropertyLong(tweet, BIMR.id, hotspot.getTweetId());
		checkAndAddPropertyString(tweet, BIMR.language, GeneralConstants.EN_LANGUAGE);
		checkAndAddPropertyString(tweet, BIMR.text, hotspot.getTweetMessage());
		checkAndAddPropertyString(tweet, BIMR.link, hotspot.getLink());
		checkAndAddPropertyString(tweet, BIMR.author, hotspot.getAuthor());

		return tweet;
	}

	private Model createMigrationModel(MigrationDTO migration) {
		Model migrationModel = migrationDataset.getNamedModel(getUUID());
		Resource migrationResource = migrationModel.createResource(BIMR.getMigrationUri(getUUID()));

		checkAndAddPropertyString(migrationResource, BIMR.id, migration.getMigrationId());
		checkAndAddPropertyString(migrationResource, BIMR.birdSpecies, migration.getSpecies());

		migrationResource.addProperty(BIMR.from, createHotspotResource(migrationModel, migration.getFromHotspot()));
		migrationResource.addProperty(BIMR.to, createHotspotResource(migrationModel, migration.getToHotspot()));

		return migrationModel;
	}

	private void checkAndAddPropertyString(Resource resource, Property property, String value) {
		if (value != null && !value.equals("")) {
			resource.addProperty(property, value);
		}
	}

	private void checkAndAddPropertyDouble(Resource resource, Property property, Double value) {
		if (value != null) {
			resource.addProperty(property, valueOf(value));
		}
	}

	private void checkAndAddPropertyDate(Resource resource, Property property, LocalDateTime value) {
		if (value != null) {
			resource.addProperty(property, valueOf(value), XSDdateTime);
		}
	}

	private void checkAndAddPropertyLong(Resource resource, Property property, Long value) {
		if (value != null) {
			resource.addProperty(property, valueOf(value));
		}
	}

	private void checkAndAddPropertyInteger(Resource resource, Property property, Integer value) {
		if (value != null) {
			resource.addProperty(property, valueOf(value));
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}

}