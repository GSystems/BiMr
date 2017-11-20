package main.java.bf.transformer;

import java.util.ArrayList;
import java.util.List;

import main.java.bfcl.dto.EBirdDataDTO;
import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.EBirdResponseDTO;
import main.java.bfcl.dto.TweetDTO;
import main.java.bfcl.dto.TwitterRequestDTO;
import main.java.bfcl.dto.TwitterResponseDTO;
import main.java.bfcl.dto.TwitterUserDTO;
import main.java.df.model.EBirdData;
import main.java.df.model.EBirdRequest;
import main.java.df.model.EBirdResponse;
import main.java.df.model.Tweet;
import main.java.df.model.TwitterRequest;
import main.java.df.model.TwitterResponse;
import main.java.df.model.TwitterUser;

public class MapTransformer {

	private MapTransformer() {
	}

	public static TwitterRequest twitterRequestFromDTO(TwitterRequestDTO requestDTO) {
		TwitterRequest request = new TwitterRequest(requestDTO.getHashtag());
		return request;
	}

	public static TwitterResponseDTO fromTwitterResponseToDTO(TwitterResponse response) {
		TwitterResponseDTO responseDTO = new TwitterResponseDTO();
		responseDTO.setTweets(fromTweetsWrapperToDTO(response.getTweets()));
		return responseDTO;
	}

	private static List<TweetDTO> fromTweetsWrapperToDTO(List<Tweet> tweets) {
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
		userDTO.setLocation(user.getLocation());
		userDTO.setUsername(user.getUsername());
		userDTO.setScreenName(user.getScreenName());
		userDTO.setUrl(user.getUrl());
		return userDTO;
	}

	public static List<Tweet> toTweetsFromDTO(List<TweetDTO> tweetsDTO) {
		List<Tweet> tweets = new ArrayList<>();
		for (TweetDTO tweetDTO : tweetsDTO) {
			Tweet tweet = new Tweet();
			tweet.setId(tweetDTO.getId());
			tweet.setLatitude(tweetDTO.getLatitude());
			tweet.setLongitude(tweetDTO.getLongitude());
			tweet.setObservationDate(tweetDTO.getObservationDate());
			tweet.setTweetMessage(tweetDTO.getTweetMessage());
			tweet.setUser(toTwitterUserFromDTO(tweetDTO.getUser()));
			tweets.add(tweet);
		}
		return tweets;
	}

	private static TwitterUser toTwitterUserFromDTO(TwitterUserDTO userDTO) {
		TwitterUser user = new TwitterUser();
		user.setEmail(userDTO.getEmail());
		user.setId(userDTO.getId());
		user.setLocation(userDTO.getLocation());
		user.setScreenName(userDTO.getScreenName());
		user.setUrl(userDTO.getUrl());
		user.setUsername(userDTO.getUsername());
		return user;
	}

	public static EBirdRequest toEbirdRequestFromDTO(EBirdRequestDTO requestDTO) {
		EBirdRequest request = new EBirdRequest();
		request.setRequestUriPattern(requestDTO.getRequestUriPattern());
		return request;
	}

	public static EBirdResponseDTO fromEBirdResponseToDTO(EBirdResponse response) {
		EBirdResponseDTO responseDTO = new EBirdResponseDTO();
		responseDTO.seteBirdData(fromEBirdDataWrapperToDTO(response.geteBirdData()));
		return responseDTO;
	}

	private static List<EBirdDataDTO> fromEBirdDataWrapperToDTO(List<EBirdData> ebirdData) {
		List<EBirdDataDTO> ebirdDataDTO = new ArrayList<>();
		for (EBirdData currentData : ebirdData) {
			EBirdDataDTO ebirdDTO = new EBirdDataDTO();
			ebirdDTO.setCommonName(currentData.getCommonName());
			ebirdDTO.setCountryName(currentData.getCountryName());
			ebirdDTO.setLatitude(currentData.getLatitude());
			ebirdDTO.setLocalityName(currentData.getLocalityName());
			ebirdDTO.setLongitude(currentData.getLongitude());
			ebirdDTO.setObservationDate(currentData.getObservationDate());
			ebirdDTO.setScientificName(currentData.getScientificName());
			ebirdDTO.setStateName(currentData.getStateName());
			ebirdDTO.setUserDisplayName(currentData.getUserDisplayName());
			ebirdDataDTO.add(ebirdDTO);
		}
		return ebirdDataDTO;
	}

}
