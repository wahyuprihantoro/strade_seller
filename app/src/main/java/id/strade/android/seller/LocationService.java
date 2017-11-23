package id.strade.android.seller;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import id.strade.android.seller.storage.Prefs;

/**
 * Created by wahyu on 07 August 2017.
 */

@EBean(scope = EBean.Scope.Singleton)
public class LocationService extends Service implements LocationListener {
    public static final int COARSE_LOCATION_REQUEST_CODE = 123;
    public static final int FINE_LOCATION_REQUEST_CODE = 124;
    private static final int MIN_TIME = 1000 * 1; // in ms
    private static final int MIN_DISTANCE = 10; // in meter
    @RootContext
    Context context;
    @Bean
    Prefs prefs;

    Activity activity;

    private LocationManager locationManager;
    private Location location;
    private boolean gpsEnabled;
    private boolean networkEnabled;
    private LocationChangeListener listener;

    public void setListener(LocationChangeListener listener) {
        this.listener = listener;
    }

    public void init(Activity activity) {
        this.activity = activity;
        getLocation();
    }

    public void stop() {
        locationManager.removeUpdates(this);
    }

    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (networkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }
            if (gpsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    COARSE_LOCATION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_REQUEST_CODE);
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("wahyu loc", location.toString());
        listener.onLocationChange(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface LocationChangeListener{
        void onLocationChange(Location location);
    }
}
