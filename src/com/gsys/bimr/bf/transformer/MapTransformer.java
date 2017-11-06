package com.gsys.bimr.bf.transformer;

import java.util.HashMap;
import java.util.Map;

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

	private static Map<String, TwitterDataDTO> fromTwitterDataWrapperToDTO(Map<String, TwitterData> tweets) {
		Map<String, TwitterDataDTO> tweetsDTO = new HashMap<>();
		for (Map.Entry<String, TwitterData> entry : tweets.entrySet()) {
			TwitterData tweet = entry.getValue();
			tweetsDTO.put(entry.getKey(), new TwitterDataDTO(tweet.getUser(), tweet.getLocation()));
		}
		return tweetsDTO;
	}

	public static TwitterRequest twitterRequestFromDTO(TwitterRequestDTO requestDTO) {
		TwitterRequest request = new TwitterRequest();
		request.setHashtags(requestDTO.getHashtags());
		return request;
	}

}
