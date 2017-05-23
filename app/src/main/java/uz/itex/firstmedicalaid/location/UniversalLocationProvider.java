package uz.itex.firstmedicalaid.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public class UniversalLocationProvider implements LocationListener {

    public static final int LOCATION_CACHE_TIME = 1000; // 30 seconds
    public static final int REQUIRED_ACCURACY = 50;

    private static String[] providers = new String[]{
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
    };

    private Context context;
    private LocationManager locationManager;
    private ILocationConsumer locationConsumer;
    private Location lastLocation;

    private long locationUpdateMinTime = 0;
    private float locationUpdateMinDistance = 0.0f;

    public UniversalLocationProvider(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean startLocationProvider(ILocationConsumer myLocationConsumer) {
        locationConsumer = myLocationConsumer;
        if (lastLocation != null && lastLocation.hasAccuracy()
                && lastLocation.getAccuracy() < REQUIRED_ACCURACY) {
            locationConsumer.onLocationChanged(lastLocation);
        }

        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            for (String provider : providers) {
                locationManager.requestLocationUpdates(
                        provider,
                        getLocationUpdateMinTime(),
                        getLocationUpdateMinDistance(),
                        this
                );
            }

            return true;
        }

        return false;
    }

    public void stopLocationProvider() {
        if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            locationManager.removeUpdates(this);
        }
    }

    public Location getLastKnownLocation() {
        return lastLocation;
    }

    public void destroy() {
        stopLocationProvider();
        locationManager = null;
        lastLocation = null;
        locationConsumer = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (lastLocation == null ||
                System.currentTimeMillis() - lastLocation.getTime() > LOCATION_CACHE_TIME ||
                (location.hasAccuracy() && location.getAccuracy() < lastLocation.getAccuracy())) {
            lastLocation = location;
            if (locationConsumer != null) {
                locationConsumer.onLocationChanged(location);
            }

            if (lastLocation.hasAccuracy() && lastLocation.getAccuracy() < REQUIRED_ACCURACY) {
                stopLocationProvider();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public long getLocationUpdateMinTime() {
        return locationUpdateMinTime;
    }

    public void setLocationUpdateMinTime(long locationUpdateMinTime) {
        this.locationUpdateMinTime = locationUpdateMinTime;
    }

    public float getLocationUpdateMinDistance() {
        return locationUpdateMinDistance;
    }

    public void setLocationUpdateMinDistance(float locationUpdateMinDistance) {
        this.locationUpdateMinDistance = locationUpdateMinDistance;
    }
}
