package bimr.bf;

import bimr.bfcl.EbirdFacade;
import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.dto.EbirdResponseDTO;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author GLK
 */

@Singleton
@Startup
public class EbirdScheduleFacadeBean implements EbirdScheduleFacade {

	@EJB
	private EbirdFacade ebirdFacade;

	@Override
//	@Schedule(minute = "*/10", hour = "*", persistent = false)
	public void ebirdApiCallScheduled() {
//		checkForSave(ebirdFacade.retrieveEbirdHotspotSightingsSummary());
		checkForSave(ebirdFacade.retrieveEbirdNearbyNotableObservations());
//		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsAtHotspots());
//		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsInRegion());
//		checkForSave(ebirdFacade.retrieveEbirdObservationsOfSpeciesAtHotspots());
	}

	private void checkForSave(EbirdResponseDTO response) {
		if (!response.getEbirdData().isEmpty()) {
			ebirdFacade.persistEbirdData(response);
		}
	}
}
