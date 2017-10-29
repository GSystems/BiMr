package com.gsys.bimr.bf;

import com.gsys.bimr.bf.dto.TwitterRequestDTO;
import com.gsys.bimr.bf.dto.TwitterResponseDTO;
import com.gsys.bimr.bf.transformer.MapTransformer;
import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.df.MapRepository;

public class MapFacadeBean implements MapFacade {
	
	@Override
	public TwitterResponseDTO retrieveTweets(TwitterRequestDTO request) {
		MapRepository repo = new MapRepository();
		return MapTransformer
				.fromTwitterResponseToDTO(repo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request)));
	}

}
