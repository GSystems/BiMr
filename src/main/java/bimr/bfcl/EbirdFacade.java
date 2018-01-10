package bimr.bfcl;

import bimr.bfcl.dto.EbirdRequestDTO;
import bimr.bfcl.dto.EbirdResponseDTO;

/**
 * @author GLK
 */
public interface EbirdFacade {

	/**
	 * Retrieves data from ebird API v1.1 and persists them into database
	 *
	 * @param request
	 */
	EbirdResponseDTO retrieveEbirdDataFromApi(EbirdRequestDTO request);

}
