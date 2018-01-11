package bimr.rf.ebird;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import bimr.rf.ebird.wrapper.EbirdRequestWrapper;
import bimr.rf.ebird.wrapper.EbirdResponseWrapper;
import bimr.rf.transformer.DataTransformer;

public class EbirdServiceClientBean implements EbirdServiceClient {

	public static final Logger log = Logger.getLogger(EbirdServiceClientBean.class.getName());

	@Override
	public EbirdResponseWrapper retrieveEBirdData(EbirdRequestWrapper request) {

		EbirdResponseWrapper response = new EbirdResponseWrapper();

		try {
			// prep connection + settings
			URL url = new URL(request.getRequestUriPattern());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");

			// reading response from API

			StringBuilder content = new StringBuilder();

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
