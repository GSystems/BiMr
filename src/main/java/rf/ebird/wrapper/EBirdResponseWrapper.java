package main.java.rf.ebird.wrapper;

import java.util.List;

/**
 * @author Rares
 */

public class EBirdResponseWrapper {
	
	private List<EBirdDataWrapper> ebirdData;

	public List<EBirdDataWrapper> getEbirdData() {
		return ebirdData;
	}

	public void setEbirdData(List<EBirdDataWrapper> ebirdData) {
		this.ebirdData = ebirdData;
	}
	
}
