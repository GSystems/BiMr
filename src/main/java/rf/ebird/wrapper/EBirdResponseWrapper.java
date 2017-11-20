package main.java.rf.ebird.wrapper;

import java.util.List;

/**
 * @author Rares
 */

public class EBirdResponseWrapper {
	
	private List<EBirdDataWrapper> eBirdData;

	public List<EBirdDataWrapper> getBirdData() {
		return eBirdData;
	}

	public void seteBirdData(List<EBirdDataWrapper> eBirdData) {
		this.eBirdData = eBirdData;
	}
	
}
