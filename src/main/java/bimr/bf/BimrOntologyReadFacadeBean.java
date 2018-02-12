package bimr.bf;

import bimr.bf.transformer.BimrRdfTransformer;
import bimr.bfcl.BimrOntologyReadFacade;
import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.MigrationDTO;
import bimr.util.BimrOntologyEnum;
import bimr.util.GeneralConstants;
import bimr.util.QueryConstants;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;

import javax.ejb.Stateless;
import java.util.List;

/**
 * @author GLK
 */

@Stateless
public class BimrOntologyReadFacadeBean implements BimrOntologyReadFacade {


	@Override
	public List<HotspotDTO> getAllHotspots() {
		return BimrRdfTransformer
				.fromRdfToHotspotDTOList(getResultForQuery(GeneralConstants.MOCKED_HOTSPOTS_DATASET_QRY_ADDR, BimrOntologyEnum.GET_ALL_HOTSPOTS_QRY.getCode()),
						QueryConstants.hotspotConstants, QueryConstants.observationConstants,
						QueryConstants.tweetConstants, QueryConstants.locationConstants, QueryConstants.userConstants);
	}

	@Override
	public List<MigrationDTO> getAllMigrations() {
		return BimrRdfTransformer
				.fromRdfToMigrationDTOList(getResultForQuery( GeneralConstants.MOCKED_MIGRATIONS_DATASET_QRY_ADDR, BimrOntologyEnum.GET_ALL_MIGRATIONS.getCode()));
	}

	@Override public List<MigrationDTO> getMigrationsByDate(String startDate, String endDate) {
		String query = String.format(BimrOntologyEnum.GET_MIGRATIONS_BY_DATE.getCode(), startDate, endDate);
		return BimrRdfTransformer.fromRdfToMigrationDTOList(getResultForQuery(GeneralConstants.MOCKED_MIGRATIONS_DATASET_QRY_ADDR, query));
	}

	@Override
	public List<HotspotDTO> getAllTweets() {
		return BimrRdfTransformer
				.fromRdfToHotspotDTOList(getResultForQuery(GeneralConstants.MOCKED_HOTSPOTS_DATASET_QRY_ADDR, BimrOntologyEnum.GET_ALL_TWEETS_QRY.getCode()),
						QueryConstants.hotspotConstants, QueryConstants.observationConstants,
						QueryConstants.tweetConstants, QueryConstants.locationConstants, QueryConstants.userConstants);
	}

	private ResultSet getResultForQuery(String address, String query) {
		return QueryExecutionFactory.sparqlService(address, query).execSelect();
	}
}