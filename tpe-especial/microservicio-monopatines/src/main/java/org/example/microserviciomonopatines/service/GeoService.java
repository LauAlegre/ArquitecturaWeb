package org.example.microserviciomonopatines.service;

public interface GeoService {

    /**
     * Distancia entre dos puntos (lat/lon) en METROS, usando Haversine.
     */
    double distanceMeters(double lat1, double lon1, double lat2, double lon2);

    /**
     * ¿El punto (lat1, lon1) está dentro de un radio (en METROS)
     * con centro en (lat2, lon2)?
     */
    boolean withinRadius(double lat1, double lon1, double lat2, double lon2, double radiusMeters);
}
