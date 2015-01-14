package com.google.maps.android.kml;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lavenderc on 12/3/14.
 *
 * Represents a placemark which is either a point, linestring, polygon or multigeometry
 * Stores the properties about the placemark including coordinates
 */
public class KMLFeature {

    private final KmlGeometry mGeometry;

    private final String mStyle;

    private HashMap<String, String> mPlacemarkProperties;

    /**
     * Creates a new KmlPlacemark object
     *
     * @param geometry   geometry object to store
     * @param style      style id to store
     * @param properties properties hashmap to store
     */
    public KMLFeature(KmlGeometry geometry, String style, HashMap<String, String> properties) {
        mPlacemarkProperties = new HashMap<String, String>();
        mGeometry = geometry;
        mStyle = style;
        mPlacemarkProperties = properties;
    }

    /**
     * Gets the style id associated with the placemark
     *
     * @return style id
     */
    public String getStyleID() {
        return mStyle;
    }

    /**
     * Gets the properties hashmap
     *
     * @return properties hashmap
     */
    public Iterator getProperties() {
        return mPlacemarkProperties.keySet().iterator();
    }

    public String getProperty(String keyValue) {
        return mPlacemarkProperties.get(keyValue);
    }

    /**
     * Gets the geometry object
     *
     * @return geometry object
     */
    public KmlGeometry getGeometry() {
        return mGeometry;
    }

}