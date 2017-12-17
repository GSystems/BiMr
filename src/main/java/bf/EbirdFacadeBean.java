package main.java.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.EbirdFacade;
import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.EBirdResponseDTO;
import main.java.df.EbirdRepo;

/**
 * @author GLK
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class EbirdFacadeBean implements EbirdFacade {

	@Inject
	private EbirdRepo repo;

	@Override
	public EBirdResponseDTO retrieveEBirdData(EBirdRequestDTO request) {
		return MapTransformer
				.fromEBirdResponseToDTO(repo.retrieveEBirdData(MapTransformer.toEbirdRequestFromDTO(request)));
	}

}
