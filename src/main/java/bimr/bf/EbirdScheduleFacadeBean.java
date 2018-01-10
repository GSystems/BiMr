package bimr.bf;

import bimr.bfcl.EbirdFacade;
import bimr.bfcl.EbirdScheduleFacade;
import bimr.bfcl.dto.EbirdRequestDTO;
import bimr.util.EbirdsEnum;

import javax.inject.Inject;

/**
 * @author GLK
 */

public class EbirdScheduleFacadeBean implements EbirdScheduleFacade {

	@Inject
	private EbirdFacade ebirdFacade;


}
