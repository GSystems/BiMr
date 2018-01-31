package bimr.bfcl;

import bimr.bfcl.dto.EbirdResponseDTO;

/**
 * @author GLK
 */
public interface EbirdFacade {

	/**
	 * Insert a list of ebird data into the database
	 *
	 * @param response
	 */
	void persistEbirdData(EbirdResponseDTO response);

	EbirdResponseDTO retrieveEbirdNotableObservationsInRegion();

	EbirdResponseDTO retrieveEbirdNearbyNotableObservations();

	EbirdResponseDTO retrieveEbirdNotableObservationsAtHotspots();

	EbirdResponseDTO retrieveEbirdObservationsOfSpeciesAtHotspots();

	EbirdResponseDTO retrieveEbirdHotspotSightingsSummary();
}
