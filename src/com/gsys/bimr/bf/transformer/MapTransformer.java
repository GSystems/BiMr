package com.gsys.bimr.bf.transformer;

import java.util.ArrayList;
import java.util.List;

import com.gsys.bimr.bfcl.dto.TwitterDataDTO;
import com.gsys.bimr.bfcl.dto.TwitterRequestDTO;
import com.gsys.bimr.bfcl.dto.TwitterResponseDTO;
import com.gsys.bimr.df.model.TwitterData;
import com.gsys.bimr.df.model.TwitterRequest;
import com.gsys.bimr.df.model.TwitterResponse;

public class MapTransformer {

	private MapTransformer() {
	}

	public static TwitterResponseDTO fromTwitterResponseToDTO(TwitterResponse response) {
		TwitterResponseDTO responseDTO = new TwitterResponseDTO();
		responseDTO.setTweets(fromTwitterDataWrapperToDTO(response.getTweets()));
		return responseDTO;
	}

	private static List<TwitterDataDTO> fromTwitterDataWrapperToDTO(List<TwitterData> tweets) {
		List<TwitterDataDTO> tweetsDTO = new ArrayList<>();
		for (TwitterData tweet : tweets) {
			TwitterDataDTO tweetDTO = new TwitterDataDTO();
			tweetDTO.setLocation(tweet.getLocation());
			tweetDTO.setMessage(tweet.getMessage());
			tweetDTO.setUser(tweet.getUser());
			tweetsDTO.add(tweetDTO);
		}
		return tweetsDTO;
	}

	public static TwitterRequest twitterRequestFromDTO(TwitterRequestDTO requestDTO) {
		TwitterRequest request = new TwitterRequest();
		request.setHashtag(requestDTO.getHashtag());
		return request;
	}

}
