package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;

import java.util.List;

public interface RdfFacade {
	void generateRdfModel(List<HotspotDTO> filteredTweets);
}
