package main.java.bfcl;

import main.java.bfcl.dto.EBirdRequestDTO;
import main.java.bfcl.dto.EBirdResponseDTO;

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
