package main.java.util;

/**
 * @author GLK
 */
public enum TwitterEnum {

	BIRDMIGRATION("#birdmigration"),
	BIRDSMIGRATION("#birsdmigration"),
	BIRDMIG("#birdmig"),
	BIRDSMIG("#birdsmig"),
	RETWEET("RT"),
	CONSUMER_KEY("jsJnEdd07JSdW46jfAhhYrKBG"),
	CONSUMER_SECRET("aERQTUoMvOYvgpv7Uof19ix9ESBJp0Lvi0T69h5lTkYHRnS6pt"),
	ACCESS_TOKEN("4831081090-3KETssKWWNdFnfagOtnQGDSvUitx7dHS1dkUE16"),
	ACCESS_TOKEN_SECRET("UQg4utsCPp2CiW5XC8cWiiZImcqZa56S9ovCqspTMt0xM");

	private String code;

	TwitterEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
