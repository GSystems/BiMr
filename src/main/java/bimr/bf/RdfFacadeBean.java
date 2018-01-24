package bimr.bf;

import bimr.bfcl.RdfFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.util.GeneralConstants;
import bimr.util.RdfEnum;
import bimr.util.rdf.ontology.Bisp;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;

import javax.ejb.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

@Singleton
public class RdfFacadeBean implements RdfFacade {

	@Override
	public void generateRdfModel(List<HotspotDTO> hotspots) {
		int id = 0;
		for (HotspotDTO hotspotDTO : hotspots) {
			Model model = ModelFactory.createDefaultModel();
			createResources(model, hotspotDTO, id);
			id++;
			writeRdfModelInFile(model);
		}
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

	private void replaceTagNames(Model model) {
		model.setNsPrefix("hotspot", RdfEnum.BASE_URI.getCode());
		model.setNsPrefix("location", RdfEnum.LOCATION_URI.getCode());
		model.setNsPrefix("tweet", RdfEnum.TWEET_URI.getCode());
		model.setNsPrefix("observation", RdfEnum.OBSERVATION_URI.getCode());
	}
}