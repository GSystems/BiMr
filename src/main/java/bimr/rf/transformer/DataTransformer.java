package bimr.rf.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import bimr.rf.twitter.wrapper.TwitterUserWrapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bimr.rf.ebird.wrapper.EbirdDataWrapper;
import bimr.rf.twitter.wrapper.TweetWrapper;
import bimr.util.GeneralConstants;
import twitter4j.Status;

public class DataTransformer {

	private static final Logger log = Logger.getLogger(DataTransformer.class.getName());

	private DataTransformer() {
	}

	public static List<TweetWrapper> fromTwitterApiResponseToWrapper(List<Status> tweets) {
		List<TweetWrapper> tweetsWrapper = new ArrayList<>();
		for (Status status : tweets) {
			TweetWrapper tweetWrapper = new TweetWrapper();
			tweetWrapper.setTweetId(status.getId());
			tweetWrapper.setTweetMessage(status.getText());
			if (status.getGeoLocation() != null) {
				tweetWrapper.setLatitude(String.valueOf(status.getGeoLocation().getLatitude()));
				tweetWrapper.setLongitude(String.valueOf(status.getGeoLocation().getLongitude()));
			}
			tweetWrapper.setObservationDate(status.getCreatedAt());
			tweetWrapper.setUser(fromUserApiToWrapper(status));
			tweetsWrapper.add(tweetWrapper);
		}
		return tweetsWrapper;
	}

	private static TwitterUserWrapper fromUserApiToWrapper(Status status) {
		TwitterUserWrapper user = new TwitterUserWrapper();
		user.setEmail(status.getUser().getEmail());
		user.setId(String.valueOf(status.getUser().getId()));
		user.setIsGeoEnabled(String.valueOf(status.getUser().isGeoEnabled()));
		user.setLocation(status.getUser().getLocation());
		user.setName(status.getUser().getName());
		user.setScreenName(status.getUser().getScreenName());
		return user;
	}

	// TODO make separate methods
	// TODO declare the constants in GeneralConstants class
	public static List<EbirdDataWrapper> fromEBirdApiResponseToWrapper(String ebirdData) {
		// preparing JSON object utility
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject;

		SimpleDateFormat formatter = new SimpleDateFormat(GeneralConstants.DATE_FORMAT);

		try {
			jsonArray = (JSONArray) jsonParser.parse(ebirdData);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}

		@SuppressWarnings("rawtypes")
		Iterator i = jsonArray.iterator();
		List<EbirdDataWrapper> ebirdsWrapper = new ArrayList<>();
		while (i.hasNext()) {
			jsonObject = (JSONObject) i.next();
			EbirdDataWrapper ebirdWrapper = new EbirdDataWrapper();
			ebirdWrapper.setLatitude((Double) jsonObject.get("lat"));
			ebirdWrapper.setLongitude((Double) jsonObject.get("lng"));
			try {
				ebirdWrapper.setObservationDate((Date) formatter.parse((String) jsonObject.get("obsDt")));
			} catch (java.text.ParseException e) {
				log.info(e.getMessage());
			}
			ebirdWrapper.setScientificName((String) jsonObject.get("sciName"));
			ebirdWrapper.setUserDisplayName((String) jsonObject.get("userDisplayName"));
			ebirdsWrapper.add(ebirdWrapper);
		}
		return ebirdsWrapper;
	}

}
