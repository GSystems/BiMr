package bimr.bf.transformer;

import java.util.ArrayList;
import java.util.List;

import bimr.bfcl.dto.*;
import bimr.df.model.*;

/**
 * @author GLK
 */
public class MapTransformer {

	private MapTransformer() {
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
		tweetDTO.setObservationDate(tweet.getObservationDate());
		tweetDTO.setTweetMessage(tweet.getTweetMessage());
		tweetDTO.setUser(fromTwitterUserToDTO(tweet.getUser()));
		return tweetDTO;
	}

	private static TwitterUserDTO fromTwitterUserToDTO(TwitterUser user) {
		TwitterUserDTO userDTO = new TwitterUserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setId(String.valueOf(user.getId()));
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
			tweet.setObservationDate(tweetDTO.getObservationDate());
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

	public static HotspotDTO toHotspotDTOFromTweetDTO(TweetDTO tweetDTO) {
		HotspotDTO hotspotDTO = new HotspotDTO();
		hotspotDTO.setLatitude(tweetDTO.getLatitude());
		hotspotDTO.setLongitude(tweetDTO.getLongitude());
		//TODO save date instead of string
		hotspotDTO.setObservationDate(tweetDTO.getObservationDate().toString());
		hotspotDTO.setTweetMessage(tweetDTO.getTweetMessage());
		//TODO save integer instead of string
		hotspotDTO.setTweetId(tweetDTO.getId().toString());
		return hotspotDTO;
	}
}
