package bimr.bf.transformer;

import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.LocationDTO;
import bimr.bfcl.dto.MigrationDTO;
import bimr.bfcl.dto.TwitterUserDTO;
import bimr.util.QueryConstants;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.time.LocalDateTime;
import java.util.*;

public class BimrRdfTransformer {

	public static List<HotspotDTO> fromRdfToHotspotDTOList(ResultSet resultSet, ImmutableList<String> hotspotParams,
			ImmutableList<String> observationParams, ImmutableList<String> tweetParams,
			ImmutableList<String> locationParams, ImmutableList<String> userParams) {

		List<HotspotDTO> hotspots = new ArrayList<>();
		Map<String, HotspotDTO> hotspotsMap = new HashMap<>();

		while (resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.next();

			HotspotDTO hotspot =
					fromRdfToHotspot(hotspotParams, observationParams, tweetParams, locationParams, userParams,
							querySolution);

			if (!hotspotsMap.containsKey(hotspot.getHotspotId())) {
				hotspotsMap.put(hotspot.getHotspotId(), hotspot);
			}

			if (querySolution.get(observationParams.get(3)) != null) {

				if (hotspotsMap.get(hotspot.getHotspotId()).getBirdSpecies() != null && !hotspotsMap
						.get(hotspot.getHotspotId()).getBirdSpecies()
						.contains(String.valueOf(querySolution.get(observationParams.get(3))))) {

					hotspotsMap.get(hotspot.getHotspotId()).getBirdSpecies()
							.add(String.valueOf(querySolution.get(observationParams.get(3))));
				} else {
					List<String> birdSpecies = new ArrayList<>();
					birdSpecies.add(String.valueOf(querySolution.get(observationParams.get(3))));
					hotspot.setBirdSpecies(birdSpecies);
				}
			}

		}

		for (Map.Entry<String, HotspotDTO> entry : hotspotsMap.entrySet()) {
			hotspots.add(entry.getValue());
		}

		return hotspots;
	}

	private static HotspotDTO fromRdfToHotspot(ImmutableList<String> hotspotParams,
			ImmutableList<String> observationParams, ImmutableList<String> tweetParams,
			ImmutableList<String> locationParams, ImmutableList<String> userParams, QuerySolution querySolution) {

		HotspotDTO hotspot = new HotspotDTO();

		if (querySolution.get(hotspotParams.get(0)) != null) {
			hotspot.setHotspotId(String.valueOf(querySolution.get(hotspotParams.get(0))));
		}
		if (querySolution.get(tweetParams.get(0)) != null) {
			hotspot.setTweetId(Long.parseLong(String.valueOf(querySolution.get(tweetParams.get(0)))));
		}
		if (querySolution.get(tweetParams.get(1)) != null) {
			hotspot.setTweetMessage(String.valueOf(querySolution.get(tweetParams.get(1))));
		}
		if (querySolution.get(tweetParams.get(2)) != null) {
			hotspot.setAuthor(String.valueOf(querySolution.get(tweetParams.get(2))));
		}
		if (querySolution.get(tweetParams.get(3)) != null) {
			hotspot.setLink(String.valueOf(querySolution.get(tweetParams.get(3))));
		}
		if (querySolution.get(tweetParams.get(4)) != null) {
			hotspot.setLanguage(String.valueOf(querySolution.get(tweetParams.get(4))));
		}
		if (querySolution.get(observationParams.get(0)) != null) {
			hotspot.setObservationDate(
					LocalDateTime.parse(querySolution.get(observationParams.get(0)).asLiteral().getValue().toString()));
		}
		if (querySolution.get(observationParams.get(1)) != null) {
			hotspot.setHowMany(Integer.valueOf(String.valueOf(querySolution.get(observationParams.get(1)))));
		}
		if (querySolution.get(observationParams.get(2)) != null) {
			hotspot.setInformationSource(String.valueOf(querySolution.get(observationParams.get(2))));
		}

		hotspot.setLocation(fromRdfToLocationDTO(querySolution, locationParams));

		TwitterUserDTO user = fromRdfToUserDTO(querySolution, userParams);
		if (user.getId() != null) {
			hotspot.setUser(user);
		}
		if (querySolution.get(observationParams.get(3)) != null) {
			List<String> birdSpecies = new ArrayList<>();
			birdSpecies.add(String.valueOf(querySolution.get(observationParams.get(3))));
			hotspot.setBirdSpecies(birdSpecies);
		}

		return hotspot;
	}

	private static LocationDTO fromRdfToLocationDTO(QuerySolution querySolution, ImmutableList<String> locationParams) {
		LocationDTO location = new LocationDTO();

		Double latitude = 0D;
		Double longitude = 0D;

		if (querySolution.get(locationParams.get(0)) != null) {
			latitude = Double.valueOf(String.valueOf(querySolution.get(locationParams.get(0))));
		}
		if (querySolution.get(locationParams.get(1)) != null) {
			longitude = Double.valueOf(String.valueOf(querySolution.get(locationParams.get(1))));
		}
		if (querySolution.get(locationParams.get(2)) != null) {
			location.setCountry(String.valueOf(querySolution.get(locationParams.get(2))));
		}
		if (querySolution.get(locationParams.get(3)) != null) {
			location.setCity(String.valueOf(querySolution.get(locationParams.get(3))));
		}
		if (querySolution.get(locationParams.get(4)) != null) {
			location.setState(String.valueOf(querySolution.get(locationParams.get(4))));
		}
		location.setGeo(Pair.of(latitude, longitude));

		return location;
	}

	private static TwitterUserDTO fromRdfToUserDTO(QuerySolution querySolution, ImmutableList<String> userParams) {
		TwitterUserDTO user = new TwitterUserDTO();

		if (querySolution.get(userParams.get(0)) != null) {
			user.setId(Long.parseLong(String.valueOf(querySolution.get(userParams.get(0)))));
		}
		if (querySolution.get(userParams.get(1)) != null) {
			user.setEmail(String.valueOf(querySolution.get(userParams.get(1))));
		}
		if (querySolution.get(userParams.get(2)) != null) {
			user.setLocation(String.valueOf(querySolution.get(userParams.get(2))));
		}
		if (querySolution.get(userParams.get(3)) != null) {
			user.setName(String.valueOf(querySolution.get(userParams.get(3))));
		}
		if (querySolution.get(userParams.get(4)) != null) {
			user.setScreenName(String.valueOf(querySolution.get(userParams.get(4))));
		}
		if (querySolution.get(userParams.get(5)) != null) {
			user.setIsGeoEnabled(String.valueOf(querySolution.get(userParams.get(5))));
		}

		return user;
	}

	public static List<MigrationDTO> fromRdfToMigrationDTOList(ResultSet resultSet) {
		List<MigrationDTO> migrations = new ArrayList<>();

		while (resultSet.hasNext()) {
			MigrationDTO migration = new MigrationDTO();
			QuerySolution querySolution = resultSet.next();

			migration.setMigrationId(String.valueOf(querySolution.get(QueryConstants.migrationConstants.get(0))));
			migration.setSpecies(String.valueOf(querySolution.get(QueryConstants.fromObservationConstants.get(3))));

			migration.setFromHotspot(
					fromRdfToHotspot(QueryConstants.fromHotspotConstants, QueryConstants.fromObservationConstants,
							QueryConstants.fromTweetConstants, QueryConstants.fromLocationConstants,
							QueryConstants.fromUserConstants, querySolution));

			migration.setToHotspot(
					fromRdfToHotspot(QueryConstants.toHotspotConstants, QueryConstants.toObservationConstants,
							QueryConstants.toTweetConstants, QueryConstants.toLocationConstants,
							QueryConstants.toUserConstants, querySolution));

			migrations.add(migration);
		}
		return migrations;
	}
}
