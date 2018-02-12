package bimr.bf.transformer;

import bimr.bfcl.dto.*;
import bimr.df.model.*;
import bimr.util.GeneralConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author GLK
 */
public class MapTransformer {

	private MapTransformer() {
	}

	public static List<MigrationDTO> fromHotspotsMapToMigrationsList(Map<String, List<HotspotDTO>> hotspotMap) {
		List<MigrationDTO> migrations = new ArrayList<>();

		for (Map.Entry<String, List<HotspotDTO>> hotspotEntry : hotspotMap.entrySet()) {

			Collections.sort(hotspotEntry.getValue());
			MigrationDTO migration = new MigrationDTO();

			for (int i = 0; i < hotspotEntry.getValue().size() - 1; i ++) {
				migration = fromHotspotToMigration(hotspotEntry.getValue().get(i), hotspotEntry.getValue().get(i + 1),
						hotspotEntry.getKey());
				migrations.add(migration);
			}
		}

		return migrations;
	}

	private static MigrationDTO fromHotspotToMigration(HotspotDTO fromHotspot, HotspotDTO toHotspot, String species) {
		MigrationDTO migration = new MigrationDTO();

		migration.setSpecies(species);
		migration.setToHotspot(toHotspot);
		migration.setFromHotspot(fromHotspot);

		return migration;
	}

	public static TwitterRequest twitterRequestFromDTO(TweetRequestDTO requestDTO) {
		return new TwitterRequest(requestDTO.getHashtag(), requestDTO.getLastTweetId(), requestDTO.getUntilDate());
	}

	public static TweetResponseDTO fromTwitterResponseToDTO(TwitterResponse response) {
		TweetResponseDTO responseDTO = new TweetResponseDTO();
		responseDTO.setTweets(fromTweetsToDTO(response.getTweets()));
		return responseDTO;
	}

	public static List<TweetDTO> fromTweetsToDTO(List<Tweet> tweets) {
		List<TweetDTO> tweetsDTO = new ArrayList<>();
		for (Tweet tweet : tweets) {
			TweetDTO tweetDTO = fromTweetToDTO(tweet);
			tweetsDTO.add(tweetDTO);
		}
		return tweetsDTO;
	}

	private static TweetDTO fromTweetToDTO(Tweet tweet) {
		TweetDTO tweetDTO = new TweetDTO();
		tweetDTO.setId(tweet.getId());
		tweetDTO.setTweetId(tweet.getTweetId());
		tweetDTO.setLatitude(tweet.getLatitude());
		tweetDTO.setLongitude(tweet.getLongitude());
//		tweetDTO.setObservationDate(tweet.getObservationDate());
		tweetDTO.setTweetMessage(tweet.getTweetMessage());
		if (tweet.getUser() != null) {
			tweetDTO.setUser(fromTwitterUserToDTO(tweet.getUser()));
		}
		return tweetDTO;
	}

	private static TwitterUserDTO fromTwitterUserToDTO(TwitterUser user) {
		TwitterUserDTO userDTO = new TwitterUserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setId(user.getId());
		userDTO.setIsGeoEnabled(String.valueOf(user.isGeoEnabled()));
		userDTO.setLocation(user.getLocation());
		userDTO.setName(user.getName());
		userDTO.setScreenName(user.getScreenName());
		return userDTO;
	}

	public static List<Tweet> toTweetsFromDTO(List<TweetDTO> tweetsDTO) {
		List<Tweet> tweets = new ArrayList<>();
		for (TweetDTO tweetDTO : tweetsDTO) {
			Tweet tweet = new Tweet();
			tweet.setId(tweetDTO.getId());
			tweet.setTweetId(tweetDTO.getTweetId());
			tweet.setLatitude(tweetDTO.getLatitude());
			tweet.setLongitude(tweetDTO.getLongitude());
//			tweet.setObservationDate(tweetDTO.getObservationDate());
			tweet.setTweetMessage(tweetDTO.getTweetMessage());
			tweets.add(tweet);
		}
		return tweets;
	}

	public static EbirdRequest toEbirdRequestFromDTO(EbirdRequestDTO requestDTO) {
		EbirdRequest request = new EbirdRequest();
		request.setRequestUriPattern(requestDTO.getRequestUriPattern());
		return request;
	}

	public static EbirdResponseDTO fromEbirdResponseToDTO(EbirdResponse response) {
		EbirdResponseDTO responseDTO = new EbirdResponseDTO();
		if (response.getEbirdData() != null) {
			responseDTO.setEbirdData(fromEbirdDataListToDTO(response.getEbirdData()));
		}
		return responseDTO;
	}

	private static List<EbirdDataDTO> fromEbirdDataListToDTO(List<EbirdData> ebirdDataList) {
		List<EbirdDataDTO> ebirdDataListDTO = new ArrayList<>();
		for (EbirdData ebirdData : ebirdDataList) {
			EbirdDataDTO ebirdDTO = new EbirdDataDTO();
			ebirdDTO.setId(ebirdData.getId());
			ebirdDTO.setLatitude(ebirdData.getLatitude());
			ebirdDTO.setLongitude(ebirdData.getLongitude());
			ebirdDTO.setObservationDate(ebirdData.getObservationDate());
			ebirdDTO.setScientificName(ebirdData.getScientificName());
			ebirdDTO.setUserDisplayName(ebirdData.getUserDisplayName());
			ebirdDataListDTO.add(ebirdDTO);
		}
		return ebirdDataListDTO;
	}

	public static EbirdResponse toEbirdResponseFromDTO(EbirdResponseDTO responseDTO) {
		EbirdResponse response = new EbirdResponse();
		List<EbirdData> ebirdDataList = new ArrayList<>();
		for (EbirdDataDTO ebirdDataDTO : responseDTO.getEbirdData()) {
			EbirdData ebirdData = new EbirdData();
			ebirdData.setId(ebirdDataDTO.getId());
			ebirdData.setLatitude(ebirdDataDTO.getLatitude());
			ebirdData.setLongitude(ebirdDataDTO.getLongitude());
			ebirdData.setObservationDate(ebirdDataDTO.getObservationDate());
			ebirdData.setScientificName(ebirdDataDTO.getScientificName());
			ebirdData.setUserDisplayName(ebirdDataDTO.getUserDisplayName());
			ebirdDataList.add(ebirdData);
		}
		response.setEbirdData(ebirdDataList);
		return response;
	}

	public static HotspotDTO addInfoFromTweetToHotspot(TweetDTO tweetDTO, HotspotDTO hotspot) {
		hotspot.setHowMany(1);
		hotspot.setObservationDate(tweetDTO.getObservationDate());
		hotspot.setTweetMessage(tweetDTO.getTweetMessage());
		hotspot.setTweetId(tweetDTO.getTweetId());
		hotspot.setInformationSource(GeneralConstants.TWITTER_SOURCE);
		if (tweetDTO.getUser() != null) {
			hotspot.setUser(tweetDTO.getUser());
		}
		return hotspot;
	}
}
