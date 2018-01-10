package bimr.rf.ebird;

import bimr.rf.ebird.wrapper.EbirdRequestWrapper;
import bimr.rf.ebird.wrapper.EbirdResponseWrapper;

/**
 * @author Rares
 */

public interface EbirdsServiceClient {

	EbirdResponseWrapper retrieveEBirdData(EbirdRequestWrapper request);
	
}
