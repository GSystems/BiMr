package com.gsys.bimr.tests;

import org.junit.Test;

import com.gsys.bimr.bf.transformer.MapTransformer;
import com.gsys.bimr.bfcl.dto.TwitterDataDTO;
import com.gsys.bimr.bfcl.dto.TwitterResponseDTO;
import com.gsys.bimr.df.model.TwitterData;
import com.gsys.bimr.df.model.TwitterResponse;
import twitter4j.GeoLocation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class MapTransformerUnitTesting {

	@Test
	public void testFromTwitterDataWrapperToDTO() {

		TwitterResponseDTO responseDTO;
		List<TwitterDataDTO> expectedResult = new ArrayList<TwitterDataDTO>();

		List<TwitterData> tweets = new ArrayList<TwitterData>();
		for (int i = 1; i <= 5; i++) {
			TwitterData data = new TwitterData();
			TwitterDataDTO dataDTO = new TwitterDataDTO();
			GeoLocation loc = new GeoLocation(i, -i);

			data.setUser("User " + i);
			data.setLocation(loc);
			data.setMessage("Message" + i);
			tweets.add(data);

			dataDTO.setUser("User " + i);
			dataDTO.setLocation(loc);
			dataDTO.setMessage("Message " + i);
			expectedResult.add(dataDTO);
		}

		TwitterResponse response = new TwitterResponse();
		response.setTweets(tweets);

		responseDTO = MapTransformer.fromTwitterResponseToDTO(response);
		List<TwitterDataDTO> twitterDataTDO = responseDTO.getTweets();

		assertEquals(expectedResult, twitterDataTDO);

	}
}
