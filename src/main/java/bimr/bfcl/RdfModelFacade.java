package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import org.apache.jena.rdf.model.Model;

import java.util.List;

public interface RdfModelFacade {
	void generateRdfModel(List<HotspotDTO> filteredTweets);

	void persistModel(Model model);
}
