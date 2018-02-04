package bimr.df.model;

import java.util.List;

/**
 * @author Rares
 */

public class EbirdResponse {
	
	private List<EbirdData> ebirdData;

	public List<EbirdData> getEbirdData() {
		return ebirdData;
	}

	// TODO change the methods city
	public void setEbirdData(List<EbirdData> ebirdData) {
		this.ebirdData = ebirdData;
	}
	
}
