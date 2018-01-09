package main.java.rf.ebird;

import main.java.rf.ebird.wrapper.EbirdRequestWrapper;
import main.java.rf.ebird.wrapper.EbirdResponseWrapper;

/**
 * @author Rares
 */

public interface EbirdsServiceClient {

	EbirdResponseWrapper retrieveEBirdData(EbirdRequestWrapper request);
	
}
