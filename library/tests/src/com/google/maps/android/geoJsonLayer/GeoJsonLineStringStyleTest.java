package com.google.maps.android.geoJsonLayer;

import junit.framework.TestCase;

import android.graphics.Color;

public class GeoJsonLineStringStyleTest extends TestCase {

    GeoJsonLineStringStyle lineStringStyle;

    public void setUp() throws Exception {
        super.setUp();
        lineStringStyle = new GeoJsonLineStringStyle();
    }

    public void testGetGeometryType() throws Exception {
        assertTrue("LineString".matches(lineStringStyle.getGeometryType()));
        assertTrue("MultiLineString".matches(lineStringStyle.getGeometryType()));
        assertTrue("GeometryCollection".matches(lineStringStyle.getGeometryType()));
        assertEquals("LineString|MultiLineString|GeometryCollection", lineStringStyle.getGeometryType());
    }

    public void testColor() throws Exception {
        lineStringStyle.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, lineStringStyle.getColor());
        assertEquals(Color.YELLOW, lineStringStyle.getPolylineOptions().getColor());

        lineStringStyle.setColor(0x76543210);
        assertEquals(0x76543210, lineStringStyle.getColor());
        assertEquals(0x76543210, lineStringStyle.getPolylineOptions().getColor());

        lineStringStyle.setColor(Color.parseColor("#000000"));
        assertEquals(Color.parseColor("#000000"), lineStringStyle.getColor());
        assertEquals(Color.parseColor("#000000"), lineStringStyle.getPolylineOptions().getColor());
    }

    public void testGeodesic() throws Exception {
        lineStringStyle.setGeodesic(true);
        assertTrue(lineStringStyle.isGeodesic());
        assertTrue(lineStringStyle.getPolylineOptions().isGeodesic());
    }

    public void testVisible() throws Exception {
        lineStringStyle.setVisible(false);
        assertFalse(lineStringStyle.isVisible());
        assertFalse(lineStringStyle.getPolylineOptions().isVisible());
    }

    public void testWidth() throws Exception {
        lineStringStyle.setWidth(20.2f);
        assertEquals(20.2f, lineStringStyle.getWidth());
        assertEquals(20.2f, lineStringStyle.getPolylineOptions().getWidth());
    }

    public void testZIndex() throws Exception {
        lineStringStyle.setZIndex(50.78f);
        assertEquals(50.78f, lineStringStyle.getZIndex());
        assertEquals(50.78f, lineStringStyle.getPolylineOptions().getZIndex());
    }

    public void testDefaultLineStringStyle() {
        assertEquals(Color.BLACK, lineStringStyle.getColor());
        assertFalse(lineStringStyle.isGeodesic());
        assertTrue(lineStringStyle.isVisible());
        assertEquals(10.0f, lineStringStyle.getWidth());
        assertEquals(0.0f, lineStringStyle.getZIndex());
    }

    public void testDefaultGetPolylineOptions() throws Exception {
        assertEquals(Color.BLACK, lineStringStyle.getPolylineOptions().getColor());
        assertFalse(lineStringStyle.getPolylineOptions().isGeodesic());
        assertTrue(lineStringStyle.getPolylineOptions().isVisible());
        assertEquals(10.0f, lineStringStyle.getPolylineOptions().getWidth());
        assertEquals(0.0f, lineStringStyle.getPolylineOptions().getZIndex());
    }
}