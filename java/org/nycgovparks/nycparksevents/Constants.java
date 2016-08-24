package org.nycgovparks.nycparksevents;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by Serena on 12/10/15.
 */
public final class Constants {
private Constants() {
        }

public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";

public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

/**
 * Used to set an expiration time for a geofence. After this amount of time Location Services
 * stops tracking the geofence.
 */
public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

/**
 * For this sample, geofences expire after twelve hours.
 */
public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
        GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
public static final float GEOFENCE_RADIUS_IN_METERS = 1000; // 1 km

}
