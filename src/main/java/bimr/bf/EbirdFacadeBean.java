package bimr.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.EbirdFacade;
import bimr.bfcl.dto.EbirdRequestDTO;
import bimr.bfcl.dto.EbirdResponseDTO;
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
	public EbirdResponseDTO retrieveEbirdDataFromApi(EbirdRequestDTO request) {
		return MapTransformer.fromEBirdResponseToDTO(AsyncUtils
				.getResultFromAsyncTask(repo.retrieveEBirdData(MapTransformer.toEbirdRequestFromDTO(request))));
	}

}
