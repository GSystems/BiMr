package main.java.util;

public enum StandfordEnum {

	LOCATION("location"),
	PROPS_KEY("annotators"),
	PROPS_VALUE("tokenize, ssplit, pos, lemma, ner, parse, dcoref");

	private String code;

	StandfordEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
