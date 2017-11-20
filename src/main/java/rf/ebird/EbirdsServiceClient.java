package main.java.rf.ebird;

import main.java.rf.ebird.wrapper.EBirdRequestWrapper;
import main.java.rf.ebird.wrapper.EBirdResponseWrapper;

/**
 * @author Rares
 */

public interface EbirdsServiceClient {

	EBirdResponseWrapper retrieveEBirdData(EBirdRequestWrapper request);
	
}
