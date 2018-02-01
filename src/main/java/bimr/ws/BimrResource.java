package bimr.ws;

import bimr.bfcl.BimrOntologyFacade;
import bimr.bfcl.dto.HotspotDTO;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bimr")
public class BimrResource {

	@EJB
	private BimrOntologyFacade bimrOntologyFacade;

	@Path("/getAllHotspots")
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	public List<HotspotDTO> getAllHotspots() {
		List<HotspotDTO> hotspots = bimrOntologyFacade.getAllHotspots();
		return hotspots;
	}

}