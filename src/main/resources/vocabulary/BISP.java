package main.resources.vocabulary;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class BISP {
    public static final String uri = "http://xmlns.com/bisp/#";
    private static final Model m = ModelFactory.createDefaultModel();
    public static final Resource LOCATION;
    public static final Resource TWEET;
    public static final Property geo;
    public static final Property longitude;
    public static final Property latitude;
    public static final Property country;
    public static final Property city;
    public static final Property name;
    public static final Property author;
    public static final Property text;
    public static final Property date;
    public static final Property language;
    public static final Property id;

    public BISP() {
    }

    public static String getURI() {
        return uri;
    }

    static {
        LOCATION = m.createResource("http://xmlns.com/bisp/location#");
        TWEET = m.createResource("http://xmlns.com/bisp/tweet#");
        geo = m.createProperty(uri, "geo");
        longitude = m.createProperty(uri, "longitude");
        latitude = m.createProperty(uri, "latitude");
        country = m.createProperty(uri, "country");
        city = m.createProperty(uri, "city");
        name = m.createProperty(uri, "name");
        author = m.createProperty(uri, "author");
        text = m.createProperty(uri, "text");
        date = m.createProperty(uri, "date");
        language = m.createProperty(uri, "language");
        id = m.createProperty(uri, "id");
    }
}

