package bimr.bfcl;

import bimr.bfcl.dto.HotspotDTO;
import bimr.bfcl.dto.MigrationDTO;

import java.util.List;

/**
 * @author GLK
 */

public interface BimrOntologyReadFacade {

	List<HotspotDTO> getAllTweets();

	List<HotspotDTO> getAllHotspots();

	List<MigrationDTO> getAllMigrations();

	List<MigrationDTO> getMigrationsByDate(String startDate, String endDate);
}
