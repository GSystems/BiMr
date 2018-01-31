package bimr.bfcl;

import org.apache.jena.rdf.model.Model;

import java.util.List;

public interface TweetScheduleFacade {

	/**
	 * Retrieve tweets at every 15 minutes
	 */
	void twitterApiCallScheduled();

}
