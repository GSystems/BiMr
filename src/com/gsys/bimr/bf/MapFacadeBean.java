package com.gsys.bimr.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.gsys.bimr.bf.dto.TwitterRequestDTO;
import com.gsys.bimr.bf.dto.TwitterResponseDTO;
import com.gsys.bimr.bf.transformer.MapTransformer;
import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.df.MapRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MapFacadeBean implements MapFacade {

	@Inject
	private MapRepository repo;

	@Override
	public TwitterResponseDTO retrieveTweets(TwitterRequestDTO request) {
		return MapTransformer
				.fromTwitterResponseToDTO(repo.retrieveTweets(MapTransformer.twitterRequestFromDTO(request)));
	}

}
