package bimr.rf.ebird.dao;

import bimr.rf.BaseDAOBean;
import bimr.rf.ebird.entity.EbirdDataEntity;

import java.util.List;

public class EbirdDAOBean extends BaseDAOBean<EbirdDataEntity, Long> implements EbirdDAO {

	@Override
	public void insertDataList(List<EbirdDataEntity> ebirdDataEntityList) {
		for (EbirdDataEntity ebirdDataEntity : ebirdDataEntityList) {
			insert(ebirdDataEntity);
		}
	}

	@Override
	public List<EbirdDataEntity> findAllEbirdData() {
		return findAll();
	}
}
