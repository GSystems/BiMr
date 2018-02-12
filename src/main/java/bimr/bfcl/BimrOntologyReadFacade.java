package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.MigrationDTO;
import bimr.bfcl.dto.StatisticsDTO;

import java.util.List;

/**
 * @author GLK
 */

public interface BimrOntologyReadFacade {

	List<StatisticsDTO> getAllTweets();

	List<HotspotDTO> getAllHotspots();

	List<MigrationDTO> getAllMigrations();

	List<MigrationDTO> getMigrationsByDate(String startDate, String endDate);

	List<StatisticsDTO> getMostObservedSpecies();
}
