package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.service.GeoService;
import org.springframework.stereotype.Service;

@Service
public class GeoServiceImpl implements GeoService {

    private static final double EARTH_RADIUS_M = 6371000.0; // radio de la Tierra en metros

    @Override
    public double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c;
    }

    @Override
    public boolean withinRadius(double lat1, double lon1, double lat2, double lon2, double radiusMeters) {
        return distanceMeters(lat1, lon1, lat2, lon2) <= radiusMeters;
    }
}
