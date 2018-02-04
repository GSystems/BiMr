package bimr.ws;

import bimr.bf.transformer.RdfModelTransformer;
import bimr.bfcl.BimrOntologyFacade;
import bimr.bfcl.TweetScheduleFacade;
import org.json.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/bimr")
public class BimrResource {

	@EJB
	private BimrOntologyFacade bimrOntologyFacade;

	@EJB
	private TweetScheduleFacade tweetScheduleFacade;

	@Path("/getAllHotspots")
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public JSONObject getAllHotspots() {
//		List<HotspotDTO> hotspots = bimrOntologyFacade.getAllHotspots();
		return RdfModelTransformer.fromHotspotsToJsonCollection(tweetScheduleFacade.mockHotspots());
	}
}