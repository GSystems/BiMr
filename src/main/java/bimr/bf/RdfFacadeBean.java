package bimr.bf;

import bimr.bfcl.RdfFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.util.GeneralConstants;
import bimr.util.RdfEnum;
import bimr.util.rdf.ontology.Bisp;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RdfFacadeBean implements RdfFacade {

	@Override
	public void generateRdfModel(List<HotspotDTO> tweets) {
		Model model = ModelFactory.createDefaultModel();

		for (HotspotDTO hotspotDTO : tweets) {

			Map<Property, String> generalProperties = addGeneralProperties(hotspotDTO);

			Map<Property, String> locationProperties = addLocationProperties(hotspotDTO);

			Map<Property, String> tweetProperties = addTweetProperties(hotspotDTO);

			Map<Property, String> userProperties = addUserProperties(hotspotDTO);

			Resource hotspotResource = model.createResource(RdfEnum.BASE_URI.getCode() + "hotspot1");

			Resource location = model.createResource(RdfEnum.LOCATION_URI.getCode());
			addProperties(locationProperties, location);
			hotspotResource.addProperty(Bisp.location, location);

			Resource tweet = model.createResource(RdfEnum.TWEET_URI.getCode());
			addProperties(tweetProperties, tweet);
			hotspotResource.addProperty(Bisp.tweet, tweet);

			Resource user = model.createResource(RdfEnum.USER_URI.getCode());
			addProperties(userProperties, user);
			hotspotResource.addProperty(Bisp.user, user);

			addProperties(generalProperties, hotspotResource);

			replaceTagNames(model);

			writeRdfModelInFile(model);
		}
	}

	private void writeRdfModelInFile(Model model) {
		model.write(System.out, "N-TRIPLES");
		try {
			PrintWriter writer = new PrintWriter(RdfEnum.FILENAME.getCode(), GeneralConstants.UTF8);
			model.write(writer, RdfEnum.RDF_XML_FORMAT.getCode());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void replaceTagNames(Model model) {
		model.setNsPrefix("location", "http://xmlns.com/bisp/location#");
		model.setNsPrefix("hotspot", "http://xmlns.com/bisp/");
		model.setNsPrefix("tweet", "http://xmlns.com/bisp/tweet#");
		model.setNsPrefix("geo", "http://xmlns.com/bisp/geo#");
		model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
	}

	private Map<Property, String> addUserProperties(HotspotDTO hotspotDTO) {
		Map<Property, String> userProperties = new HashMap<>();
		userProperties.put(FOAF.accountName, hotspotDTO.getUser().getScreenName());
		userProperties.put(FOAF.name, hotspotDTO.getUser().getName());
		//			userProperties.put(FOAF., hotspotDTO.getUser().getId());
		//			userProperties.put(FOAF. , hotspotDTO.getUser().getEmail());
		//			userProperties.put(FOAF. , hotspotDTO.getUser().getLocation());
		return userProperties;
	}

	private Map<Property, String> addTweetProperties(HotspotDTO hotspotDTO) {
		Map<Property, String> tweetProperties = new HashMap<>();
		tweetProperties.put(Bisp.id, hotspotDTO.getTweetId());
		tweetProperties.put(Bisp.language, GeneralConstants.EN_LANGUAGE);
		tweetProperties.put(Bisp.text, hotspotDTO.getTweetMessage());
		return tweetProperties;
	}

	private Map<Property, String> addLocationProperties(HotspotDTO hotspotDTO) {
		Map<Property, String> locationProperties = new HashMap<>();
		if (hotspotDTO.getLatitude() != null) {
			locationProperties.put(Bisp.latitude, hotspotDTO.getLatitude());
			locationProperties.put(Bisp.longitude, hotspotDTO.getLongitude());
		}
		if(hotspotDTO.getLocationName() != null) {
			locationProperties.put(Bisp.name, hotspotDTO.getLocationName());
		}
		return locationProperties;
	}

	private Map<Property, String> addGeneralProperties(HotspotDTO hotspotDTO) {
		Map<Property, String> generalProperties = new HashMap<>();
		generalProperties.put(Bisp.informationSourceId, GeneralConstants.TWITTER_SOURCE);
		generalProperties.put(Bisp.birdSpecies, hotspotDTO.getBirdSpecies());
		generalProperties.put(Bisp.observationDate, hotspotDTO.getObservationDate());
		if (hotspotDTO.getHowMany() != null) {
			generalProperties.put(Bisp.howMany, hotspotDTO.getHowMany());
		}
		return generalProperties;
	}

	private Resource addProperties(Map<Property, String> properties, Resource resource) {
		for (Map.Entry<Property, String> entry : properties.entrySet()) {
			resource.addProperty(entry.getKey(), entry.getValue());
		}
		return resource;
	}
}



//			hotspotResource.addProperty(Bisp.informationSourceId, GeneralConstants.TWITTER_SOURCE)
//					.addProperty(Bisp.birdSpecies, hotspotDTO.getBirdSpecies())
//					.addProperty(Bisp.howMany, hotspotDTO.getHowMany())
//					.addProperty(Bisp.observationDate, hotspotDTO.getHowMany()).addProperty(
//					Bisp.location, model.createResource("http://xmlns.com/bisp/location")
//							.addProperty(Bisp.latitude, hotspotDTO.getLatitude())
//							.addProperty(Bisp.longitude, hotspotDTO.getLongitude()))
//					.addProperty(Bisp.informationSourceId, "twitter")
//					.addProperty(Bisp.tweet,
//					model.createResource(RdfEnum.BASE_URI.getCode() + "tweet")
//							.addProperty(Bisp.id, hotspotDTO.getTweetId())
//							.addProperty(Bisp.author, "@SomeUSer")
//							.addProperty(Bisp.language, GeneralConstants.EN_LANGUAGE)
//							.addProperty(Bisp.text, hotspotDTO.getTweetMessage())
//							.addProperty(Bisp.link, "")
//							.addProperty(Bisp.user,
//			 						model.createResource(RdfEnum.BASE_URI.getCode() + "tweet#user")
//											.addProperty(FOAF.accountName, "Lester Daniel")
//											.addProperty(FOAF.name, "Lester Daniel")
//											.addProperty(FOAF.firstName, "Lester")
//											.addProperty(FOAF.lastName, "Daniel")
//											.addProperty(FOAF.gender, "M")));