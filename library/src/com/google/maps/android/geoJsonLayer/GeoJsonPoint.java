package com.google.maps.android.geoJsonLayer;

import com.google.android.gms.maps.model.LatLng;

/**
 * A GeoJsonPoint geometry contains a single {@link com.google.android.gms.maps.model.LatLng}.
 */
public class GeoJsonPoint implements GeoJsonGeometry {

    private final static String GEOMETRY_TYPE = "Point";

    private LatLng mCoordinates;

    /**
     * Creates a new GeoJsonPoint
     *
     * @param coordinate coordinate of GeoJsonPoint to store
     */
    public GeoJsonPoint(LatLng coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("Coordinate cannot be null");
        }
        mCoordinates = coordinate;
    }

    /**
     * Gets the type of geometry
     *
     * @return type of geometry
     */
    public String getType() {
        return GEOMETRY_TYPE;
    }

    /**
     * Gets the coordinates of the GeoJsonPoint
     *
     * @return coordinates of the GeoJsonPoint
     */
    public LatLng getCoordinates() {
        return mCoordinates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(GEOMETRY_TYPE).append("{");
        sb.append("\n coordinates=").append(mCoordinates);
        sb.append("\n}\n");
        return sb.toString();
    }
}