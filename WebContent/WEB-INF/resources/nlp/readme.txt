Guide: https://nlp.stanford.edu/software/crf-faq.shtml

Once you have the annotated the .tsv file you can run this command to serialize the NER model.
java -cp stanford-ner-3.8.0.jar edu.stanford.nlp.ie.crf.CRFClassifier -prop bisp.prop

Last annotated tweet: 2362