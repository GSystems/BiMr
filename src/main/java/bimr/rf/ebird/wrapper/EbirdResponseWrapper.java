package bimr.rf.ebird.wrapper;

import java.util.List;

/**
 * @author Rares
 */

public class EbirdResponseWrapper {
	
	private List<EbirdDataWrapper> ebirdData;

	public List<EbirdDataWrapper> getEbirdData() {
		return ebirdData;
	}

	public void setEbirdData(List<EbirdDataWrapper> ebirdData) {
		this.ebirdData = ebirdData;
	}
	
}
