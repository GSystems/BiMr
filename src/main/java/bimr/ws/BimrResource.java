package bimr.ws;

import bimr.bf.transformer.RdfModelTransformer;
import bimr.bfcl.BimrOntologyFacade;
import bimr.bfcl.TweetScheduleFacade;
import bimr.bfcl.dto.HotspotDTO;
import org.apache.jena.rdf.model.Resource;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/bimr")
public class BimrResource {

	@EJB
	private BimrOntologyFacade bimrOntologyFacade;

	@EJB
	private TweetScheduleFacade tweetScheduleFacade;

	@Path("/getAllHotspots")
	@GET
	@Produces({ MediaType.TEXT_PLAIN})
	public String getAllHotspotsAsTxt() {
		return RdfModelTransformer.fromHotspotsToJsonCollection(tweetScheduleFacade.mockHotspots()).toString();
	}

	@Path("/getAll")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getAllHotspots() {
		return RdfModelTransformer.fromHotspotsToJsonCollection(tweetScheduleFacade.mockHotspots()).toMap();
	}

	@Path("/getAllJ")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<HotspotDTO> getAllHotspotsJson() {
		return bimrOntologyFacade.getAllHotspots();
	}
}