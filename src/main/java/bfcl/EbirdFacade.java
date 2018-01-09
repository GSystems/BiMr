package main.java.bfcl;

import main.java.bfcl.dto.EbirdRequestDTO;
import main.java.bfcl.dto.EbirdResponseDTO;

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
