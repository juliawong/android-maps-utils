package com.example.juliawong.geojsonintents;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            new DownloadGeoJsonFile(intent.getScheme()).execute(intent.getData());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

    }

    private class DownloadGeoJsonFile extends AsyncTask<Uri, Void, JSONObject> {
        private final String mScheme;

        public DownloadGeoJsonFile(String scheme) {
            mScheme = scheme;
        }

        @Override
        protected JSONObject doInBackground(Uri... params) {
            try {
                InputStream stream = null;
                if (mScheme.equals("file")) {
                    stream = getContentResolver().openInputStream(params[0]);
                } else if (mScheme.startsWith("http")) {
                    stream = new URL(params[0].toString()).openStream();
                }
                String line;
                StringBuilder result = new StringBuilder();
                // Reads from stream
                assert stream != null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                // Read each line of the GeoJSON file into a string
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Converts the result string into a JSONObject
                return new JSONObject(result.toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.i("JSON", jsonObject.toString());
            GeoJsonLayer layer = new GeoJsonLayer(mMap, jsonObject);
            // Set style of polygons to have a blue fill
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
            polygonStyle.setFillColor(Color.BLUE);
            polygonStyle.setGeodesic(true);
            polygonStyle.setStrokeWidth(3);
            layer.setDefaultPolygonStyle(polygonStyle);
            layer.addGeoJsonDataToLayer();
        }
    }
}
