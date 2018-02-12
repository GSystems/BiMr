package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.rdf.model.Model;

import java.util.List;
import java.util.Map;

public interface BimrOntologyPersistFacade {

	Map<String, List<Model>> createModelsForHotspot(List<HotspotDTO> filteredTweets);

	void persistAllModelsForAHotspot(Model model);

	void persistMigrationModel(Model model);

	void printModel(Model model);

	void checkAndSaveMigrations();
}