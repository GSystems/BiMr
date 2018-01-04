package bimr.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.EbirdFacade;
import bimr.bfcl.dto.EBirdRequestDTO;
import bimr.bfcl.dto.EBirdResponseDTO;
import bimr.df.EbirdRepo;
import bimr.util.AsyncUtils;

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
		return MapTransformer.fromEBirdResponseToDTO(AsyncUtils
				.getResultFromAsyncTask(repo.retrieveEBirdData(MapTransformer.toEbirdRequestFromDTO(request))));
	}

}
