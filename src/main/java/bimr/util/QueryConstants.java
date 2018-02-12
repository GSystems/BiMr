package bimr.util;

import com.google.common.collect.ImmutableList;

public class QueryConstants {

	public static final ImmutableList<String> migrationConstants = ImmutableList.of("migration_id", "species");

	public static final ImmutableList<String> hotspotConstants = ImmutableList.of("hotspot_id");
	public static final ImmutableList<String> tweetConstants =
			ImmutableList.of("tweet_id", "tweet_text", "author", "link", "tweet_language");
	public static final ImmutableList<String> observationConstants =
			ImmutableList.of("observation_date", "how_many", "information_source_id", "bird_species");
	public static final ImmutableList<String> locationConstants =
			ImmutableList.of("latitude", "longitude", "country", "city", "state");
	public static final ImmutableList<String> userConstants =
			ImmutableList.of("user_id", "email", "address", "user_name", "screen_name", "has_geo_enabled");

	public static final ImmutableList<String> fromHotspotConstants = ImmutableList.of("from_hotspot_id");
	public static final ImmutableList<String> fromTweetConstants =
			ImmutableList.of("from_tweet_id", "from_tweet_text", "from_author", "from_link", "from_tweet_language");
	public static final ImmutableList<String> fromObservationConstants =
			ImmutableList.of("from_date", "from_how_many", "from_information_source_id", "species");
	public static final ImmutableList<String> fromLocationConstants =
			ImmutableList.of("from_latitude", "from_longitude", "from_country", "from_city", "from_state");
	public static final ImmutableList<String> fromUserConstants =
			ImmutableList.of("from_user_id", "from_email", "from_address", "from_user_name", "from_screen_name", "from_has_geo_enabled");

	public static final ImmutableList<String> toHotspotConstants = ImmutableList.of("to_hotspot_id");
	public static final ImmutableList<String> toTweetConstants =
			ImmutableList.of("to_tweet_id", "to_tweet_text", "to_author", "to_link", "to_tweet_language");
	public static final ImmutableList<String> toObservationConstants =
			ImmutableList.of("to_date", "to_how_many", "to_information_source_id", "species");
	public static final ImmutableList<String> toLocationConstants =
			ImmutableList.of("to_latitude", "to_longitude", "to_country", "to_city", "to_state");
	public static final ImmutableList<String> toUserConstants =
			ImmutableList.of("to_user_id", "to_email", "to_address", "to_user_name", "to_screen_name", "to_has_geo_enabled");
}
