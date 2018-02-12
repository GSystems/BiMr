package bimr.util;

/**
 * @author GLK
 */
public class GeneralConstants {

	private GeneralConstants() {}

	public static final String SCHEMA = "BIMR";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String XSD_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final Long DEFAULT_SINCE_ID = 931486142918250496l;
	public static final Integer MAX_NUMBER_HASHTAGS = 10;
	public static final String TWITTER_SOURCE = "twitter";
	public static final String EN_LANGUAGE = "en";

	public static final String FILENAME  = "hotspots.rdf";
	public static final String DIRECTORY_NAME = "database/dataset1";
	public static final String HOTSPOTS_DATASET_ADDR = "http://localhost:3030/hotspots";
	public static final String MIGRATIONS_DATASET_ADDR = "http://localhost:3030/migrations";
	public static final String MOCKED_HOTSPOTS_DATASET_ADDR = "http://localhost:3030/mocked-hotspots";
	public static final String MOCKED_MIGRATIONS_DATASET_ADDR = "http://localhost:3030/mocked-migrations";

	public static final String HOTSPOTS_DATASET_QRY_ADDR = "http://localhost:3030/hotspots/query";
	public static final String MIGRATIONS_DATASET_QRY_ADDR = "http://localhost:3030/migrations/query";
	public static final String MOCKED_HOTSPOTS_DATASET_QRY_ADDR = "http://localhost:3030/mocked-hotspots/query";
	public static final String MOCKED_MIGRATIONS_DATASET_QRY_ADDR = "http://localhost:3030/mocked-migrations/query";

}
