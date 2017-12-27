package main.java.util;

public enum StandfordEnum {

	LOCATION("LOCATION"),
	PROPS_KEY("annotators"),
	PROPS_VALUE("tokenize, ssplit, pos, lemma, ner"),
	NER_MODEL_KEY("ner.model"),
	NER_MODEL_VALUE("edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");

	private String code;

	StandfordEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
