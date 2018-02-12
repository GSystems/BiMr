package bimr.util;

public enum BimrOntologyEnum {

	RDF_XML_FORMAT("RDF/XML-ABBREV"),
	TURTLE_FORMAT("N-TRIPLES"),

	GET_ALL_TWEETS_QRY(
			  "PREFIX bimr: <http://xmlns.com/bimr#>\n" + "PREFIX user: <http://xmlns.com/bimr/user#>\n"
					  + "PREFIX tweet: <http://xmlns.com/bimr/tweet#>\n"
					  + "PREFIX location: <http://xmlns.com/bimr/location#>\n"
					  + "PREFIX observation: <http://xmlns.com/bimr/observation#>\n"
					  + "PREFIX hotspot: <http://xmlns.com/bimr/hotspot#>\n" + "\n"
					  + "SELECT DISTINCT ?tweet_id ?tweet_text ?tweet_language ?author ?link\n"
					  + "WHERE {\n" +
					  "    ?tweet  bimr:id ?tweet_id;\n"
					  + "          tweet:text ?tweet_text;\n"
					  + "          tweet:language ?tweet_language.\n"
					  + "  OPTIONAL { ?tweet tweet:author ?author }\n"
					  + "  OPTIONAL { ?tweet tweet:link ?link }"
	),

	GET_ALL_HOTSPOTS_QRY(
			"PREFIX bimr: <http://xmlns.com/bimr#>\n" + "PREFIX user: <http://xmlns.com/bimr/user#>\n"
					+ "PREFIX tweet: <http://xmlns.com/bimr/tweet#>\n"
					+ "PREFIX location: <http://xmlns.com/bimr/location#>\n"
					+ "PREFIX observation: <http://xmlns.com/bimr/observation#>\n"
					+ "PREFIX hotspot: <http://xmlns.com/bimr/hotspot#>\n"
					+ "PREFIX uid: <http://www.w3.org/2001/vcard-rdf/3.0#UID>\n"
					+ "PREFIX name: <http://www.w3.org/2001/vcard-rdf/3.0#NAME>\n"
					+ "PREFIX address: <http://schemas.talis.com/2005/address/schema#>\n"
					+ "PREFIX screenName: <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME>\n"
					+ "PREFIX address: <http://www.w3.org/2001/vcard-rdf/3.0#ADR>\n"
					+ "PREFIX email: <http://www.w3.org/2001/vcard-rdf/3.0#EMAIL>\n" + "\n"
					+ "SELECT ?bird_species ?hotspot_id ?how_many ?observation_date ?latitude ?longitude ?tweet_id ?tweet_text ?tweet_language ?information_source_id ?author ?link ?city ?country ?state ?user_name ?user_id ?email ?address ?screen_name ?has_geo_enabled\n"
					+ "WHERE {\n" + "  ?hotspot bimr:id ?hotspot_id;\n"
					+ "           hotspot:observation ?observation.\n"
					+ "  ?observation observation:informationSourceId ?information_source_id;\n"
					+ "        \t   observation:howMany ?how_many;\n"
					+ "               observation:date ?observation_date;\n"
					+ "               observation:tweet ?tweet;\n"
					+ "      \t\t   observation:birdSpecies ?bird_species;\n"
					+ "    \t\t   observation:location ?location.\n"
					+ "  ?location location:latitude ?latitude;        \n"
					+ "            location:longitude ?longitude.\n" + "  ?tweet  bimr:id ?tweet_id;\n"
					+ "          tweet:text ?tweet_text;\n" + "          tweet:language ?tweet_language.\n" + "\n"
					+ "  OPTIONAL { ?location location:country ?country }\n"
					+ "  OPTIONAL { ?location location:city ?city }\n"
					+ "  OPTIONAL { ?location location:state ?state }\n"
					+ "  OPTIONAL { ?tweet tweet:author ?author }\n" + "  OPTIONAL { ?tweet tweet:link ?link }\n"
					+ "  OPTIONAL {\n" + "    ?hotspot uid: ?user_id.\n" + "    ?user uid: ?user_id;\n"
					+ "          name: ?user_name;\n" + "  }\n" + "  OPTIONAL { ?user email: ?email }\n"
					+ "  OPTIONAL { ?user address: ?address }\n" + "  OPTIONAL { ?user screenName: ?screen_name }\n"
					+ "  OPTIONAL { ?user user:hasGeoEnabled ?has_geo_enabled }\n" + "}\n" + "ORDER BY ?bird_species"
	),

	GET_ALL_MIGRATIONS(
			"PREFIX bimr: <http://xmlns.com/bimr#>\n" + "PREFIX user: <http://xmlns.com/bimr/user#>\n"
					+ "PREFIX tweet: <http://xmlns.com/bimr/tweet#>\n"
					+ "PREFIX location: <http://xmlns.com/bimr/location#>\n"
					+ "PREFIX observation: <http://xmlns.com/bimr/observation#>\n"
					+ "PREFIX hotspot: <http://xmlns.com/bimr/hotspot#>\n"
					+ "PREFIX uid: <http://www.w3.org/2001/vcard-rdf/3.0#UID>\n"
					+ "PREFIX name: <http://www.w3.org/2001/vcard-rdf/3.0#NAME>\n"
					+ "PREFIX address: <http://schemas.talis.com/2005/address/schema#>\n"
					+ "PREFIX screenName: <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME>\n"
					+ "PREFIX address: <http://www.w3.org/2001/vcard-rdf/3.0#ADR>\n"
					+ "PREFIX email: <http://www.w3.org/2001/vcard-rdf/3.0#EMAIL>\n"
					+ "PREFIX migration: <http://xmlns.com/bimr/migration#>\n"
					+ "PREFIX ui: <http://www.w3.org/ns/ui#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
					+ "\n"
					+ "SELECT ?species ?migration_id ?from_date ?to_date ?from_latitude ?from_longitude ?to_latitude ?to_longitude ?from_tweet_message ?to_tweet_message ?from_user_name ?from_user_id ?from_hotspot_id ?to_hotspot_id\n"
					+ "WHERE {\n" + "  ?migration observation:birdSpecies ?species;\n"
					+ "             bimr:id ?migration_id;\n" + "             migration:from ?from_hotspot;\n"
					+ "             migration:to ?to_hotspot.\n" + "  ?from_hotspot bimr:id ?from_hotspot_id;\n"
					+ "                hotspot:observation ?from_observation.\n"
					+ "  ?to_hotspot bimr:id ?to_hotspot_id;\n" + "              uid: ?to_user_id;\n"
					+ "              hotspot:observation ?to_observation.\n"
					+ "  ?from_observation observation:date ?from_date;\n"
					+ "                    observation:location ?from_location;\n"
					+ "                    observation:tweet ?from_tweet.\n"
					+ "  ?from_location location:latitude ?from_latitude;\n"
					+ "                 location:longitude ?from_longitude.\n"
					+ "  ?to_observation observation:date ?to_date;\n"
					+ "                  observation:location ?to_location;\n"
					+ "                  observation:tweet ?to_tweet.\n"
					+ "  ?to_location location:latitude ?to_latitude;\n"
					+ "                 location:longitude ?to_longitude.\n"
					+ "  ?from_tweet tweet:text ?from_tweet_message.\n" + "  ?to_tweet tweet:text ?to_tweet_message .\n"
					+ "}\n" + "ORDER BY ?species ?from_date"
	),

	GET_MIGRATIONS_BY_DATE(
			"PREFIX bimr: <http://xmlns.com/bimr#>\n" + "PREFIX user: <http://xmlns.com/bimr/user#>\n"
					+ "PREFIX tweet: <http://xmlns.com/bimr/tweet#>\n"
					+ "PREFIX location: <http://xmlns.com/bimr/location#>\n"
					+ "PREFIX observation: <http://xmlns.com/bimr/observation#>\n"
					+ "PREFIX hotspot: <http://xmlns.com/bimr/hotspot#>\n"
					+ "PREFIX uid: <http://www.w3.org/2001/vcard-rdf/3.0#UID>\n"
					+ "PREFIX name: <http://www.w3.org/2001/vcard-rdf/3.0#NAME>\n"
					+ "PREFIX address: <http://schemas.talis.com/2005/address/schema#>\n"
					+ "PREFIX screenName: <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME>\n"
					+ "PREFIX address: <http://www.w3.org/2001/vcard-rdf/3.0#ADR>\n"
					+ "PREFIX email: <http://www.w3.org/2001/vcard-rdf/3.0#EMAIL>\n"
					+ "PREFIX migration: <http://xmlns.com/bimr/migration#>\n"
					+ "PREFIX ui: <http://www.w3.org/ns/ui#>\n" + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
					+ "\n"
					+ "SELECT ?species ?migration_id ?from_date ?to_date ?from_latitude ?from_longitude ?to_latitude ?to_longitude ?from_tweet_message ?to_tweet_message ?from_user_name ?from_user_id ?from_hotspot_id ?to_hotspot_id\n"
					+ "WHERE {\n" + "  ?migration observation:birdSpecies ?species;\n"
					+ "             bimr:id ?migration_id;\n" + "             migration:from ?from_hotspot;\n"
					+ "             migration:to ?to_hotspot.\n" + "  ?from_hotspot bimr:id ?from_hotspot_id;\n"
					+ "                hotspot:observation ?from_observation.\n"
					+ "  ?to_hotspot bimr:id ?to_hotspot_id;\n" + "              uid: ?to_user_id;\n"
					+ "              hotspot:observation ?to_observation.\n"
					+ "  ?from_observation observation:date ?from_date;\n"
					+ "                    observation:location ?from_location;\n"
					+ "                    observation:tweet ?from_tweet.\n"
					+ "  ?from_location location:latitude ?from_latitude;\n"
					+ "                 location:longitude ?from_longitude.\n"
					+ "  ?to_observation observation:date ?to_date;\n"
					+ "                  observation:location ?to_location;\n"
					+ "                  observation:tweet ?to_tweet.\n"
					+ "  ?to_location location:latitude ?to_latitude;\n"
					+ "                 location:longitude ?to_longitude.\n"
					+ "  ?from_tweet tweet:text ?from_tweet_message.\n" + "  ?to_tweet tweet:text ?to_tweet_message.\n"
					+ "  FILTER (?from_date >= \"%1s\"^^xsd:dateTime && ?to_date <= \"%1s\"^^xsd:dateTime).\n" + "}\n"
					+ "ORDER BY ?species ?from_date"),

	GET_ALL_USERS_QRY (
			"PREFIX user: <http://xmlns.com/bimr/user#>\n" + "PREFIX uid: <http://www.w3.org/2001/vcard-rdf/3.0#UID>\n"
					+ "PREFIX name: <http://www.w3.org/2001/vcard-rdf/3.0#NAME>\n"
					+ "PREFIX address: <http://schemas.talis.com/2005/address/schema#>\n"
					+ "PREFIX screenName: <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME>\n"
					+ "PREFIX email: <http://www.w3.org/2001/vcard-rdf/3.0#EMAIL>\n" + "\n"
					+ "SELECT DISTINCT ?user_name ?user_id ?email ?address ?screen_name ?has_geo_enabled\n"
					+ "WHERE {\n" + "  ?hotspot uid: ?user_id.\n" + "  ?user uid: ?user_id;\n"
					+ "          name: ?user_name;\n"
					+ "  OPTIONAL { ?user email: ?email }\n"
					+ "  OPTIONAL { ?user address: ?address }\n"
					+ "  OPTIONAL { ?user screenName: ?screen_name }\n"
					+ "  OPTIONAL { ?user user:hasGeoEnabled ?has_geo_enabled }\n" + "}\n"
	),

	GET_MOST_OBSERVED_SPECIES_QRY(
			"PREFIX observation: <http://xmlns.com/bimr/observation#>\n"
					+ "PREFIX hotspot: <http://xmlns.com/bimr/hotspot#>\n" + "\n"
					+ "SELECT ?bird_species (COUNT(?bird_species) as ?count)\n"
					+ "WHERE {\n"
					+ "  ?hotspot hotspot:observation ?observation.\n"
					+ "  ?observation observation:informationSourceId ?information_source_id;\n"
					+ "               observation:birdSpecies ?bird_species;\n" + "}\n"
					+ "GROUP BY (?bird_species)\n"
					+ "ORDER BY DESC(?count)"
	);

	private String code;

	BimrOntologyEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
