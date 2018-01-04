package bimr.rf.ebird;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import bimr.rf.ebird.EbirdsServiceClientBean;
import bimr.rf.ebird.wrapper.EBirdRequestWrapper;
import bimr.rf.ebird.wrapper.EBirdResponseWrapper;

public class EbirdsServiceClientBeanTest {

	@Test
	public void testRetrieveEbirdData() {

		EBirdRequestWrapper request = new EBirdRequestWrapper();
		
		request.setRequestUriPattern(
				"mockedUri");

		EbirdsServiceClientBean client = mock(EbirdsServiceClientBean.class);

		EBirdResponseWrapper response = mock(EBirdResponseWrapper.class);
		
		when(client.retrieveEBirdData(request)).thenReturn(response);

		assertNotNull(response);
		assertNotNull(response.getEbirdData());
	}
}
