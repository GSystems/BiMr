package com.gsys.bimr.rf.eBird;

import com.gsys.bimr.rf.model.EBirdRequestWrapper;
import com.gsys.bimr.rf.model.EBirdResponseWrapper;

/**
 * @author Rares
 */

public interface EbirdsServiceClient {

	EBirdResponseWrapper retrieveEBirdData(EBirdRequestWrapper request);
	
}
