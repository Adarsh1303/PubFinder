package com.example.petr_panda.application;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnCameraChangeListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Circle currArea;
    static List<HashMap<String,String>> googlePlaces;
    int count = 0;
    LatLng currentCenter;
    double centerLat;
    double centerLon;
    double radius = 5000;
    EditText radiusBox;
    Button drawButton;
    static Button goButton;
    public boolean checkConnectivity(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        radiusBox = (EditText)findViewById(R.id.radiusArea);
        goButton = (Button)findViewById(R.id.checkPubs);
        drawButton = (Button)findViewById(R.id.drawButton);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    private void draw_circle()
    {

        if(currArea != null)
        {
            currArea.remove();
        }

            CircleOptions circleOptions = new CircleOptions();

            circleOptions.center(new LatLng(centerLat, centerLon));

            circleOptions.radius(radius);

            circleOptions.strokeColor(Color.BLACK);

            //circleOptions.fillColor(0x30ff0000);

            circleOptions.strokeWidth(2);

            currArea = mGoogleMap.addCircle(circleOptions);


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        mGoogleMap.setOnMarkerDragListener(this);
//        mGoogleMap.setOnMapClickListener(this);
//        mGoogleMap.setOnMapLongClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
            else
            {
                checkLocationPermission();
            }
        }
        else
        {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                 centerLat = mGoogleMap.getCameraPosition().target.latitude;
                 centerLon = mGoogleMap.getCameraPosition().target.longitude;
                currentCenter = new LatLng(mGoogleMap.getCameraPosition().target.latitude,mGoogleMap.getCameraPosition().target.longitude);
                if(checkConnectivity(MapsActivity.this)) {
                    locater();
                }
                draw_circle();
//                Toast.makeText(getApplicationContext(),"Lat : " + centerLat + " Lon : "+centerLon,Toast.LENGTH_LONG).show();
            }
        });


        //setUpMapIfNeeded();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

//        Toast.makeText(this,"Location Chaged to " +count,Toast.LENGTH_LONG).show();
        if(count == 0)
        {
                    mLastLocation = location;
                    if (mCurrLocationMarker != null) {

                        mCurrLocationMarker.remove();
                    }

                    count++;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    //mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
//        Toast.makeText(this,"Chupke aa rha hu mai",Toast.LENGTH_LONG).show();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            if(count == 0)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSION_REQUEST_LOCATION = 99;
    private void checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs location permission")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_LOCATION :
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"permission denied",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }



    @Override
    public void onCameraChange(CameraPosition cameraPosition)
    {
        ;
//        mGoogleMap.clear();
//        mGoogleMap.addMarker(new MarkerOptions().position(cameraPosition.target))
    }

    public void Draw(View v)
    {
        Toast.makeText(getApplicationContext(),"AfterCHange",Toast.LENGTH_LONG).show();
        if(radiusBox.getText().length() == 0)
        {
            radius = (double)5000;
        }
        else
        {
            radius = Double.parseDouble(radiusBox.getText().toString());
        }

        if(checkConnectivity(MapsActivity.this)) {
            locater();
        }
        else
        {
            Toast.makeText(MapsActivity.this,"Please connect to internet and input radius",Toast.LENGTH_LONG);
        }
        draw_circle();


    }

    private void locater()
    {
        String place_type = "night_club";
        mGoogleMap.clear();
        if(mCurrLocationMarker!=null)
        {
            mCurrLocationMarker.remove();
        }
        String url = getUrl(centerLat,centerLon,place_type);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mGoogleMap;
        DataTransfer[1] = url;
        Log.d("Doing","Locating");
        GetNearByPlacesData getNearByPlacesData = new GetNearByPlacesData();
        getNearByPlacesData.execute(DataTransfer);
        Toast.makeText(this,"Showing Restaurants",Toast.LENGTH_LONG);
    }

    private String getUrl(double latitude, double longitude,String place_type)
    {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location="+latitude + "," + longitude);
        googlePlacesUrl.append("&radius="+radius);
        googlePlacesUrl.append("&type="+place_type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCCAAgVEzLg2ZPT7F0J7XDZmn2cREa7H5s");
        Log.d("getUrl",googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public void displayList(View view)
    {
//        Toast.makeText(this,"Check pubs button pressed",Toast.LENGTH_LONG);
        Intent intent = new Intent(this,ListActivity.class);
        finish();
        startActivity(intent);
    }



//    public boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null;
//    }
//
//    public  boolean hasInternetAccess(Context context) {
//        if (isNetworkAvailable(context)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection)
//                        (new URL("http://clients3.google.com/generate_204")
//                                .openConnection());
//                urlc.setRequestProperty("User-Agent", "Android");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(1500);
//                urlc.connect();
//                return (urlc.getResponseCode() == 204 &&
//                        urlc.getContentLength() == 0);
//            } catch (IOException e) {
//                Log.e("connection", "Error checking internet connection", e);
//                Toast.makeText(context,"Internet Connection Required",Toast.LENGTH_LONG);
//            }
//        } else {
//            Log.d("After Conncetion", "No network available!");
//        }
//        return false;
//    }

}
