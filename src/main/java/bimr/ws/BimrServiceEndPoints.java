package bimr.ws;

import bimr.bf.transformer.GeoJsonTransformer;
import bimr.bfcl.BimrOntologyPersistFacade;
import bimr.bfcl.BimrOntologyReadFacade;
import bimr.bfcl.TweetScheduleFacade;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * @author GLK
 */

@Path("/bimr")
public class BimrServiceEndPoints {

	@EJB
	private BimrOntologyReadFacade bimrOntologyReadFacade;

	@EJB
	private BimrOntologyPersistFacade bimrOntologyPersistFacade;

	@EJB
	private TweetScheduleFacade tweetScheduleFacade;

	@Path("/getAllTweets")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getAllTweetsGeoJson() {
		return GeoJsonTransformer.fromHotspotsToJsonCollection(bimrOntologyReadFacade.getAllTweets()).toMap();
	}

	@Path("/getAllHotspots")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getAllTweets() {
		return GeoJsonTransformer.fromHotspotsToJsonCollection(bimrOntologyReadFacade.getAllHotspots()).toMap();
	}

	@Path("/test")
	@GET
	public void getAllHotspots() {
		tweetScheduleFacade.twitterApiCallScheduled();
	}

	@Path("/getAllMigrations")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getAllMigrations() {
		return GeoJsonTransformer.fromMigrationToJsonCollection(bimrOntologyReadFacade.getAllMigrations()).toMap();
	}

	@Path("/getMigrationsByDate/{startDate}/{endDate}")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String, Object> getMigrationsByDate(
			@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {

		return GeoJsonTransformer
				.fromMigrationToJsonCollection(bimrOntologyReadFacade.getMigrationsByDate(startDate, endDate)).toMap();
	}
}