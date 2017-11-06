package com.gsys.bimr.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.gsys.bimr.bf.transformer.MapTransformer;
import com.gsys.bimr.bfcl.MapFacade;
import com.gsys.bimr.bfcl.dto.EBirdRequestDTO;
import com.gsys.bimr.bfcl.dto.EBirdResponseDTO;
import com.gsys.bimr.bfcl.dto.TwitterRequestDTO;
import com.gsys.bimr.bfcl.dto.TwitterResponseDTO;
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

	@Override
	public EBirdResponseDTO retrieveEBirdData(EBirdRequestDTO request) {
		// TODO Auto-generated method stub
		return null;
	}

}
