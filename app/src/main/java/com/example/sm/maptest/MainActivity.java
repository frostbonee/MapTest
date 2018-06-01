package com.example.sm.maptest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mmi.MapView;
import com.mmi.MapmyIndiaMapView;
import com.mmi.apis.place.Place;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeListener;
import com.mmi.apis.place.reversegeocode.ReverseGeocodeManager;
import com.mmi.events.MapListener;
import com.mmi.events.ScrollEvent;
import com.mmi.events.ZoomEvent;
import com.mmi.layers.BasicInfoWindow;
import com.mmi.layers.MapEventsOverlay;
import com.mmi.layers.MapEventsReceiver;
import com.mmi.layers.Marker;
import com.mmi.layers.UserLocationOverlay;
import com.mmi.layers.location.GpsLocationProvider;
import com.mmi.util.GeoPoint;


import com.mmi.events.ZoomEvent;
import com.mmi.mapmyindia.*;

import java.util.List;


public class MainActivity extends AppCompatActivity {//implements android.location.LocationListener {
    public LocationManager locationManager;
    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapmyIndiaMapView mView = findViewById(R.id.map);
        final MapView mMapView = mView.getMapView();
        final GeoPoint geoPoint = new GeoPoint(18.4639, 73.9189);
        mMapView.setCenter(geoPoint);
        final Marker marker = new Marker(mMapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();

        mMapView.setZoom(5);

        mMapView.computeScroll();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        mMapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent scrollEvent) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent zoomEvent) {
                //boolean x=mMapView.canZoomIn();
                return true;
            }
        });
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            UserLocationOverlay mLocationOverlay;//=new UserLocationOverlay(new GpsLocationProvider(this),mMapView);
            mLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(this), mMapView);

            mLocationOverlay.setCurrentLocationResId(R.mipmap.ic_launcher);
            mLocationOverlay.enableMyLocation();
            mMapView.getOverlays().add(mLocationOverlay);
            mLocationOverlay.getMyLocation();
        //    mMapView.setPositionAndScale(mLocationOverlay,1,b);
            mMapView.invalidate();

            Location location = mLocationOverlay.getLastFix();
         //   Log.d("loc", "current =>"+ location.getLatitude() + " "+ location.getLongitude() );
            if (location != null) {
                Log.d("MainActivity ", " Current location => " + location.getLatitude() + ", " + location.getLongitude());
                GeoPoint current=new GeoPoint(location.getLatitude(),location.getLongitude());
                Marker cm = new Marker(mMapView);

                 cm.setPosition(current);
                 //   mMapView.setCenter(current);
                  cm.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                  mMapView.getOverlays().add(cm);
                  mMapView.invalidate();
            }


            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, new MapEventsReceiver(){
                Marker tap_marker=new Marker(mMapView);
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    BasicInfoWindow infoWindow = new BasicInfoWindow(R.layout.tooltip, mMapView);
                    infoWindow.setTipColor(getResources().getColor(R.color.base_color));

                    tap_marker.setPosition(p);
                  //  tap_marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);

                  //  mMapView.invalidate();
                    ReverseGeocodeManager reverseGeocodeManager;
                    reverseGeocodeManager = new ReverseGeocodeManager();

                    reverseGeocodeManager.getPlace(p, new ReverseGeocodeListener() {
                        @Override
                        public void onResult(int code, Place place) {
                            if(code==0){
                                String addrs=place.toString();
                                //       String address=reverseGeocodeManager.toString();
                                TextView addr=(TextView) findViewById(R.id.tooltip_title);
                                addr.setText(addrs);

                            }

                            //code:0 success, 1 exception, 2 no result
                            // place in response to given GeoPoint

                        }
                    });
                    tap_marker.setInfoWindow(infoWindow);
                    mMapView.getOverlays().add(tap_marker);
                    return true;


                } @Override
                public boolean longPressHelper(GeoPoint p) {
                    tap_marker.remove(mMapView);
                    mMapView.getOverlays().remove(tap_marker);
                    mMapView.invalidate();

                    return true;
                }

            });
            mMapView.getOverlays().add(mapEventsOverlay);
            mMapView.invalidate();







            //  mLocationOverlay.getMyLocationProvider();
            //   mLocationOverlay.setLocation(mLocationOverlay.getLastFix());
            //   GeoPoint current = mLocationOverlay.getMyLocation();
            //        getLocation();
            //         GeoPoint current = new GeoPoint(latitude, longitude);
            //    mMapView.invalidate();

            //    Marker cm = new Marker(mMapView);

            //  cm.setPosition(current);
            //     mMapView.setCenter(current);
            //  cm.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            //  mMapView.getOverlays().add(cm);
            //  mMapView.invalidate();
        }


    }

/*
    protected void getLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager)
                    this.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //   Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

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
    */
}


//    return bestLocation;


//    locationManager.requestLocationUpdates(provider, 1000, 0, this);
//    Location location = locationManager.getLastKnownLocation(locationManager.getProviders(criteria,false);
//  .getBestProvider(criteria, false));
//      double latitude = bestLocation.getLatitude();
//     double longitude = bestLocation.getLongitude();


//       UserLocationOverlay mLocationOverlay;//=new UserLocationOverlay(new GpsLocationProvider(this),mMapView);
//       mLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(this), mMapView);
//       mLocationOverlay.setCurrentLocationResId(R.mipmap.ic_launcher);
//       mLocationOverlay.enableMyLocation();
//       mLocationOverlay.setEnabled(true);
//       mMapView.getOverlays().add(mLocationOverlay);
// GeoPoint current=mLocationOverlay.getMyLocation();

//    mMapView.invalidate();

//   public boolean onZoom(ZoomEvent zoom){
//       mMapView.setZoom(15);
//   }

