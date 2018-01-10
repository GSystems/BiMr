package bimr.bf;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import bimr.bf.transformer.MapTransformer;
import bimr.bfcl.EbirdFacade;
import bimr.bfcl.dto.EbirdRequestDTO;
import bimr.bfcl.dto.EbirdResponseDTO;
import bimr.df.EbirdRepo;
import bimr.util.AsyncUtils;
import bimr.util.EbirdsEnum;

/**
 * @author GLK
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class EbirdFacadeBean implements EbirdFacade {

	@Inject
	private EbirdRepo repo;

	@Override
	public void retrieveEbirdDataFromApi(EbirdRequestDTO request) {
		EbirdResponseDTO response = MapTransformer.fromEBirdResponseToDTO(AsyncUtils
				.getResultFromAsyncTask(repo.retrieveEBirdData(MapTransformer.toEbirdRequestFromDTO(request))));
		if (response != null) {
			persistData(response);
		}
	}

	private void persistData(EbirdResponseDTO response) {
		repo.insertEbirdData(MapTransformer.toEbirdResponseFromDTO(response).getEbirdData());
	}

	@Override
	public void retrieveEbirdNotableObservationsInRegion() {
		retrieveEbirdDataFromApi(getRequest(EbirdsEnum.RECENT_NOTABLE_OBSERVATIONS_IN_REGION.name()));
	}

	@Override
	public void retrieveEbirdNearbyNotableObservations() {
		retrieveEbirdDataFromApi(getRequest(EbirdsEnum.RECENT_NEARBY_NOTABLE_OBSERVATIONS.name()));
	}

	@Override
	public void retrieveEbirdNotableObservationsAtHotspots() {
		retrieveEbirdDataFromApi(getRequest(EbirdsEnum.RECENT_NOTABLE_OBSERVATIONS_AT_HOTSPOTS.name()));
	}

	@Override
	public void retrieveEbirdObservationsOfSpeciesAtHotspots() {
		retrieveEbirdDataFromApi(getRequest(EbirdsEnum.RECENT_OBSERVATIONS_OF_SPECIES_AT_HOTSPOTS.name()));
	}

	@Override
	public void retrieveEbirdHotspotSightingsSummary() {
		retrieveEbirdDataFromApi(getRequest(EbirdsEnum.HOTSPOT_SIGHTINGS_SUMMARY_API_REQUEST_URI.name()));
	}

	private EbirdRequestDTO getRequest(String uri) {
		EbirdRequestDTO request = new EbirdRequestDTO();
		request.setRequestUriPattern(uri);
		return request;
	}

}
