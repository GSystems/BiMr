package bimr.bf;

import bimr.bfcl.EbirdFacade;
import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.dto.EbirdResponseDTO;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * @author GLK
 */

@Singleton
public class EbirdScheduleFacadeBean implements EbirdScheduleFacade {

	@Inject
	private EbirdFacade ebirdFacade;

	@Override
	@Schedule(second = "*", minute = "*", hour = "*/24", persistent = false)
	public void ebirdApiCallScheduled() {
		checkForSave(ebirdFacade.retrieveEbirdHotspotSightingsSummary());
		checkForSave(ebirdFacade.retrieveEbirdNearbyNotableObservations());
		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsAtHotspots());
		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsInRegion());
		checkForSave(ebirdFacade.retrieveEbirdObservationsOfSpeciesAtHotspots());
	}

	private void checkForSave(EbirdResponseDTO response) {
		if (response != null) {
			ebirdFacade.persistEbirdData(response);
		}
	}
}
