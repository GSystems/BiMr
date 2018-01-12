package bimr.bf;

import bimr.bfcl.EbirdFacade;
import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.dto.EbirdResponseDTO;

import javax.ejb.*;

/**
 * @author GLK
 */

@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Lock(LockType.READ)
public class EbirdScheduleFacadeBean implements EbirdScheduleFacade {

	@EJB
	private EbirdFacade ebirdFacade;

	@Override
	@Schedule(second = "*", minute = "*", hour = "*/1", persistent = false)
	public void ebirdApiCallScheduled() {
//		checkForSave(ebirdFacade.retrieveEbirdHotspotSightingsSummary());
		checkForSave(ebirdFacade.retrieveEbirdNearbyNotableObservations());
//		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsAtHotspots());
//		checkForSave(ebirdFacade.retrieveEbirdNotableObservationsInRegion());
//		checkForSave(ebirdFacade.retrieveEbirdObservationsOfSpeciesAtHotspots());
	}

	private void checkForSave(EbirdResponseDTO response) {
		if (response != null) {
			ebirdFacade.persistEbirdData(response);
		}
	}
}
