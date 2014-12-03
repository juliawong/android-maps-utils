package com.google.maps.android.importGeoJson;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by juliawong on 12/2/14.
 * Imports the given GeoJSON file and adds all GeoJSON objects to the map
 */
// TODO return newly added map objects

public class ImportGeoJson {
    private JSONObject JSON_FILE;
    private ArrayList<Object> newGeoJsonObjects = new ArrayList<Object>();

    /**
     * Creates a new ImportGeoJson object
     * @param geojson_file_url URL of GeoJSON file
     * @throws java.io.IOException
     * @throws org.json.JSONException
     */
    public ImportGeoJson(String geojson_file_url) throws IOException, JSONException {
        InputStream stream = new URL(geojson_file_url).openConnection().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        Log.i("GeoJSON file", result.toString());

        JSON_FILE = new JSONObject(result.toString());
    }

    /**
     * Creates a new ImportGeoJson object
     * @param resource_id Raw resource GeoJSON file
     */
    public ImportGeoJson(int resource_id) {
        // convert resource into json
        // parseGeoJsonFile()
    }

    /**
     * Parses all GeoJSON objects in the GeoJSON file
     */
    private void parseGeoJsonFile() {
        if (checkIfFeatureCollection()) {
            // parse JSONArray
            // parseGeoJsonFeature(...)
            // add new object to newGeoJsonObjects
        }
        else {
            // parse a feature
            // parseGeoJsonFeature(JSON_FILE);
        }
    }

    /**
     * Checks if GeoJSON file contains a feature collection
     * @return true if file contains a feature collection, otherwise false
     */
    private boolean checkIfFeatureCollection() {
        // check if type is FeatureCollection
        // throw error if type isn't FeatureCollection or Feature
        // cater for capitalisation
        return false;
    }

    /**
     * Parses a JSONArray containing a coordinate into a single
     * {@link com.google.android.gms.maps.model.LatLng} object
     * @param geoJsonCoordinates JSONArray containing a coordinate from the GeoJSON file
     * @return {@link com.google.android.gms.maps.model.LatLng} object representing the coordinate
     */
    private LatLng coordinateToLatLngArray(JSONArray geoJsonCoordinates) {
        // Parse coordinates into a LatLng object
        return null;
    }

    /**
     * Parses a JSONArray of coordinates into an array of
     * {@link com.google.android.gms.maps.model.LatLng} objects
     * @param geoJsonCoordinates JSONArray of coordinates from the GeoJSON file
     * @return array of {@link com.google.android.gms.maps.model.LatLng}
*                                objects representing the coordinates
     */
    private ArrayList<LatLng> coordinatesToLatLngArray(JSONArray geoJsonCoordinates) {
        // Parse all coordinates into an array of LatLng
        ArrayList<LatLng> coordinatesArray = new ArrayList<LatLng>();
        return coordinatesArray;
    }

    /**
     * Parses a single feature from the GeoJSON file into an object for the map
     * @param geoJsonFeature JSONObject containing one feature
     * @return Map model object created or the array of map model objects created
     */
    private Object parseGeoJsonFeature(JSONObject geoJsonFeature) {
        // TODO convert coords and properties
        // Extract properties
        // Extract GeoJSON object into an array of LatLng
        // swap x, y coords
        // Create & return GeoJSON object

//        try {
//            switch (lowercaseType) {
//                case 'point':
//                    return toMarker(geoJsonFeature);
//
//                case 'multipoint':
//                    return toMarkers(geoJsonFeature);
//
//                case 'linestring':
//                    return toPolyline(geoJsonFeature);
//
//                case 'multilinestring':
//                    return toPolylines(geoJsonFeature);
//
//                case 'polygon':
//                    return toPolygon(geoJsonFeature);
//
//                case 'multipolygon':
//                    return toPolygons(geoJsonFeature);
//            }
//        } catch (error) {
//            // throw errors here
//        }
        return null;
    }

    /**
     * Creates a new map {@link com.google.android.gms.maps.model.Marker} object based on the
     * existing coordinates and properties
     * @param geoJsonPoint JSONObject containing the Point GeoJSON object
     * @return new map {@link com.google.android.gms.maps.model.Marker} model object
     */
    private Marker toMarker(JSONObject geoJsonPoint) {
        // parse GeoJson point obj into Maps Marker
        // Call coordinateToLatLngArray(...)
        // Properties:
        /*
        id
        alpha
        icon
        anchor
        draggable
        rotation
        visible */

        return null;
    }

    /**
     * Creates new map {@link com.google.android.gms.maps.model.Marker} objects based on the
     * existing coordinates and properties
     * @param geoJsonMultiPoint JSONObject containing the MultiPoint GeoJSON object
     * @return an array of new map {@link com.google.android.gms.maps.model.Marker} model objects
     */
    private ArrayList<Marker> toMarkers(JSONObject geoJsonMultiPoint) {
        // parse GeoJson multipoint into multiple Maps Markers
        // Return an array of these new Markers
        // Call toMarker(...)
        return null;
    }

    /**
     * Creates a new {@link com.google.android.gms.maps.model.Polyline} object based on the existing
     * coordinates and properties
     * @param geoJsonLineString JSONObject containing the LineString GeoJSON object
     * @return new {@link com.google.android.gms.maps.model.Polyline} model object
     */
    private Polyline toPolyline(JSONObject geoJsonLineString) {
        // parse GeoJson linestring obj into Maps Polyline
        // Call coordinatesToLatLngArray(...)
        // All elements except for first are holes

        // Properties:
        /*
        id
        width
        color
        z-index
        visible
        geodesic */

        return null;
    }

    /**
     * Creates new {@link com.google.android.gms.maps.model.Polyline} objects based on the existing
     * coordinates and properties
     * @param geoJsonMultiLineString JSONObject containing the MultiLineString GeoJSON object
     * @return an array of new {@link com.google.android.gms.maps.model.Polyline} model objects
     */
    private ArrayList<Polyline> toPolylines(JSONObject geoJsonMultiLineString) {
        // parse GeoJson linestring into multiple Maps Polylines
        // Return an array of these new Polylines
        // Call toPolyline(...)
        return null;
    }

    /**
     * Creates a new {@link com.google.android.gms.maps.model.Polygon} object based on the existing
     * coordinates and properties
     * @param geoJsonPolygon JSONObject containing the Polygon GeoJSON object
     * @return new {@link com.google.android.gms.maps.model.Polygon} model object
     */
    private Polygon toPolygon(JSONObject geoJsonPolygon) {
        // parse GeoJson polygon obj into Maps Polygon
        // Call coordinatesToLatLngArray(...)

        // Properties:
        /*
        id
        stroke width
        stroke color
        fill color
        z-index
        visible
        geodesic */

        return null;
    }

    /**
     * Creates new {@link com.google.android.gms.maps.model.Polygon} objects based on the existing
     * coordinates and properties
     * @param geoJsonMultiPolygon JSONObject containing the Polygon GeoJSON object
     * @return an array of new {@link com.google.android.gms.maps.model.Polygon} model objects
     */
    private ArrayList<Polygon> toPolygons(JSONObject geoJsonMultiPolygon) {
        // parse GeoJson polygon into multiple Maps Polygon
        // Return an array of these new Polygons
        // Call toPolygons(...)
        return null;
    }

}
