package bimr.rf.ebird.dao;

import bimr.rf.BaseDAO;
import bimr.rf.ebird.entity.EbirdDataEntity;

import java.util.List;

/**
 * @author GLK
 */
public interface EbirdDAO extends BaseDAO<EbirdDataEntity, Long> {

	/**
	 * Insert a list with EbirdData
	 * @param ebirdDataEntityList
	 */
	void insertDataList(List<EbirdDataEntity> ebirdDataEntityList);

	/**
	 * Retrieve all information stored in database
	 * @return
	 */
	List<EbirdDataEntity> findAllEbirdData();
}
