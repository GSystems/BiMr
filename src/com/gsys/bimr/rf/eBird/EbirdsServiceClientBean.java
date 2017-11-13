package com.gsys.bimr.rf.eBird;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.gsys.bimr.rf.model.EBirdRequestWrapper;
import com.gsys.bimr.rf.model.EBirdResponseWrapper;
import com.gsys.bimr.rf.transformer.DataTransformer;

public class EbirdsServiceClientBean implements EbirdsServiceClient {

	public static final Logger LOGGER = Logger.getLogger(EbirdsServiceClientBean.class.getName());
	
	@Override
	public EBirdResponseWrapper retrieveEBirdData(EBirdRequestWrapper request) {
		
		EBirdResponseWrapper response = new EBirdResponseWrapper();
		
		try {
			// prep connection + settings
			URL url = new URL(request.getRequestUriPattern());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			
			// reading response from API
			
			StringBuffer content = new StringBuffer();
			
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String line;
				// line by line
				while ((line = in.readLine()) != null) {
					content.append(line);
				}
			}
			// sending to DataTransformer
			// TODO rename the method
			response.seteBirdData(DataTransformer.fromEBirdRawResponseToWrapper(content.toString()));
		} catch (IOException e) {
			LOGGER.severe("Unable to connect: " + e.getMessage());
		}
		
		return response;
	}

}
