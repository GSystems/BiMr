package com.gsys.bimr.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.gsys.bimr.rf.eBird.EbirdsServiceClientBean;
import com.gsys.bimr.rf.model.EBirdRequestWrapper;
import com.gsys.bimr.rf.model.EBirdResponseWrapper;

public class EbirdsServiceClientBeanUnitTesting {

	@Test
	public void testRetrieveEbirdData() {

		EBirdRequestWrapper request = new EBirdRequestWrapper();
		request.setRequestUriPattern(
				"http://ebird.org/ws1.1/data/obs/region/recent?rtype=subnational1&r=US-NV&fmt=json");

		EbirdsServiceClientBean client = new EbirdsServiceClientBean();
		EBirdResponseWrapper response = client.retrieveEBirdData(request);

		assertNotNull(response);
		assertNotNull(response.getBirdData());

		request.setRequestUriPattern(
				"http://ebird.org/ws1.1/data/obs/region/recent?rtype=subnational1&r=US-NV&fmt=json&maxResults=10");
		response = client.retrieveEBirdData(request);

		assertEquals(10, response.getBirdData().size());
	}
}
