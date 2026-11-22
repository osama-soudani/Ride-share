package com.rideshare.sos.common.geo;

import com.rideshare.sos.dto.GeoPointDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/** The ONLY place that converts latitude/longitude <-> PostGIS Point. */
public final class GeometryUtils {

    private static final GeometryFactory FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private GeometryUtils() {
    }

    public static Point toPoint(GeoPointDto dto) {
        // JTS order is (x = longitude, y = latitude)
        return FACTORY.createPoint(new Coordinate(dto.longitude(), dto.latitude()));
    }

    public static GeoPointDto toDto(Point point) {
        return new GeoPointDto(point.getY(), point.getX());
    }
}