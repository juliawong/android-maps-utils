package com.google.maps.android.kml;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.END_TAG;

/**
 * Parses the feature of a given KML file into a KmlPlacemark or KmlGroundOverlay object
 */
/* package */ class KmlFeatureParser {

    private final static String GEOMETRY_REGEX = "Point|LineString|Polygon|MultiGeometry";

    private final static int LONGITUDE_INDEX = 0;

    private final static int LATITUDE_INDEX = 1;

    private final static String PROPERTY_REGEX = "name|description|visibility";

    private final static String BOUNDARY_REGEX = "outerBoundaryIs|innerBoundaryIs";

    private final static String EXTENDED_DATA = "ExtendedData";

    private final static String STYLE_URL_TAG = "styleUrl";

    private final static String STYLE_TAG = "Style";

    /**
     * Creates a new Placemark object (created if a Placemark start tag is read by the
     * XmlPullParser and if a Geometry tag is contained within the Placemark tag)
     * and assigns specific elements read from the parser to the Placemark.
     */
    /* package */ static KmlPlacemark createPlacemark(XmlPullParser mParser) throws IOException, XmlPullParserException {
        String styleId = null;
        KmlStyle inlineStyle = null;
        HashMap<String, String> properties = new HashMap<String, String>();
        KmlGeometry geometry = null;
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("Placemark"))) {
            if (eventType == START_TAG) {
                if (mParser.getName().equals(STYLE_URL_TAG)) {
                    styleId = mParser.nextText();
                } else if (mParser.getName().matches(GEOMETRY_REGEX)) {
                    geometry = createGeometry(mParser.getName(), mParser);
                } else if (mParser.getName().matches(PROPERTY_REGEX)) {
                    properties.put(mParser.getName(), mParser.nextText());
                } else if (mParser.getName().equals(EXTENDED_DATA)) {
                    properties.putAll(setExtendedDataProperties(mParser));
                } else if (mParser.getName().equals(STYLE_TAG)) {
                    KmlStyleParser styleParser = new KmlStyleParser();
                    styleParser.createStyle(mParser);
                    inlineStyle = styleParser.getStyle();
                }
            }
            eventType = mParser.next();
        }
        // If there is no geometry associated with the Placemark then we do not add it
        if (geometry != null) {
            return new KmlPlacemark(geometry, styleId, inlineStyle, properties);
        }
        return null;
    }

    /**
     * Creates a new GroundOverlay object (created if a GroundOverlay tag is read by the
     * XmlPullParser) and assigns specific elements read from the parser to the GroundOverlay
     */
    /* package */ static KmlGroundOverlay createGroundOverlay(XmlPullParser mParser)
            throws IOException, XmlPullParserException {
        KmlGroundOverlay mGroundOverlay = new KmlGroundOverlay();
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("GroundOverlay"))) {
            if (eventType == START_TAG) {
                if (mParser.getName().equals("Icon")) {
                    mGroundOverlay.setImageUrl(getImageUrl(mParser));
                } else if (mParser.getName().equals("LatLonBox")) {
                    mGroundOverlay.setLatLngBox(createLatLonBox(mGroundOverlay, mParser));
                } else if (mParser.getName().equals("drawOrder")) {
                    mGroundOverlay.setDrawOrder(Float.parseFloat(mParser.nextText()));
                } else if (mParser.getName().equals("visibility")) {
                    mGroundOverlay.setVisibility(Integer.parseInt(mParser.nextText()));
                } else if (mParser.getName().equals("ExtendedData")) {
                    mGroundOverlay.setProperties(setExtendedDataProperties(mParser));
                } else if (mParser.getName().equals("color")) {
                    mGroundOverlay.setColor(mParser.nextText());
                } else if (mParser.getName().matches(PROPERTY_REGEX)) {
                    mGroundOverlay.setProperty(mParser.getName(), mParser.nextText());
                }
            }
            eventType = mParser.next();
        }
        //TODO: Change constructor of GroundOverlay instead of having set methods
        return mGroundOverlay;
    }

    /**
     * Retrieves an image url from the "href" tag nested within a "GroundOverlay" tag, read by
     * the XmlPullParser.
     *
     * @return Image Url for the GroundOverlay
     */

    private static String getImageUrl (XmlPullParser mParser) throws IOException, XmlPullParserException {
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("Icon"))) {
            if (eventType == START_TAG && mParser.getName().equals("href")) {
                return mParser.nextText();
            }
            eventType = mParser.next();
        }
        return null;
    }

    /**
     * Creates a new KmlGeometry object (Created if "Point", "LineString", "Polygon" or
     * "MultiGeometry" tag is read by the XmlPullParser)
     *
     * @param geometryType Type of geometry object to create
     */
    private static KmlGeometry createGeometry(String geometryType, XmlPullParser mParser)
            throws IOException, XmlPullParserException {
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals(geometryType))) {
            if (eventType == START_TAG) {
                if (mParser.getName().equals("Point")) {
                    return createPoint(mParser);
                } else if (mParser.getName().equals("LineString")) {
                    return createLineString(mParser);
                } else if (mParser.getName().equals("Polygon")) {
                    return createPolygon(mParser);
                } else if (mParser.getName().equals("MultiGeometry")) {
                    return createMultiGeometry(mParser);
                }
            }
            eventType = mParser.next();
        }
        return null;
    }

    /**
     * Adds untyped name value pairs parsed from the ExtendedData
     */
    private static HashMap<String, String> setExtendedDataProperties(XmlPullParser mParser)
            throws XmlPullParserException, IOException {
        HashMap<String, String> properties = new HashMap<String, String>();
        String propertyKey = null;
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals(EXTENDED_DATA))) {
            if (eventType == START_TAG) {
                if (mParser.getName().equals("Data")) {
                    propertyKey = mParser.getAttributeValue(null, "name");
                } else if (mParser.getName().equals("value") && propertyKey != null) {
                    properties.put(propertyKey, mParser.nextText());
                    propertyKey = null;
                }
            }
            eventType = mParser.next();
        }
        return properties;
    }

    /**
     * Creates a new KmlPoint object
     *
     * @return KmlPoint object
     */
    private static KmlPoint createPoint(XmlPullParser mParser) throws XmlPullParserException, IOException {
        LatLng coordinate = null;
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("Point"))) {
            if (eventType == START_TAG && mParser.getName().equals("coordinates")) {
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
    private static KmlLineString createLineString(XmlPullParser mParser) throws XmlPullParserException, IOException {
        ArrayList<LatLng> coordinates = new ArrayList<LatLng>();
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("LineString"))) {
            if (eventType == START_TAG && mParser.getName().equals("coordinates")) {
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
    private static KmlPolygon createPolygon(XmlPullParser mParser) throws XmlPullParserException, IOException {
        // Indicates if an outer boundary needs to be defined
        Boolean isOuterBoundary = false;
        ArrayList<LatLng> outerBoundary = new ArrayList<LatLng>();
        ArrayList<ArrayList<LatLng>> innerBoundaries = new ArrayList<ArrayList<LatLng>>();
        int eventType = mParser.getEventType();
        while (!(eventType == END_TAG && mParser.getName().equals("Polygon"))) {
            if (eventType == START_TAG) {
                if (mParser.getName().matches(BOUNDARY_REGEX)) {
                    isOuterBoundary = mParser.getName().equals("outerBoundaryIs");
                } else if (mParser.getName().equals("coordinates")) {
                    if (isOuterBoundary) {
                        outerBoundary = convertToLatLngArray(mParser.nextText());
                    } else {
                        innerBoundaries.add(convertToLatLngArray(mParser.nextText()));
                    }
                }
            }
            eventType = mParser.next();
        }
        return new KmlPolygon(outerBoundary, innerBoundaries);
    }

    /**
     * Creates a new KmlMultiGeometry object
     *
     * @return KmlMultiGeometry object
     */
    private static KmlMultiGeometry createMultiGeometry(XmlPullParser mParser) throws XmlPullParserException, IOException {
        ArrayList<KmlGeometry> geometries = new ArrayList<KmlGeometry>();
        // Get next otherwise have an infinite loop
        int eventType = mParser.next();
        while (!(eventType == END_TAG && mParser.getName().equals("MultiGeometry"))) {
            if (eventType == START_TAG && mParser.getName().matches(GEOMETRY_REGEX)) {
                geometries.add(createGeometry(mParser.getName(), mParser));
            }
            eventType = mParser.next();
        }
        return new KmlMultiGeometry(geometries);
    }

    /**
     * Creates a bound for a LatLonBox and sets the GroundOverlay to contain this bound
     *
     * @param groundOverlay GroundOverlay to set the bound
     * @return LatLngBounds
     */
    private static LatLngBounds createLatLonBox(KmlGroundOverlay groundOverlay, XmlPullParser mParser)
            throws XmlPullParserException, IOException {
        Double north = 0.0;
        Double south = 0.0;
        Double east = 0.0;
        Double west = 0.0;

        int eventType = mParser.next();
        while (!(eventType == END_TAG && mParser.getName().equals("LatLonBox"))) {
            if (eventType == START_TAG) {
                if (mParser.getName().equals("north")) {
                    north = Double.parseDouble(mParser.nextText());
                } else if (mParser.getName().equals("south")) {
                    south = Double.parseDouble(mParser.nextText());
                } else if (mParser.getName().equals("east")) {
                    east = Double.parseDouble(mParser.nextText());
                } else if (mParser.getName().equals("west")) {
                    west = Double.parseDouble(mParser.nextText());
                } else if (mParser.getName().equals("rotation")) {
                    groundOverlay.setRotation(Float.parseFloat(mParser.nextText()));
                }
            }
            eventType = mParser.next();
        }
        return createLatLngBounds(north, south, east, west);
    }

    /**
     * Convert a string of coordinates into an array of LatLngs
     *
     * @param coordinatesString coordinates string to convert from
     * @return array of LatLng objects created from the given coordinate string array
     */
    private static ArrayList<LatLng> convertToLatLngArray(String coordinatesString) {
        ArrayList<LatLng> coordinatesArray = new ArrayList<LatLng>();
        // Need to trim to avoid whitespace around the coordinates such as tabs
        String[] coordinates = coordinatesString.trim().split("(\\s+)");
        for (String coordinate : coordinates) {
            coordinatesArray.add(convertToLatLng(coordinate));
        }
        return coordinatesArray;
    }

    /**
     * Convert a string coordinate from a string into a LatLng object
     *
     * @param coordinateString coordinate string to convert from
     * @return LatLng object created from given coordinate string
     */
    private static LatLng convertToLatLng(String coordinateString) {
        // Lat and Lng are separated by a ,
        String[] coordinate = coordinateString.split(",");
        Double lat = Double.parseDouble(coordinate[LATITUDE_INDEX]);
        Double lon = Double.parseDouble(coordinate[LONGITUDE_INDEX]);
        return new LatLng(lat, lon);
    }

    /**
     * Given a set of four latLng coordinates, creates a LatLng Bound
     *
     * @param north North coordinate of the bounding box
     * @param south South coordinate of the bounding box
     * @param east  East coordinate of the bounding box
     * @param west  West coordinate of the bounding box
     */
    private static LatLngBounds createLatLngBounds(Double north, Double south, Double east, Double west) {
        LatLng southWest = new LatLng(south, west);
        LatLng northEast = new LatLng(north, east);
        return new LatLngBounds(southWest, northEast);
    }
}
