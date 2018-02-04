package bimr.bf.transformer;

import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class OntologyTransformer {

	public static List<HotspotDTO> fromRdfToHotspotDTOList(ResultSet resultSet) {
		List<HotspotDTO> hotspots = new ArrayList<>();

		while (resultSet.hasNext()) {
			HotspotDTO hotspot = new HotspotDTO();

			QuerySolution querySolution = resultSet.next();

			hotspot.setTweetId(String.valueOf(querySolution.get("tweetId")));
			hotspot.setTweetMessage(String.valueOf(querySolution.get("tweetMessage")));
			hotspot.setObservationDate(String.valueOf(querySolution.get("http://xmlns.com/hotspot#date")));
			hotspot.setLongitude(String.valueOf(querySolution.get("longitude")));
			hotspot.setLatitude(String.valueOf(querySolution.get("latitude")));
			hotspot.setAuthor(String.valueOf(querySolution.get("author")));
			hotspot.setCountry(String.valueOf(querySolution.get("country")));
			hotspot.setHowMany(String.valueOf(querySolution.get("howMany")));
			hotspot.setInformationSourceId(String.valueOf(querySolution.get("http://xmlns.com/hotspot#informationSourceId")));
			hotspot.setLink(String.valueOf(querySolution.get("link")));
			hotspot.setCity(String.valueOf(querySolution.get("city")));
			hotspot.setState(String.valueOf(querySolution.get("state")));
			hotspot.setUser(fromRdfToUserDTO(querySolution));
			hotspot.setBirdSpecies(fromRdfToBirdSpecies(querySolution));

			hotspots.add(hotspot);
		}

		return hotspots;
	}

	private static TwitterUserDTO fromRdfToUserDTO(QuerySolution querySolution) {
		TwitterUserDTO user = new TwitterUserDTO();

		user.setEmail(String.valueOf(querySolution.get("EMAIL")));
		user.setId(String.valueOf(querySolution.get("UID")));
		user.setIsGeoEnabled(String.valueOf(querySolution.get("GEO")));
		user.setLocation(String.valueOf(querySolution.get("ADR")));
		user.setName(String.valueOf(querySolution.get("NAME")));
		user.setScreenName(String.valueOf(querySolution.get("NICKNAME")));

		return user;
	}

	// TODO fix this issue (there are more than one bird species)
	private static List<String> fromRdfToBirdSpecies(QuerySolution querySolution) {
		List<String> birdSpecies = new ArrayList<>();
		birdSpecies.add(String.valueOf(querySolution.get("<http://xmlns.com/hotspot#birdSpecies>")));
		return birdSpecies;
	}
}
