package main.java.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import main.java.bf.transformer.MapTransformer;
import main.java.bfcl.EbirdFacade;
import main.java.bfcl.dto.EbirdRequestDTO;
import main.java.bfcl.dto.EbirdResponseDTO;
import main.java.df.EbirdRepo;
import main.java.util.AsyncUtils;

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
