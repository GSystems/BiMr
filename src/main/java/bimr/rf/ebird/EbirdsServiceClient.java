package bimr.rf.ebird;

import bimr.rf.ebird.wrapper.EBirdRequestWrapper;
import bimr.rf.ebird.wrapper.EBirdResponseWrapper;

/**
 * @author Rares
 */

public interface EbirdsServiceClient {

	EBirdResponseWrapper retrieveEBirdData(EBirdRequestWrapper request);
	
}
