package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import org.apache.jena.rdf.model.Model;

import java.util.List;

public interface BimrOntologyFacade {

	List<Model> generateHotspotModels(List<HotspotDTO> filteredTweets);

	void persistHotspotModels(List<Model> models);

	List<HotspotDTO> getAllHotspots();
}
