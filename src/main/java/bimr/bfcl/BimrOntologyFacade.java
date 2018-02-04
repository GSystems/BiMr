package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public interface BimrOntologyFacade {

	Map<String, List<Model>> createModelsForHotspots(List<HotspotDTO> filteredTweets);

	void persistAllModelsForAHotspot(List<Model> models);

	void printAllModelsForAHotspot(List<Model> models);

	List<HotspotDTO> getAllHotspots();
}
