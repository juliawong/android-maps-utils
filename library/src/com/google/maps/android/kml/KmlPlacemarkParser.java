package com.google.maps.android.kml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lavenderch on 1/12/15.
 */
public class KmlPlacemarkParser {


    private static final String GEOMETRY_TAG_REGEX = "Point|LineString|Polygon|MultiGeometry";

    private static final int LONGITUDE_INDEX = 0;

    private static final int LATITUDE_INDEX = 1;

    private static final String PROPERTY_TAG_REGEX = "name|description|visibility";

    private static final String STYLE_TAG = "styleUrl";

    private final ArrayList<KmlPlacemark> mPlacemarks;

    private XmlPullParser mParser;

    public KmlPlacemarkParser(XmlPullParser parser) {
        mPlacemarks = new ArrayList<KmlPlacemark>();
        mParser = parser;
    }

    /**
     * Creates a KmlPlacemark object for each placemark detected if they contain a geometry. Also
     * stores styles and properties for the given placemark.
     */
    public void createPlacemark() throws IOException, XmlPullParserException {
        String style = null;
        HashMap<String, String> properties = new HashMap<String, String>();
        KmlGeometry geometry = null;

        int eventType = mParser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals("Placemark"))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (mParser.getName().equals(STYLE_TAG)) {
                    style = mParser.nextText();
                } else if (mParser.getName().matches(GEOMETRY_TAG_REGEX)) {
                    geometry = createGeometry(mParser.getName());
                } else if (mParser.getName().matches(PROPERTY_TAG_REGEX)) {
                } else if (mParser.getName().matches(PROPERTY_TAG_REGEX)) {
                    properties.put(mParser.getName(), mParser.nextText());
                }
            }
            eventType = mParser.next();
        }
        // If there is no geometry associated with the Placemark then we do not add it
        if (geometry != null) {
            mPlacemarks.add(new KmlPlacemark(geometry, style, properties));
        }
    }

    /**
     * Creates a new KmlGeometry object of type Point, LineString, Polygon or MultiGeometry
     *
     * @param geometryType type of geometry object to create
     */
    private KmlGeometry createGeometry(String geometryType)
            throws IOException, XmlPullParserException {
        int eventType = mParser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals(geometryType))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (mParser.getName().equals("Point")) {
                    return createPoint();
                } else if (mParser.getName().equals("LineString")) {
                    return createLineString();
                } else if (mParser.getName().equals("Polygon")) {
                    return createPolygon();
                } else if (mParser.getName().equals("MultiGeometry")) {
                    return createMultiGeometry();
                }
            }
            eventType = mParser.next();
        }
        return null;
    }

    /**
     * Convert a string coordinate from a string into a LatLng object
     *
     * @param coordinateString coordinate string to convert from
     * @return LatLng object created from given coordinate string
     */
    private LatLng convertToLatLng(String coordinateString) {
        // Lat and Lng are separated by a ,
        String[] coordinate = coordinateString.split(",");
        Double lat = Double.parseDouble(coordinate[LATITUDE_INDEX]);
        Double lon = Double.parseDouble(coordinate[LONGITUDE_INDEX]);
        return new LatLng(lat, lon);
    }

    /**
     * Convert a string of coordinates into an array of LatLngs
     *
     * @param coordinatesString coordinates string to convert from
     * @return array of LatLng objects created from the given coordinate string array
     */
    private ArrayList<LatLng> convertToLatLngArray(String coordinatesString) {
        ArrayList<LatLng> coordinatesArray = new ArrayList<LatLng>();
        // Need to trim to avoid whitespace around the coordinates such as tabs
        String[] coordinates = coordinatesString.trim().split("(\\s+)");
        for (String coordinate : coordinates) {
            coordinatesArray.add(convertToLatLng(coordinate));
        }
        return coordinatesArray;
    }

    /**
     * Creates a new KmlPoint object
     *
     * @return KmlPoint object
     */
    private KmlPoint createPoint() throws XmlPullParserException, IOException {
        LatLng coordinate = null;
        int eventType = mParser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals("Point"))) {
            if (eventType == XmlPullParser.START_TAG && mParser.getName().equals("coordinates")) {
                coordinate = convertToLatLng(mParser.nextText());
            }
            eventType = mParser.next();
        }
        return new KmlPoint(coordinate);
    }

    /**
     * Creates a new KmlLineString object
     *
     * @return KmlLineString object
     */
    private KmlLineString createLineString() throws XmlPullParserException, IOException {
        ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
        int eventType = mParser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals("LineString"))) {
            if (eventType == XmlPullParser.START_TAG && mParser.getName().equals("coordinates")) {
                coordinates = convertToLatLngArray(mParser.nextText());
            }
            eventType = mParser.next();
        }
        return new KmlLineString(coordinates);
    }

    /**
     * Creates a new KmlPolygon object. Parses only one outer boundary and no or many inner
     * boundaries containing the coordinates.
     *
     * @return KmlPolygon object
     */
    private KmlPolygon createPolygon() throws XmlPullParserException, IOException {
        // Indicates if an outer boundary needs to be defined
        Boolean isOuterBoundary = false;
        // Indicates if an inner boundary needs to be defined
        Boolean isInnerBoundary = false;
        ArrayList<LatLng> outerBoundaryCoordinates = new ArrayList<LatLng>();
        ArrayList<ArrayList<LatLng>> innerBoundaryCoordinates = new ArrayList<ArrayList<LatLng>>();

        int eventType = mParser.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals("Polygon"))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (mParser.getName().equals("outerBoundaryIs")) {
                    isOuterBoundary = true;
                } else if (mParser.getName().equals("innerBoundaryIs")) {
                    isInnerBoundary = true;
                } else if (mParser.getName().equals("coordinates")) {
                    if (isOuterBoundary) {
                        outerBoundaryCoordinates = convertToLatLngArray(mParser.nextText());
                        isOuterBoundary = false;
                    } else if (isInnerBoundary) {
                        innerBoundaryCoordinates.add(convertToLatLngArray(mParser.nextText()));
                        isInnerBoundary = false;
                    }
                }
            }
            eventType = mParser.next();
        }
        return new KmlPolygon(outerBoundaryCoordinates, innerBoundaryCoordinates);
    }

    /**
     * Creates a new KmlMultiGeometry object
     *
     * @return KmlMultiGeometry object
     */
    private KmlMultiGeometry createMultiGeometry() throws XmlPullParserException, IOException {
        ArrayList<KmlGeometry> geometries = new ArrayList<KmlGeometry>();
        // Get next otherwise have an infinite loop
        int eventType = mParser.next();
        while (!(eventType == XmlPullParser.END_TAG && mParser.getName().equals("MultiGeometry"))) {
            if (eventType == XmlPullParser.START_TAG && mParser.getName()
                    .matches(GEOMETRY_TAG_REGEX)) {
                geometries.add(createGeometry(mParser.getName()));
            }
            eventType = mParser.next();
        }
        return new KmlMultiGeometry(geometries);
    }


    /**
     * Gets the array of KmlPlacemark objects
     *
     * @return array of KmlPlacemark objects
     */
    public ArrayList<KmlPlacemark> getPlacemarks() {
        return mPlacemarks;
    }



}
