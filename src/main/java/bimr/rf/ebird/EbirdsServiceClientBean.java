package bimr.rf.ebird;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import bimr.rf.ebird.wrapper.EBirdRequestWrapper;
import bimr.rf.ebird.wrapper.EBirdResponseWrapper;
import bimr.rf.transformer.DataTransformer;

public class EbirdsServiceClientBean implements EbirdsServiceClient {

	public static final Logger log = Logger.getLogger(EbirdsServiceClientBean.class.getName());

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
			response.setEbirdData(DataTransformer.fromEBirdApiResponseToWrapper(content.toString()));
		} catch (IOException e) {
			log.severe("Unable to connect: " + e.getMessage());
		}

		return response;
	}

}
