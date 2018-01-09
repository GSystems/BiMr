package main.java.util;

public enum StanfordEnum {

	LOCATION("LOCATION"),
	BISP("BISP"),
	NUMBER("NUMBER"),
	PROPS_KEY("annotators"),
	PROPS_VALUE("tokenize, ssplit, pos, lemma, ner"),
	NER_MODEL_KEY("ner.model"),

	/**
	 * 3 class:	Location, Person, Organization
	 * 4 class:	Location, Person, Organization, Misc
	 * 7 class: 	Location, Person, Organization, Money, Percent, Date, Time
	 */
	NER_3CLASS_MODEL_VALUE("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz"),
	NER_BISP_MODEL_VALUE("/Users/GLK/Desktop/nlp/test/ner-model.ser.gz");

	private String code;

	StanfordEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
