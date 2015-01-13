package com.google.maps.android.geoJsonLayer;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Created by juliawong on 12/29/14.
 *
 * A feature has a geometry, bounding box, id and set of properties. Styles are also stored in this
 * class.
 */
public class GeoJsonFeature extends Observable {

    private final GeoJsonGeometry mGeoJsonGeometry;

    private final String mId;

    private final ArrayList<LatLng> mBoundingBox;

    private final HashMap<String, String> mProperties;

    private GeoJsonPointStyle mGeoJsonPointStyle;

    private GeoJsonLineStringStyle mGeoJsonLineStringStyle;

    private GeoJsonPolygonStyle mGeoJsonPolygonStyle;

    /**
     * Creates a new Feature object
     *
     * @param GeoJsonGeometry type of geometry to assign to the feature
     * @param id              id to refer to the feature by
     * @param properties      map of data containing properties related to the feature
     */
    public GeoJsonFeature(GeoJsonGeometry GeoJsonGeometry, String id,
            HashMap<String, String> properties, ArrayList<LatLng> boundingBox) {
        mGeoJsonGeometry = GeoJsonGeometry;
        mId = id;
        mBoundingBox = boundingBox;
        mProperties = properties;
        mGeoJsonPointStyle = new GeoJsonPointStyle();
        mGeoJsonLineStringStyle = new GeoJsonLineStringStyle();
        mGeoJsonPolygonStyle = new GeoJsonPolygonStyle();

    }

    /**
     * Gets the hashmap of the properties
     *
     * @return hashmap of properties
     */
    public HashMap<String, String> getProperties() {
        return mProperties;
    }

    /**
     * Gets the PointStyle of the feature
     *
     * @return PointStyle object
     */
    public GeoJsonPointStyle getPointStyle() {
        return mGeoJsonPointStyle;
    }

    /**
     * Sets the PointStyle of the feature
     *
     * @param geoJsonPointStyle PointStyle object
     */
    public void setPointStyle(GeoJsonPointStyle geoJsonPointStyle) {
        mGeoJsonPointStyle = geoJsonPointStyle;
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the LineStringStyle of the feature
     *
     * @return LineStringStyle object
     */
    public GeoJsonLineStringStyle getLineStringStyle() {
        return mGeoJsonLineStringStyle;
    }

    /**
     * Sets the LineStringStyle of the feature
     *
     * @param geoJsonLineStringStyle LineStringStyle object
     */
    public void setLineStringStyle(GeoJsonLineStringStyle geoJsonLineStringStyle) {
        mGeoJsonLineStringStyle = geoJsonLineStringStyle;
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the PolygonStyle of the feature
     *
     * @return PolygonStyle object
     */
    public GeoJsonPolygonStyle getPolygonStyle() {
        return mGeoJsonPolygonStyle;
    }

    /**
     * Sets the PolygonStyle of the feature
     *
     * @param geoJsonPolygonStyle PolygonStyle object
     */
    public void setPolygonStyle(GeoJsonPolygonStyle geoJsonPolygonStyle) {
        mGeoJsonPolygonStyle = geoJsonPolygonStyle;
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the stored geometry object
     *
     * @return geometry object
     */
    public GeoJsonGeometry getGeometry() {
        return mGeoJsonGeometry;
    }

    /**
     * Gets the ID of the feature
     *
     * @return ID of the feature
     */
    public String getId() {
        return mId;
    }

    /**
     * Checks if the geometry is assigned
     *
     * @return true if feature contains geometry object, false if geometry is currently null
     */
    public boolean hasGeometry() {
        return (mGeoJsonGeometry != null);
    }

    /**
     * Gets the array containing the coordinates of the bounding box for the FeatureCollection. If
     * the FeatureCollection did not have a bounding box or if the GeoJSON file did not contain a
     * FeatureCollection then null will be returned.
     *
     * @return array containing bounding box of FeatureCollection, null if no bounding box
     */
    public ArrayList<LatLng> getBoundingBox() {
        return mBoundingBox;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Feature{");
        sb.append("\n bounding box=").append(mBoundingBox);
        sb.append("\n geometry=").append(mGeoJsonGeometry);
        sb.append(",\n point style=").append(mGeoJsonPointStyle);
        sb.append(",\n line string style=").append(mGeoJsonLineStringStyle);
        sb.append(",\n polygon style=").append(mGeoJsonPolygonStyle);
        sb.append(",\n id=").append(mId);
        sb.append(",\n properties=").append(mProperties);
        sb.append("\n}\n");
        return sb.toString();
    }
}
