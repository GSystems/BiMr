package bimr.bfcl;

import bimr.bfcl.dto.EbirdRequestDTO;

/**
 * @author GLK
 */
public interface EbirdFacade {

	/**
	 * Retrieves data from ebird API v1.1 and persists them into database
	 *
	 * @param request
	 */
	void retrieveEbirdDataFromApi(EbirdRequestDTO request);

	void retrieveEbirdNotableObservationsInRegion();

	void retrieveEbirdNearbyNotableObservations();

	void retrieveEbirdNotableObservationsAtHotspots();

	void retrieveEbirdObservationsOfSpeciesAtHotspots();

	void retrieveEbirdHotspotSightingsSummary();
}
