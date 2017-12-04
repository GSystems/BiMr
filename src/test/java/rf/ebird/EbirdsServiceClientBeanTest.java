package test.java.rf.ebird;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.rf.ebird.EbirdsServiceClientBean;
import main.java.rf.ebird.wrapper.EBirdRequestWrapper;
import main.java.rf.ebird.wrapper.EBirdResponseWrapper;

public class EbirdsServiceClientBeanTest {

	@Test
	public void testRetrieveEbirdData() {

		EBirdRequestWrapper request = new EBirdRequestWrapper();
		
		request.setRequestUriPattern(
				"http://ebird.org/ws1.1/data/obs/region/recent?rtype=subnational1&r=US-NV&fmt=json");

		EbirdsServiceClientBean client = new EbirdsServiceClientBean();
		EBirdResponseWrapper response = client.retrieveEBirdData(request);

		assertNotNull(response);
		assertNotNull(response.getEbirdData());
	}
}
