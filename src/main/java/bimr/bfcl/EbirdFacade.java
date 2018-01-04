package bimr.bfcl;

import bimr.bfcl.dto.EBirdRequestDTO;
import bimr.bfcl.dto.EBirdResponseDTO;

/**
 * @author GLK
 */
public interface EbirdFacade {

	/**
	 * Retrieve data from eBirds API
	 * 
	 * @param request
	 * @return
	 */
	EBirdResponseDTO retrieveEBirdData(EBirdRequestDTO request);

}
